package com.muhammad.fansonic.animations.components.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.animatedBorder(
    durationMillis: Int = 4000,
    shape: Shape,
    colors: List<Color>,
    borderWidth: Dp,
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "animatedBorder")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            )
        ),
        label = "linearOffset"
    )

    return this
        .clipToBounds()
        .drawWithContent {
            val strokeWidth = borderWidth.toPx()
            val outline = shape.createOutline(size = size, layoutDirection = layoutDirection, density = this)
            val start = Offset(x = size.width * animatedOffset, y = 0f)
            val end = Offset(x = size.width * (animatedOffset - 1f), y = size.height)
            val brush = Brush.linearGradient(colors = colors, start = start, end = end)
            drawOutline(outline = outline, brush = brush, style = Stroke(strokeWidth))
            drawContent()
        }
}

@Preview
@Composable
private fun AnimatedBorderModifierPreview() {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .animatedBorder(
                shape = RoundedCornerShape(16.dp),
                colors = listOf(Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta),
                borderWidth = 4.dp
            )
    )
}
