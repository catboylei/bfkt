package brainfuck

import utils.buildJumpTable
import utils.foldCode
import kotlin.time.measureTime

class Program(source: String) {
	// NOTE: Currently (intentionally) no support for ! and # commands 
	val code: Pair<CharArray, IntArray>
    val jumpTable: IntArray

	init {
        println("Pre-compiling...")
		val duration = measureTime {
            code = foldCode(source.filter { it in "+-<>[].," })
            jumpTable = buildJumpTable(code)
        }
        println("Done in ${duration}!")
	}

	fun debug() {
		println(code.first.concatToString())
		println(code.second.contentToString())
		println(jumpTable.contentToString())
	}
}
