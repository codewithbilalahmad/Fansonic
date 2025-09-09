package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.core.FastOutLinearInEasing
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RotateTwoDotsAnimation(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1500,dotRadius : Float = 50f,
    dotColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val anim1 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.8f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val anim2 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = 360f, targetValue = 0f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Canvas(modifier = modifier) {
        val angle = Math.toRadians(rotation.toDouble())
        val x = (center.x + cos(angle) * 140f).toFloat()
        val y = (center.y + sin(angle) * 140f).toFloat()
        withTransform({
            scale(anim1)
        }) {
            drawCircle(color = dotColor, center = center, alpha = anim1, radius = dotRadius)
        }
        withTransform({
            scale(anim2)
        }) {
            drawCircle(color = dotColor, alpha = anim2, center = Offset(x = x, y = y), radius = dotRadius)
        }
    }
}

@Preview
@Composable
private fun RotateTwoDotsAnimationPreview() {
    RotateTwoDotsAnimation(modifier = Modifier.size(150.dp))
}