package brainfuck

import utils.buildJumpTable
import utils.foldCode

class Program(source: String) {
	// NOTE: Currently (intentionally) no support for ! and # commands 
	val code: Pair<CharArray, IntArray> = foldCode( source.filter { it in "+-<>[].," } ) // directly store precompiled code	
	val jumpTable = buildJumpTable(code)
}
