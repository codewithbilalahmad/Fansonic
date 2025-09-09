package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RotationCircle(
    modifier: Modifier = Modifier,
    durationMillis: Int = 2000,
    circleColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    val transition = rememberInfiniteTransition()
    val rotation by transition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearOutSlowInEasing)
        )
    )
    Box(modifier = modifier
        .size(100.dp)
        .graphicsLayer {
            this.rotationX = rotation
            this.rotationY = rotationY
        }
        .background(color = circleColor, shape = CircleShape))
}

@Preview
@Composable
private fun RotationCirclePreview() {
    RotationCircle(modifier = Modifier.size(120.dp))
}