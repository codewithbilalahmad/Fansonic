package com.muhammad.fansonic.glitchEffect

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GlitchEffectScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        GlitchEffect()
    }
}

@Composable
fun GlitchEffect() {
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "GLITCH EFFECT",
            style = TextStyle(color = Color.Red, fontSize = 24.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.offset(
                x = (-2 + offset * 4).dp,
                y = (-2 + offset * 4).dp
            )
        )
        Text(
            text = "GLITCH EFFECT",
            style = TextStyle(color = Color.Red, fontSize = 24.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.offset(
                x = (2 - offset * 4).dp,
                y = (2 - offset * 4).dp
            )
        )
        Text(
            text = "GLITCH EFFECT",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}