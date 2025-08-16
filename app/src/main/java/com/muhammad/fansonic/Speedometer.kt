package com.muhammad.fansonic

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Speedometer(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 100.0) currentSpeed: Float,
) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .drawBehind {
                val glowRadius = size.height / 2
                val glowColor = when {
                    currentSpeed < 50 -> Color(0xFF4CAF50)
                    currentSpeed > 80 -> Color(0xFFFFC107)
                    else -> Color(0xFFF44336)
                }
                val alpha = currentSpeed / 100f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            glowColor.copy(0.3f * alpha),
                            glowColor.copy(alpha = 0f)
                        ), center = center, radius = glowRadius + 60f
                    ), radius = glowRadius + 60f, center = center
                )
            }) {
        val circleRadius = size.height / 2
        val arcStrokeWith = 20.dp.toPx()
        val startAngle = 130f
        val sweepAngle = 280f
        val mainColor = when {
            currentSpeed < 50 -> Color(0xFF4CAF50) // Green
            currentSpeed < 80 -> Color(0xFFFFC107) // Amber
            else -> Color(0xFFF44336)
        }
        drawArc(
            color = Color.LightGray,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = arcStrokeWith, cap = StrokeCap.Round)
        )
        drawArc(
            color = mainColor,
            startAngle = startAngle,
            sweepAngle = (currentSpeed / 100f) * sweepAngle,
            useCenter = false,
            style = Stroke(width = arcStrokeWith, cap = StrokeCap.Round)
        )
        for (speed in 0..100 step 10) {
            val angleInRed = (startAngle + (speed / 100f) * sweepAngle) * (PI / 180f)
            val lineLength = if (speed % 20 == 0) 20f else 10f
            val lineThickness = if (speed % 20 == 0) 3.dp.toPx() else 2.dp.toPx()
            val startOffset = calculateOffset(
                degrees = angleInRed,
                radius = circleRadius - arcStrokeWith / 2 - 5f,
                center = center
            )
            val endOffset = calculateOffset(
                degrees = angleInRed,
                radius = circleRadius - arcStrokeWith / 2 - 5f - lineLength,
                center = center
            )
            drawLine(
                color = Color.Black,
                start = startOffset,
                end = endOffset,
                strokeWidth = lineThickness, cap = StrokeCap.Round
            )
            if (speed % 10 == 0) {
                val textMarker = textMeasurer.measure(
                    text = speed.toString(),
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                )
                val textWidth = textMarker.size.width
                val textHeight = textMarker.size.height
                val textOffset =
                    calculateOffset(angleInRed, circleRadius - arcStrokeWith - 50f, center)
                drawText(
                    textMarker,
                    color = Color.Black,
                    topLeft = Offset(
                        x = textOffset.x - textWidth / 2,
                        y = textOffset.y - textHeight / 2
                    )
                )
            }
        }
        val needleAngle = (startAngle + (currentSpeed / 100f) * sweepAngle) * (PI / 180f)
        val needleEnd = calculateOffset(needleAngle, circleRadius - 40f, center)
        drawLine(
            color = Color.Black,
            start = center,
            end = needleEnd, strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round
        )
    }
}

fun calculateOffset(degrees: Double, radius: Float, center: Offset): Offset {
    val x = (radius * cos(degrees) + center.x).toFloat()
    val y = (radius * sin(degrees) + center.x).toFloat()
    return Offset(x, y)
}