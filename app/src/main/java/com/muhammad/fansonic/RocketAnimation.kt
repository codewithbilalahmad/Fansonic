package com.muhammad.fansonic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun RocketAnimation() {
    val scope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val shake = remember { Animatable(0f) }
    var hasLaunched by remember { mutableStateOf(false) }
    fun launchRocket() {
        hasLaunched = true
        scope.launch {
            offsetY.animateTo(targetValue = 0f, animationSpec = tween(200))
            rotation.animateTo(targetValue = 0f, animationSpec = tween(200))
            shake.animateTo(targetValue = 0f, animationSpec = tween(200))
            offsetY.animateTo(40f, tween(150, easing = LinearEasing))
            offsetY.animateTo(0f, tween(100, easing = LinearEasing))
            launch {
                repeat(3) {
                    shake.animateTo(targetValue = 10f, animationSpec = tween(60))
                    shake.animateTo(targetValue = -10f, animationSpec = tween(60))
                }
                shake.animateTo(targetValue = 0f, animationSpec = tween(80))
            }
            launch {
                offsetY.animateTo(
                    targetValue = -450f,
                    animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
                )
                offsetY.animateTo(
                    targetValue = -2000f,
                    animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
                )
                offsetY.snapTo(0f)
                hasLaunched = false
            }
            launch {
                rotation.animateTo(
                    targetValue = 720f,
                    animationSpec = tween(durationMillis = 900, easing = LinearEasing)
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(16.dp), contentAlignment = Alignment.BottomCenter
    ) {
        if (hasLaunched) {
            Rocket(
                offsetY = offsetY.value,
                rotation = rotation.value,
                shake = shake.value
            )
        }
        Button(onClick = ::launchRocket, enabled = !hasLaunched) {
            Text(if(hasLaunched) "Launching.." else "Launch Rocket")
        }
    }
}

@Composable
private fun Rocket(offsetY: Float, rotation: Float, shake: Float) {
    Box(contentAlignment = Alignment.Center) {
        AnimatedVisibility(offsetY >0, enter = fadeIn(), exit = fadeOut()) {
            val transition = rememberInfiniteTransition()
            val flicker by transition.animateFloat(
                initialValue = 0.7f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(120),
                    repeatMode = RepeatMode.Reverse
                ), label = "flame"
            )
            val alpha = (1f - (offsetY  / -2000f).coerceIn(0f, 1f))
            Box(
                modifier =
                    Modifier
                        .size(width = 30.dp, height = 60.dp)
                        .offset(y = 8.dp)
                        .graphicsLayer {
                            this.alpha = alpha
                            scaleY =flicker
                        }
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(Color.Yellow, Color.Red, Color.Transparent)
                            ),
                            shape = RoundedCornerShape(50)
                        ))
        }
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_rocket),
            contentDescription = "Rocket",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer {
                    translationY = offsetY - 100f
                    rotationZ = rotation + shake
                })
    }
}