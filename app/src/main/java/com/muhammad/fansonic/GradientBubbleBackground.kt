package com.muhammad.fansonic

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.util.lerp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import kotlin.random.Random

@Composable
fun GradientBubbleBackground(modifier: Modifier = Modifier, bubbleCount: Int = 50) {
    val infiniteTransition = rememberInfiniteTransition()
    val animationSpec: InfiniteRepeatableSpec<Color> = infiniteRepeatable(
        animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
        repeatMode = RepeatMode.Reverse
    )
    val color1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF00BCD4),
        targetValue = Color(0xFFE91E63),
        animationSpec = animationSpec,
        label = "color1"
    )
    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF8BC34A),
        targetValue = Color(0xFFFFEB3B),
        animationSpec = animationSpec,
        label = "color2"
    )
    val color3 by infiniteTransition.animateColor(
        initialValue = Color(0xFF3F51B5),
        targetValue = Color(0xFFFFC1E3),
        animationSpec = animationSpec,
        label = "color3"
    )
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 10000,
                easing = LinearEasing
            ), repeatMode = RepeatMode.Reverse
        ), label = "progress"
    )
    val bubbleColors = listOf(
        Color(0xFFFF80AB),
        Color(0xFFEA80FC),
        Color(0xFF82B1FF),
        Color(0xFF80D8FF),
        Color(0xFFA7FFEB),
        Color(0xFFFFF59D),
    )
    val bubbles = remember {
        List(bubbleCount) {
            Bubble(
                offsetStart = Offset(Random.nextFloat(), Random.nextFloat()),
                offsetEnd = Offset(Random.nextFloat(), Random.nextFloat()),
                radiusStart = Random.nextFloat() * 40,
                radiusEnd = Random.nextFloat() * 90, color = bubbleColors.random(),
                alpha = Random.nextFloat().coerceIn(0.3f, 0.8f)
            )
        }
    }
    Canvas(modifier = modifier) {
        val gradient = Brush.verticalGradient(
            colors = listOf(color1, color2, color3),
            startY = 0f,
            endY = size.height.toDp().toPx(),
            tileMode = TileMode.Mirror
        )
        drawRect(brush = gradient)
        for (bubble in bubbles) {
            val offset = androidx.compose.ui.geometry.lerp(bubble.offsetStart, bubble.offsetEnd, progress)
            val radius = lerp(bubble.radiusStart, bubble.radiusEnd, progress)
            val scale = size * 1.2f
            drawCircle(
                color = bubble.color, radius = radius * density, center = Offset(
                    x = offset.x * scale.width,
                    y = offset.y * scale.height
                ), alpha = bubble.alpha
            )
        }
    }
}

@Immutable
data class Bubble(
    val offsetStart: Offset,
    val offsetEnd: Offset,
    val radiusStart: Float,
    val radiusEnd: Float,
    val alpha: Float,val color : Color
)