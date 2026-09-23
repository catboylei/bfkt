import kotlin.test.Test
import kotlin.test.assertEquals
import brainfuck.Tape
import kotlin.test.assertFailsWith
import utils.CellValueOutOfBounds
import utils.PointerOutOfBounds

@kotlin.ExperimentalUnsignedTypes
class TapeTest {

	// basic functions
	@Test 
	fun getSetTest() {
        val tape = Tape(10)
        tape.set(42u)
        assertEquals(42u.toUByte(), tape.get())
    }

	@Test
	fun removeAddTest() {
		val tape = Tape(10)
		tape.add(10u)
		tape.remove(3u)
		assertEquals(7u.toUByte(), tape.get())
	}

	@Test 
	fun addRemovePtrTest() {
		val tape = Tape(10)
		tape.addPtr(3)
		tape.removePtr(2)
		assertEquals(1, tape.ptr)
	}

	// cell bounds behaviour for each setting
	
	@Test 
	fun addRemoveWrapTest() {
		Opts.wrapCells = Bounds.WRAP
		val tape = Tape(10)
		tape.set(5u)
		tape.remove(10u) // 251
		tape.add(20u) // 15
		assertEquals(15u.toUByte(), tape.get())
	}

	@Test
	fun addRemoveClampTest() {
		Opts.wrapCells = Bounds.CLAMP
		val tape = Tape(10)
		tape.add(150u); tape.add(150u)
		assertEquals(255u.toUByte(), tape.get())
		tape.remove(150u); tape.remove(150u)
		assertEquals(0u.toUByte(), tape.get())
	}

	@Test
	fun addRemoveErrorTest() {
		Opts.wrapCells = Bounds.ERROR
		val tape = Tape(10)
		assertFailsWith<CellValueOutOfBounds> { tape.add(150u); tape.add(150u) }
		assertFailsWith<CellValueOutOfBounds> { tape.remove(150u); tape.remove(150u) }
	}

	// ptr bound behaviour 
	
	@Test
	fun addRemovePtrWrapTest() {
		Opts.wrapPtr = Bounds.WRAP
		val tape = Tape(10)
		tape.addPtr(15)
		assertEquals(5, tape.ptr)
		tape.removePtr(8)
		assertEquals(7, tape.ptr)
	}

	@Test
	fun addRemovePtrClampTest() {
		Opts.wrapPtr = Bounds.CLAMP
		val tape = Tape(10)
		tape.addPtr(23)
		assertEquals(9, tape.ptr)
		tape.removePtr(20)
		assertEquals(0, tape.ptr)
	}

	@Test
	fun addRemovePtrErrorTest() {
		Opts.wrapPtr = Bounds.ERROR
		val tape = Tape(10)
		assertFailsWith<PointerOutOfBounds> { tape.addPtr(12) }
        assertFailsWith<PointerOutOfBounds> { tape.removePtr(23) }
	}
}		
