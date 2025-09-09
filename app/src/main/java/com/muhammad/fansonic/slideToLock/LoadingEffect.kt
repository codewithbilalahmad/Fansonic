package com.muhammad.fansonic.slideToLock

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

@Composable
fun LoadingText(
    text: String, modifier: Modifier, fontSize: TextUnit,
    colors: List<Color> = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(0.3f),
        MaterialTheme.colorScheme.surfaceVariant.copy(0.6f)
    ),
) {
    val transition = rememberInfiniteTransition("loadingEffect")
    val loading by transition.animateFloat(
        initialValue = -200f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(x = loading, y = 0f),
        end = Offset(x = loading + 200f, y = 0f)
    )
    Text(text = text, modifier = modifier, style = TextStyle(fontSize = fontSize, brush = brush))
}