import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
 
class Hello : CliktCommand() {
    val value: String by option().default("mrrra").help("meow mrrp mraow purrr")

    override fun run() {
        echo("meow $value")
    }
}

fun main(args: Array<String>) = Hello().main(args)
