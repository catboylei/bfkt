package utils 

// instructions to not be folded (for obvious reasons)
private val noFold = setOf('[', ']', '.', ',')

// creates a jump table from the precompiled code
fun buildJumpTable(code: Pair<CharArray, IntArray>): IntArray {
    val ins = code.first
    val table = IntArray(ins.size)
    val stack = ArrayDeque<Int>()

    for (i in ins.indices) {
        when (ins[i]) {
            '[' -> stack.addLast(i)
            ']' -> {
                val open = stack.removeLastOrNull() ?: throw UnmatchedBracket("Unmatched ] bracket")
                table[open] = i
                table[i] = open
            }
        }
    }

    if (stack.isNotEmpty()) { throw UnmatchedBracket("Unmatched [ bracket") }
    return table
}

// folds provided filtered code into precompiled arrays
fun foldCode(code: String): Pair<CharArray, IntArray> {
	val ins = mutableListOf<Char>()
	val mult = mutableListOf<Int>()

	for (c in code) {
		when (c) {
			in noFold -> { ins.add(c); mult.add(1) }
			ins.lastOrNull() -> { mult[mult.lastIndex]++ }
			else -> {
				ins.add(c); mult.add(1)	
			}
		}
	} 

	return Pair(ins.toCharArray(), mult.toIntArray())
}
