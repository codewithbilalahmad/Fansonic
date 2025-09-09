package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleScaleAnimation(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1500,
    circleSize : Dp = 15.dp,
    containerColor: Color = MaterialTheme.colorScheme.onBackground,
    circleColor: Color = MaterialTheme.colorScheme.primary,
) {
    val transition = rememberInfiniteTransition()
    val scale by transition.animateFloat(
        initialValue = 1f, targetValue = 10f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Row(
        modifier = modifier.background(color = containerColor, shape = CircleShape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        repeat(2){
            Box(modifier = Modifier
                .size(circleSize)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .background(circleColor, CircleShape))
        }
    }
}

@Preview
@Composable
private fun CircleScaleAnimationPreview() {
    CircleScaleAnimation(modifier = Modifier.size(150.dp))
}