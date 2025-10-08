package com.muhammad.fansonic.clock

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ClockScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Clock(modifier = Modifier.size(300.dp))
    }
}

@Composable
fun Clock(
    modifier: Modifier = Modifier,
    hourColor: Color = MaterialTheme.colorScheme.secondary,
    minuteColor: Color = MaterialTheme.colorScheme.onBackground,
    secondColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    var currentTime by remember { mutableStateOf(Calendar.getInstance()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Calendar.getInstance()
            delay(990L)
        }
    }
    val hours = currentTime.get(Calendar.HOUR_OF_DAY)
    val minutes = currentTime.get(Calendar.MINUTE)
    val seconds = currentTime.get(Calendar.SECOND)
    val hourAngle = (hours % 12 + minutes / 60f) * 30f
    val minuteAngle = minutes * 6f
    val secondAngle = seconds * 6f
    val canvasColor = MaterialTheme.colorScheme.surfaceContainer
    Box(modifier = modifier.aspectRatio(1f), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2f
            val center = Offset(x = size.width / 2f, y = size.height / 2f)
            drawCircle(color = canvasColor, radius = radius,)
            for (i in 0 until 12) {
                val angle = i * 30f
                rotate(angle, center) {
                    drawLine(
                        color = hourColor.copy(0.6f),
                        start = Offset(x = center.x, center.y - radius * 1f),
                        end = Offset(center.x, center.y - radius * 0.9f),
                        strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round
                    )
                }
            }
            for (i in 0 until 60) {
                if (i % 5 != 0) {
                    val angle = i * 6f
                    rotate(angle, center) {
                        drawLine(
                            color = minuteColor.copy(0.4f),
                            start = Offset(x = center.x, center.y - radius * 1f),
                            end = Offset(center.x, center.y - radius * 0.95f),
                            strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round
                        )
                    }
                }
            }
            drawHand(
                center = center,
                angle = hourAngle,
                color = hourColor,
                strokeWidth = 8.dp.toPx(),
                length = radius * 0.5f
            )
            drawHand(
                center = center,
                angle = minuteAngle,
                color = minuteColor,
                strokeWidth = 6.dp.toPx(),
                length = radius * 0.65f
            )
            drawHand(
                center = center,
                angle = secondAngle,
                color = secondColor,
                strokeWidth = 4.dp.toPx(),
                length = radius * 0.80f
            )
            drawCircle(color = secondColor, radius = 8.dp.toPx(), center = center)
        }
    }
}

private fun DrawScope.drawHand(
    center: Offset, angle: Float, length: Float, color: Color, strokeWidth: Float,
) {
    val angleRad = (angle - 90) * (PI / 180f).toFloat()
    val endX = center.x + length * cos(angleRad)
    val endY = center.y + length * sin(angleRad)
    drawLine(
        color = color,
        start = center,
        end = Offset(x = endX, y = endY),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
}