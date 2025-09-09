package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ArcRotationAnimation(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1000,
    arcStrokeWidth: Float = 10f,
    circleColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    arcColor: Color = MaterialTheme.colorScheme.error,
) {
    val transition = rememberInfiniteTransition()
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val circleRadius by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.40f, animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                delayMillis = 100,
                easing = FastOutLinearInEasing
            ), repeatMode = RepeatMode.Reverse
        )
    )
    Canvas(modifier = modifier) {
        val radius = size.minDimension /2f
        drawArc(
            color = arcColor,
            startAngle = rotation,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = arcStrokeWidth, cap = StrokeCap.Round)
        )
        drawArc(
            color = arcColor,
            startAngle = rotation + 180f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = arcStrokeWidth, cap = StrokeCap.Round)
        )
        drawCircle(color = arcColor.copy(alpha = 0.2f), radius = radius /2.2f, center = center)
        drawCircle(color = circleColor, radius = radius * circleRadius, center = center)
    }
}

@Preview
@Composable
private fun ArcRotationAnimationPreview() {
    ArcRotationAnimation(modifier = Modifier.size(150.dp))
}