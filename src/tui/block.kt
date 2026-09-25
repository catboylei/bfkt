package tui

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.layout.drawBehind
import com.jakewharton.mosaic.layout.padding
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Box
import com.jakewharton.mosaic.ui.BoxScope
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.TextStyle
import com.jakewharton.mosaic.ui.unit.IntOffset
import com.jakewharton.mosaic.ui.unit.IntSize

@Composable
fun Block(modifier: Modifier = Modifier, title: String? = null, borderColor: Color = Color.Unspecified, content: @Composable BoxScope.() -> Unit, ) {
    Box(
        modifier.drawBehind {
            if (width < 2 || height < 2) return@drawBehind

            // corners
            drawRect('╭', foreground = borderColor, topLeft = IntOffset(0, 0), size = IntSize(1, 1))
            drawRect('╮', foreground = borderColor, topLeft = IntOffset(width - 1, 0), size = IntSize(1, 1))
            drawRect('╰', foreground = borderColor, topLeft = IntOffset(0, height - 1), size = IntSize(1, 1))
            drawRect('╯', foreground = borderColor, topLeft = IntOffset(width - 1, height - 1), size = IntSize(1, 1))

            // edges
            drawRect('─', foreground = borderColor, topLeft = IntOffset(1, 0), size = IntSize(width - 2, 1))
            drawRect('─', foreground = borderColor, topLeft = IntOffset(1, height - 1), size = IntSize(width - 2, 1))
            drawRect('│', foreground = borderColor, topLeft = IntOffset(0, 1), size = IntSize(1, height - 2))
            drawRect('│', foreground = borderColor, topLeft = IntOffset(width - 1, 1), size = IntSize(1, height - 2))

            if (title != null) {
                drawText(0, 2, " $title ", foreground = borderColor, textStyle = TextStyle.Bold)
            }
        }
    ) {
        Box(Modifier.padding(1), content = content)
    }
}