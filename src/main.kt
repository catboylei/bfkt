import brainfuck.Program
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.boolean
import utils.FileException
import utils.readText
import com.jakewharton.mosaic.runMosaicBlocking
import com.jakewharton.mosaic.NonInteractivePolicy.Ignore
import tui.Tui

fun enterAlt() { println("\u001B[?1049h\u001B[?25l") } // alt screen and hide cursor
fun leaveAlt() { println("\u001B[?25h\u001B[?1049l") }

class Bfkt : CliktCommand() {
    override fun help(context: Context) = "A (fancy) Brainfuck Toolchain"

	override val printHelpOnEmptyArgs = true
	override fun run() = Unit

	// hook up --version flag 
	init {
		versionOption(VERSION)
	}
}

// TODO: fix flags for wrapPtr and wrapCells 
class Run : CliktCommand() {
    override fun help(context: Context) = "Run Brainfuck Program"

	// cannot use file type arg because no JVM :/
    val file: String by argument(help = "Source file")

	val nocompile: Boolean by option(help = "Interpret without compiling").boolean().default(false)
    val wrap: Boolean by option(help = "Wrap pointer and cell values on overflow").boolean().default(false)

    override fun run() { 

		val source = try {
            readText(file)
        } catch (e: FileException) {
            throw CliktError(e.message)
        }

        Program(source)
	}
}

class Build : CliktCommand() {
    override fun help(context: Context) = "Compile Brainfuck Program (x86-64 Linux)"

    val file: String by argument(help = "Source file")
    
	val wrap: Boolean by option(help = "Wrap pointer and cell values on overflow").boolean().default(false)

    override fun run() { 
		// TODO: yk the entire compiler no big deal
	}
}

class Tui : CliktCommand() {
	override fun help(context: Context) = "Open the TUI"

    val file: String by argument(help = "Source file")
	
	override fun run() {

		val source = try {
            readText(file)
        } catch (e: FileException) {
            throw CliktError(e.message)
        }

		enterAlt()
		try {	
			runMosaicBlocking(onNonInteractive = Ignore) {
				Tui(source)
			}
		} finally {
			leaveAlt()
		}
	}
}

fun main(args: Array<String>) = Bfkt().subcommands(Run(), Build(), Tui()).main(args)
