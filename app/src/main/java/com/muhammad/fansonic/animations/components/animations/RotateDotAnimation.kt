package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RotateDotAnimation(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1500,
    dotColor: Color = MaterialTheme.colorScheme.onBackground,
    dotRadius: Float = 20f,
    showOrbit: Boolean = true,
) {
    val transition = rememberInfiniteTransition()
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing)
        )
    )
    Canvas(modifier = modifier) {
        val orbitRadius = (size.minDimension / 2f) - dotRadius
        val angle = Math.toRadians(rotation.toDouble())
        val x = (center.x + cos(angle) * orbitRadius).toFloat()
        val y = (center.y + sin(angle) * orbitRadius).toFloat()
        if (showOrbit) {
            drawCircle(
                color = dotColor.copy(alpha = 0.6f),
                radius = orbitRadius,
                style = Stroke(width = 10f)
            )
        }
        drawCircle(color = dotColor, center = Offset(x = x, y = y), radius = dotRadius)
    }
}

@Preview
@Composable
private fun RotateDotAnimationPreview() {
    RotateDotAnimation(modifier = Modifier.size(200.dp))
}