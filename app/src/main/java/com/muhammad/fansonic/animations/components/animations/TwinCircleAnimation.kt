package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.animations.utils.animateValues

@Composable
fun TwinCircleAnimation(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1500,
    circleColor: Color = MaterialTheme.colorScheme.onBackground,
    color1: Color = MaterialTheme.colorScheme.primary,
    color2: Color = MaterialTheme.colorScheme.primary,
) {
    val transition = rememberInfiniteTransition()
    val color1 by transition.animateColor(
        initialValue = color1, targetValue = color2, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val color2 by transition.animateColor(
        initialValue = color2, targetValue = color1, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val offsetX by animateValues(
        values = listOf(0f, 100f, -100f, 0f), animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val scale by animateValues(
        values = listOf(1f, 10f, 10f, 10f, 1f), animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Canvas(modifier = modifier) {
        val circleRadius = size.minDimension / 4f
        drawCircle(color = circleColor)
        drawCircle(
            color = color2,
            radius = circleRadius + scale * 2f,
            center = Offset(x = -offsetX + center.x, y = center.y)
        )
        drawCircle(
            color = color1,
            radius = circleRadius + scale * 2f,
            center = Offset(x = offsetX + center.x, y = center.y)
        )
    }
}

@Preview
@Composable
private fun TwinCircleAnimationPreview() {
    TwinCircleAnimation(modifier = Modifier.size(150.dp))
}