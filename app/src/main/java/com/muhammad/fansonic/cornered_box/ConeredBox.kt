package com.muhammad.fansonic.cornered_box

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CorneredBoxScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), contentAlignment = Alignment.Center
    ) {
        CorneredBox(
            modifier = Modifier.fillMaxWidth().height(300.dp),
            contentAlignment = Alignment.Center,
            onClick = {}, cornerColor = Color.Gray, containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentPadding = PaddingValues(16.dp)
        ) {
            Text("Cornered Box", style = MaterialTheme.typography.headlineLarge)
        }
    }
}

@Composable
fun CorneredBox(
    modifier: Modifier = Modifier,
    cornerColor: Color = Color.White,
    containerColor: Color = Color.Transparent,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    cornerStrokeWidth: Dp = 6.dp,
    cornerSize: Dp = 30.dp,
    shape: Shape = RoundedCornerShape(10.dp),
    contentAlignment: Alignment = Alignment.TopStart,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(containerColor, shape)
            .drawBehind {
                var cornerRadius = 0f
                val outline = shape.createOutline(size, layoutDirection, this)
                if (outline is Outline.Rounded) {
                    cornerRadius = outline.roundRect.topLeftCornerRadius.x
                }
                val centerX = size.width / 2f
                val centerY = size.height / 2f
                val startX = 0f
                val startY = 0f
                val endX = size.width
                val endY = size.height
                val horLineSize = size.width - cornerSize.toPx() * 2f
                val verLineSize = size.height - cornerSize.toPx() * 2f
                with(drawContext.canvas.nativeCanvas) {
                    val checkPoint = saveLayer(null, null)
                    drawRoundRect(
                        color = cornerColor,
                        cornerRadius = CornerRadius(cornerRadius),
                        style = Stroke(width = cornerStrokeWidth.toPx(), cap = StrokeCap.Round)
                    )
                    // top
                    drawLine(
                        color = Color.Yellow,
                        start = Offset(x = centerX - horLineSize /2, y = startY),
                        end = Offset(x = centerX + horLineSize / 2, y = startY),
                        strokeWidth = 20f,
                        blendMode = BlendMode.Clear, cap = StrokeCap.Round
                    )
                    // bottom
                    drawLine(
                        color = Color.Yellow,
                        start = Offset(x = centerX - horLineSize /2, y = endY),
                        end = Offset(
                            x = centerX + horLineSize / 2, y = endY
                        ), strokeWidth = 20f, blendMode = BlendMode.Clear, cap = StrokeCap.Round
                    )
                    // left
                    drawLine(
                        color = Color.Yellow,
                        start = Offset(x = startX, y = centerY - verLineSize / 2),
                        end = Offset(x = startX, y = centerY + verLineSize / 2),
                        strokeWidth = 20f, cap = StrokeCap.Round,
                        blendMode = BlendMode.Clear
                    )
                    // right
                    drawLine(
                        color = Color.Yellow,
                        start = Offset(x = endX, y = centerY - verLineSize / 2),
                        end = Offset(x = endX, y = centerY + verLineSize / 2),
                        strokeWidth = 20f, cap = StrokeCap.Round,
                        blendMode = BlendMode.Clear
                    )
                    restoreToCount(checkPoint)
                }
            }
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(contentPadding),
        content = { content() }, contentAlignment = contentAlignment
    )
}