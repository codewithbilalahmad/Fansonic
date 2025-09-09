package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ClockAnimation(
    modifier: Modifier = Modifier,
    borderWidth: Dp = 2.dp,
    lineStroke: Dp = 6.dp,useCorrectTime : Boolean = false,
    minuteLineColor: Color = MaterialTheme.colorScheme.primary,
    hourLineColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    borderColor: Color = MaterialTheme.colorScheme.onBackground,
    containerColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val minuteRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if(useCorrectTime) 60_000 else 1500, easing = LinearEasing)
        )
    )
    val hourRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(if(useCorrectTime) 43_200_000 else 10_000, easing = LinearEasing)
        )
    )
    Canvas(
        modifier = modifier
            .border(
                width = borderWidth,
                color = borderColor,
                shape = CircleShape
            )
            .background(color = containerColor, shape = CircleShape)
    ) {
        val radius = size.minDimension / 2
        val center = Offset(x = radius, y = radius)
        withTransform({
            rotate(minuteRotation, center)
        }) {
            drawLine(
                strokeWidth = lineStroke.toPx(),
                start = center,
                end = center.copy(y = center.y - radius + 20.dp.toPx()),
                cap = StrokeCap.Round,
                color = minuteLineColor
            )
        }
        withTransform({
            rotate(hourRotation, center)
        }) {
            drawLine(
                color = hourLineColor,
                start = center,
                end = center.copy(y = center.y - radius + 40.dp.toPx()),
                cap = StrokeCap.Round,
                strokeWidth = lineStroke.toPx()
            )
        }
    }
}

@Preview
@Composable
private fun ClockAnimationPreview() {
    ClockAnimation(modifier = Modifier.size(200.dp))
}