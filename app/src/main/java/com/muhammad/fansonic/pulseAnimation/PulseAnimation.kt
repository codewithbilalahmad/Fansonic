package com.muhammad.fansonic.pulseAnimation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PulseAnimationScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        PulseAnimation()
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PulseAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 800
                1f at 0 using LinearEasing
                1.2f at 400 using LinearEasing
                1f at 800 using LinearEasing
            }, repeatMode = RepeatMode.Restart
        )
    )
    Box(modifier = modifier
        .size(100.dp)
        .scale(pulse)
        .background(
            shape = MaterialShapes.Heart.toShape(), color = Color(0xFFEF002F)
        ))
}