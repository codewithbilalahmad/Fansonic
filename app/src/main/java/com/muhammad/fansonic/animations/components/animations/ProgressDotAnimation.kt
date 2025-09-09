package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ProgressDotAnimation(
    modifier: Modifier = Modifier,dotSpacing : Dp = 10.dp,
    dotCount: Int = 3, travelDistance: Dp = 15.dp,dotSize : Dp = 25.dp,
    durationMillis: Int = 2000,dotColor : Color = MaterialTheme.colorScheme.onBackground
) {
    val density = LocalDensity.current
    val dots = List(dotCount) {
        remember {
            Animatable(0f)
        }
    }
    dots.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f, animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        this.durationMillis = durationMillis
                        0f at 0 using LinearOutSlowInEasing
                        1f at 200 using LinearOutSlowInEasing
                        0f at 400 using LinearOutSlowInEasing
                        0f at 2000
                    }, repeatMode = RepeatMode.Restart
                )
            )
        }
    }
    val animations = dots.map { it.value }
    val travelDistance = with(density) {
        travelDistance.toPx()
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        animations.forEachIndexed { index, animation ->
            Box(modifier = Modifier
                .size(dotSize)
                .graphicsLayer {
                    translationY = -animation * travelDistance
                }
                .background(color = dotColor, shape = CircleShape))
            if(index != animations.size -1){
                Spacer(Modifier.width(dotSpacing))
            }
        }
    }
}

@Preview
@Composable
private fun ProgressAnimationPreview() {
    ProgressDotAnimation()
}