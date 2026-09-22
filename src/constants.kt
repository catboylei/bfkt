const val VERSION = "0.1.0"

object Opts {
	var wrapCells = Bounds.WRAP
	var wrapPtr = Bounds.ERROR 
}

enum class Bounds { ERROR, WRAP, CLAMP }

