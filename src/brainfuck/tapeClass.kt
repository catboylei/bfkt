package brainfuck 

@kotlin.ExperimentalUnsignedTypes
class Tape(size: Int) {
	// kotlin byte = u8
	private val cells = UByteArray(size)
	// public getter, overwite setter to private
	var ptr = 0
		private set

	fun get(): UByte = cells[ptr] 
	fun set(value: UByte) { cells[ptr] = value } 
	// scuffed because += isnt fully supported yet
	fun add(value: UByte) { cells[ptr] = (cells[ptr] + value).toUByte() } 
	fun remove(value: UByte) { cells[ptr] = (cells[ptr] - value).toUByte() } 
}
