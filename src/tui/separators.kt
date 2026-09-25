package tui

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.layout.drawBehind
import com.jakewharton.mosaic.layout.fillMaxHeight
import com.jakewharton.mosaic.layout.width
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Box
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.unit.IntOffset
import com.jakewharton.mosaic.ui.unit.IntSize

@Composable
fun VerticalSeparator(modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Box(modifier.width(1).fillMaxHeight().drawBehind {
        if (height <= 0) return@drawBehind
        drawRect('│', foreground = color, topLeft = IntOffset(0, 0), size = IntSize(1, height))
    })
}