import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.boolean
import com.github.ajalt.clikt.parameters.types.choice
import utils.FileException
import utils.readText

class Bfkt : CliktCommand() {
    override fun help(context: Context) = "A (fancy) Brainfuck Toolchain"

	override val printHelpOnEmptyArgs = true
	override fun run() = Unit

	init {
		versionOption(VERSION)
	}
}

class Run : CliktCommand() {
    override fun help(context: Context) = "Run Brainfuck Program"

	// cannot use file arg because no JVM :/
    val file: String by argument(help = "Source file")

	val nocompile: Boolean by option(help = "Interpret without compiling").boolean().default(false)
    val wrap: Boolean by option(help = "Wrap pointer and cell values on overflow").boolean().default(false)

    override fun run() { 

		val source = try {
            readText(file)
        } catch (e: FileException) {
            throw CliktError(e.message)
        }

        println(source)
	}
}

class Build : CliktCommand() {
    override fun help(context: Context) = "Compile Brainfuck Program (x86-64 Linux)"

    val file: String by argument(help = "Source file")
    
	val wrap: Boolean by option(help = "Wrap pointer and cell values on overflow").boolean().default(false)

    override fun run() { 
	}
}

fun main(args: Array<String>) = Bfkt().subcommands(Run(), Build()).main(args)
