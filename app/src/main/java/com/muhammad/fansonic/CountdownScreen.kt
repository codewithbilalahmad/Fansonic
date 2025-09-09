package com.muhammad.fansonic

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun CountdownScreen() {
    val durationMillis = 30000
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CountdownTimer(durationMillis = durationMillis)
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CountdownTimer(
    modifier: Modifier = Modifier,
    durationMillis: Int,
    strokeWidth: Dp = 8.dp,
    radius: Dp = 120.dp,
    color: Color = Color.Black, onFinish: () -> Unit = {},
    backgroundColor: Color = Color.LightGray,
) {
    var startCountDown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        startCountDown = true
    }
    val progress by animateFloatAsState(
        targetValue = if (startCountDown) 1f else 0f, finishedListener = {
            onFinish()
        },
        animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing),
        label = "progress"
    )
    val secondsLeft = ((1 - progress) * durationMillis / 1000).roundToInt()
    val seconds = secondsLeft % 60
    val minutes = secondsLeft / 60
    val timer = String.format("%02d:%02d",minutes, seconds)
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radiusPx = radius.toPx()
            val strokeWidth = strokeWidth.toPx()
            drawCircle(
                color = backgroundColor, radius = radiusPx, style = Stroke(width = strokeWidth)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false, size = Size(
                    width = radiusPx * 2,
                    height = radiusPx * 2
                ), topLeft = Offset(
                    x = (size.width - radiusPx * 2) / 2,
                    y = (size.height - radiusPx * 2) / 2
                ),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Text(
            text = timer,
            modifier = Modifier.padding(8.dp),
            style = TextStyle(color = Color.Black, fontSize = 72.sp, textAlign = TextAlign.Center)
        )
    }
}