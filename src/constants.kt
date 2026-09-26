import com.jakewharton.mosaic.ui.Color

const val VERSION = "0.1.0"
val TUI_FOREGROUND = Color(255, 206, 255)
val TUI_ACTIVE = Color(218, 112, 214)
val ASCII_TITLE = "      ..                    ..           s    \n" +
		". uW8\"         oec :  < .z@8\"`          :8    \n" +
		"`t888         @88888   !@88E           .88    \n" +
		" 8888   .     8\"*88%   '888E   u      :888ooo \n" +
		" 9888.z88N    8b.       888E u@8NL  -*8888888 \n" +
		" 9888  888E  u888888>   888E`\"88*\"    8888    \n" +
		" 9888  888E   8888R     888E .dN.     8888    \n" +
		" 9888  888E   8888P     888E~8888     8888    \n" +
		" 9888  888E   *888>     888E '888&   .8888Lu= \n" +
		".8888  888\"   4888      888E  9888.  ^%888*   \n" +
		" `%888*%\"     '888    '\"888*\" 4888\"    'Y\"    \n" +
		"    \"`         88R       \"\"    \"\"             \n" +
		"               88>                            \n" +
		"               48                             \n" +
		"               '8 "

object Opts {
	var wrapCells = Bounds.WRAP
	var wrapPtr = Bounds.ERROR 
}

enum class Bounds { ERROR, WRAP, CLAMP }

