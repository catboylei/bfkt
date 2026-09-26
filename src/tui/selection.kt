package tui

import TUI_ACTIVE
import TUI_FOREGROUND
import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Alignment
import com.jakewharton.mosaic.ui.Arrangement
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Text

@Composable
fun Selection(modifier: Modifier = Modifier, selected: Int) {
    fun charIfSelected(char: Char, index: Int): Char {
        return if (index == selected) char else ' '
    }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(1),) {
        Text(" ")
        lines.keys.forEachIndexed { index, line ->
            Text("${charIfSelected('>', index)} $line ${charIfSelected('<', index)}", color = if (index == selected) TUI_ACTIVE else TUI_FOREGROUND)
        }
    }
}