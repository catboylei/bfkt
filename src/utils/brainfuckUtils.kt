package utils 

// instructions to not be folded (for obvious reasons)
private val noFold = setOf('[', ']', '.', ',')

fun buildJumpTable(code: Pair<CharArray, IntArray>): IntArray {
    val ins = code.first
    val table = IntArray(ins.size)
    val stack = IntArray(ins.size)
	var sp = 0 // stack pointer

    for (i in ins.indices) {
        when (ins[i]) {
            '[' -> stack[sp++] = i
            ']' -> {
				if (sp == 0) throw UnmatchedBracket("Unmatched ] bracket")
                val open = stack[--sp]
                table[open] = i
                table[i] = open
			}
        }
    }

	if (sp != 0) { throw UnmatchedBracket("Unmatched [ bracket") }
    return table
}

fun foldCode(code: String): Pair<CharArray, IntArray> {
	// compute at worst possible array length
    val ins = CharArray(code.length)
    val mult = IntArray(code.length)
    var count = 0

    fun lastWritten(): Char? = if (count > 0) ins[count - 1] else null

    for (c in code) {
        when (c) {
            in noFold -> { ins[count] = c; mult[count] = 1; count++ }
            lastWritten() -> { mult[count - 1]++ }
            else -> { ins[count] = c; mult[count] = 1; count++ }
        }
    }

	// trim the arrays 
    return Pair(ins.copyOf(count), mult.copyOf(count))
}
