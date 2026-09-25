package tui

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
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Text
import kotlinx.coroutines.awaitCancellation
import com.jakewharton.mosaic.LocalTerminalState
import com.jakewharton.mosaic.layout.size
import com.jakewharton.mosaic.layout.width
import com.jakewharton.mosaic.ui.Alignment
import com.jakewharton.mosaic.ui.Box
import com.jakewharton.mosaic.ui.Color

@Composable
fun Tui(source: String) {
    var isOpen by remember { mutableStateOf(true) }
    val terminalSize = LocalTerminalState.current.size

    val minWidth = 120
    val minHeight = 20

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
                Row(Modifier.fillMaxSize()) {
                    Column(Modifier.weight(2f).fillMaxHeight()) { Text(source) }
                    VerticalSeparator()
                    Column(Modifier.width(20).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) { Text("stuff 1"); Text("stuff 2"); Text("stuff 3")}
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