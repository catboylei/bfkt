package brainfuck

import utils.CellValueOutOfBounds
import utils.PointerOutOfBounds

@kotlin.ExperimentalUnsignedTypes
class Tape(val size: Int) {
	// kotlin byte = u8
	private val cells = UByteArray(size)
	// public getter, overwite setter to private
	var ptr: Int = 0
		private set

	fun get(): UByte = cells[ptr] 
	fun set(value: UByte) { cells[ptr] = value } 

	// NOTE: Clamping here adds complexity so wrapping is the default
	// also i believe the convention is for wrapping
	fun add(value: UByte) { 	
		cells[ptr] = when ((cells[ptr].toInt() + value.toInt()) <= 255) {
			true -> (cells[ptr] + value).toUByte()
			else -> when (Opts.wrapCells) {
				Bounds.WRAP -> (cells[ptr] + value).toUByte()
				Bounds.CLAMP -> (cells[ptr] + value).coerceIn(0u, 255u).toUByte()  
				Bounds.ERROR -> throw CellValueOutOfBounds("Value overflow at cell $ptr = ${cells[ptr] + value} (Change behavior with --wrapCell)")
			}
		}
	} 
	fun remove(value: UByte) { 
		cells[ptr] = when ((cells[ptr].toInt() - value.toInt()) >= 0) {
			true -> (cells[ptr] - value).toUByte()
			else -> when (Opts.wrapCells) {
				Bounds.WRAP -> (cells[ptr] - value).toUByte()
				Bounds.CLAMP -> (cells[ptr] - minOf(value, cells[ptr])).toUByte()
				Bounds.ERROR -> throw CellValueOutOfBounds("Value underflow at cell $ptr = ${cells[ptr] - value} (Change behavior with --wrapCell)")
			}
		}
	} 

	// NOTE: Here cleanly erroring on pointer overflow is the default behaviour 
	// to be monitored/changed maybe
	fun addPtr(value: Int) { 
		ptr = when ((ptr + value) < size) {
			true -> ptr + value
			else -> when (Opts.wrapPtr) {
				Bounds.ERROR -> throw PointerOutOfBounds("Pointer overflow at ptr = ${ptr + value} (Change behavior with --wrapPtr)")
				Bounds.CLAMP -> size - 1
				Bounds.WRAP -> (ptr + value) % size
			}
		}
	}
	fun removePtr(value: Int) { 
		ptr = when ((ptr - value) >= 0) {
			true -> ptr - value
			else -> when (Opts.wrapPtr) {
				Bounds.ERROR -> throw PointerOutOfBounds("Pointer underflow at ptr = ${ptr - value} (Change behavior with --wrapPtr)")
				Bounds.CLAMP -> 0
				Bounds.WRAP -> ((ptr - value) % size + size) % size 
			}
		}
	}
}
