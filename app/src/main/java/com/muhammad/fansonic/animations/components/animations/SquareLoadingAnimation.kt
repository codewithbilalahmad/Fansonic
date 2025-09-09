package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SquareLoadingAnimation(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1000,
    squareColor: Color = MaterialTheme.colorScheme.onBackground,
    strokeWidth: Float = 12f,
) {
    val transition = rememberInfiniteTransition()
    val rotation by transition.animateFloat(
        initialValue = 0f, targetValue = 180f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis / 2, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val heightFraction by transition.animateFloat(
        initialValue = 1f, targetValue = 0f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Canvas(modifier = modifier) {
        val squareSize = size.minDimension * 0.8f
        val half = squareSize / 2
        val topLeft = Offset(x = center.x - half, y = center.y - half)
        withTransform({
            rotate(degrees = rotation, pivot = center)
        }) {
            drawRect(
                color = squareColor,
                topLeft = topLeft,
                size = Size(width = squareSize, height = squareSize),
                style = Stroke(width = strokeWidth)
            )
        }
        drawRect(
            color = squareColor,
            topLeft = topLeft,
            size = Size(width = squareSize, height = squareSize * heightFraction)
        )
    }
}

@Preview
@Composable
private fun SquareLoadingAnimationPreview() {
    SquareLoadingAnimation(modifier = Modifier.size(150.dp))
}