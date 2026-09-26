package tui

import ASCII_TITLE
import TUI_FOREGROUND
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.layout.fillMaxHeight
import com.jakewharton.mosaic.layout.fillMaxSize
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Text
import kotlinx.coroutines.awaitCancellation
import com.jakewharton.mosaic.LocalTerminalState
import com.jakewharton.mosaic.layout.KeyEvent
import com.jakewharton.mosaic.layout.size
import com.jakewharton.mosaic.layout.width
import com.jakewharton.mosaic.ui.Alignment
import com.jakewharton.mosaic.ui.Arrangement
import com.jakewharton.mosaic.ui.Box
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Spacer

enum class Pages {
    MAIN,
    RUN,
    BUILD,
    DEBUG
}

val lines: Map<String, Pages> = mapOf(
    "Run" to Pages.RUN,
    "Build" to Pages.BUILD,
    "Debug" to Pages.DEBUG
)

@Composable
fun Tui(source: String) {
    var isOpen by remember { mutableStateOf(true) }
    val terminalSize = LocalTerminalState.current.size
    var openPage by remember { mutableStateOf(Pages.MAIN) }

    val minWidth = 120
    val minHeight = 45

    if (terminalSize.columns < minWidth || terminalSize.rows < minHeight) {
        Box(Modifier.size(width = terminalSize.columns, height = terminalSize.rows - 1).onKeyEvent { event -> isOpen = false; "q" == event.key }, contentAlignment = Alignment.Center) {
            Text(
                "Terminal too small: ${terminalSize.columns}x${terminalSize.rows} (min ${minWidth}x${minHeight}) \n(you can always use the regular CLI)",
                color = Color.Red,
            )
        }
    } else {
        Column(
            Modifier.size(width = terminalSize.columns, height = terminalSize.rows - 1)
                .onKeyEvent { event ->
                    when (event.key) {
                        "q" -> isOpen = false
                        else -> return@onKeyEvent false
                    }

                    return@onKeyEvent true
                }
        ) {
            Block(Modifier.fillMaxSize(), title = "Bfkt-tui <3") {
                when (openPage) {
                    Pages.MAIN -> MainScreen { index ->
                        openPage = lines.values.toList()[index]
                    }
                    else -> Spacer() // TODO: other pages !!
                }
            }
        }
    }

    LaunchedEffect(isOpen) {
        if (isOpen) {
            awaitCancellation()
        }
    }
}

@Composable
fun MainScreen(onSelect: (Int) -> Unit) {
    var selected by remember { mutableStateOf(0) }

    fun increment() { selected = (selected + 1).mod(lines.size) }
    fun decrement() { selected = (selected - 1).mod(lines.size) }

    Column(Modifier.fillMaxSize()
        .onKeyEvent { event ->
            when (event.key) {
                "j" -> increment()
                "k" -> decrement()
                "Enter" -> onSelect(selected)
                else -> return@onKeyEvent false
            }

            return@onKeyEvent true
        }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2))
    {
        Spacer(Modifier.size(1, 10))
        Text(ASCII_TITLE, color = TUI_FOREGROUND)
        Selection(Modifier.width(20).fillMaxHeight(), selected)
    }
}