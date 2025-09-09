package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PacmanAnimation(
    modifier: Modifier = Modifier,
    durationMillis: Int = 800,
    eyeRadius: Float = 10f,
    arcColor: Color = MaterialTheme.colorScheme.onBackground,
    eyeColor: Color = MaterialTheme.colorScheme.error,
) {
    val transition = rememberInfiniteTransition()
    val mouthSweep by transition.animateFloat(
        initialValue = 360f, targetValue = 280f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val mouthOffset by transition.animateFloat(
        initialValue = 0f, targetValue = 40f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Canvas(modifier = modifier) {
        val pacmanRadius = size.minDimension / 2f
        drawArc(
            color = arcColor,
            startAngle = mouthOffset,
            sweepAngle = mouthSweep,
            useCenter = true,
            size = Size(width = pacmanRadius * 2, height = pacmanRadius * 2),
            topLeft = Offset(x = center.x - pacmanRadius, y = center.y - pacmanRadius)
        )
        drawCircle(
            color = eyeColor,
            radius = eyeRadius,
            center = Offset(x = center.x + pacmanRadius / 3f, y = center.y - pacmanRadius / 1.5f)
        )
    }
}

@Preview
@Composable
private fun PacmanAnimationPreview() {
    PacmanAnimation(modifier = Modifier.size(150.dp))
}