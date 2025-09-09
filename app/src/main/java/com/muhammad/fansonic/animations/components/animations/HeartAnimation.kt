package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R

@Composable
fun HeartAnimation(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1000,
    heartSize : Dp,
    lineWidth : Dp,
    lineHeight : Dp = 10.dp,
    travelDistance : Dp = 30.dp,
    color: Color = MaterialTheme.colorScheme.error
) {
    val density = LocalDensity.current
    val transition = rememberInfiniteTransition()
    val offset by transition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val travelDistance = with(density){
        travelDistance.toPx()
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_heart),
            contentDescription = "heart",tint = color, modifier = Modifier
                .size(heartSize)
                .graphicsLayer {
                    translationY = offset * travelDistance
                }
        )
        Box(modifier = Modifier
            .size(width = lineWidth, height = lineHeight)
            .graphicsLayer {
                scaleX = 0.5f + offset / 2
                alpha = 0.3f + offset / 2
            }
            .background(color = color, shape = CircleShape))
    }
}

@Preview
@Composable
private fun HeartAnimationPreview() {
    HeartAnimation(modifier = Modifier.size(150.dp), heartSize = 60.dp, lineWidth = 40.dp)
}