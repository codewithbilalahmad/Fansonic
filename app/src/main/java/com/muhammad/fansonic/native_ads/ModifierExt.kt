package com.muhammad.fansonic.native_ads

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.loadingEffect(
    loadingColors : List<Color> = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(0.3f),
        MaterialTheme.colorScheme.surfaceVariant.copy(0.6f)
    )
) : Modifier{
    val infiniteTransition = rememberInfiniteTransition("loadingEffect")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = -200f, targetValue = 800f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "loadingEffect"
    )
    return drawBehind {
        val brush = Brush.linearGradient(
            colors = loadingColors,
            start = Offset(x = offsetX, y = 0f),
            end = Offset(offsetX + size.width, size.height)
        )
        drawRoundRect(brush = brush)
    }
}