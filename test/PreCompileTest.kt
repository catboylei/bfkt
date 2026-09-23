import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertEquals
import utils.foldCode
import utils.buildJumpTable
import utils.UnmatchedBracket


class PreCompileTest {

	// folding
	@Test
	fun emptyCodeTest() {
		val (ins, mult) = foldCode("")
		assertContentEquals(charArrayOf(), ins)
		assertContentEquals(intArrayOf(), mult)
	}

	@Test
	fun foldTest() {
		val (ins, mult) = foldCode("+++><<[[]],----")
		assertContentEquals(charArrayOf('+', '>', '<', '[', '[', ']', ']', ',', '-'), ins)
        assertContentEquals(intArrayOf(3, 1, 2, 1, 1, 1, 1, 1, 4), mult)
	}
	
	// jump table building
	@Test
	fun jumpTableUnmatchedTest() {
		assertFailsWith<UnmatchedBracket> { buildJumpTable(foldCode("[+")) }	
		assertFailsWith<UnmatchedBracket> { buildJumpTable(foldCode("+]")) }	
	}

	@Test 
	fun jumpTableTest() {
		val table = buildJumpTable(foldCode("++[>++[<+>-]<-]>."))
		assertContentEquals(intArrayOf(0, 12, 0, 0, 9, 0, 0, 0, 0, 4, 0, 0, 1, 0, 0), table)
	}


}
