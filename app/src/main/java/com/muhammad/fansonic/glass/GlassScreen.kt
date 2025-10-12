package com.muhammad.fansonic.glass

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun GlassCardScreen() {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedGradient by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF141E30),
                        Color(0xFF243B55),
                        Color(0xFF141E30)
                    ),
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(
                        x = 1000f * sin(animatedGradient * Math.PI.toFloat().absoluteValue),
                        y = 1000f * cos(animatedGradient * Math.PI.toFloat().absoluteValue)
                    )
                )
            ), contentAlignment = Alignment.Center
    ){
        AnimatedParticles(particleCount = 50)
        GlassCard(width = 340.dp, height = 140.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "Upcoming Events",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    "• Team Meeting - 10:00 AM",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
                Text(
                    "• Client Call - 2:00 PM",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
                Text(
                    "• Review - 5:30 PM",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun GlassCard(
    title: String? = null,
    value: String? = null,
    width: Dp,
    height: Dp,
    content: @Composable (() -> Unit)? = null,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = FastOutSlowInEasing
            ), repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .graphicsLayer {
                translationY = offset
            }) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .liquidGlassEffect(
                    shape = RoundedCornerShape(30.dp),
                    borderColor = Color.White.copy(alpha = 0.4f),
                    intensity = 0.9f
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (content != null) {
                content()
            } else {
                if (title != null) {
                    Text(title, color = Color.White.copy(alpha = 0.9f), fontSize = 16.sp)
                }
                if (value != null) {
                    Text(
                        value,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun Modifier.liquidGlassEffect(
    shape: Shape,
    borderColor: Color,
    intensity: Float = 0.7f,
    blurRadius: Dp = 0.dp,
): Modifier {
    return this
        .clip(shape)
        .background(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.15f * intensity),
                    Color.White.copy(alpha = 0.05f * intensity),
                    Color.Transparent
                ), center = Offset(x = 0.3f, y = 0.3f), radius = 500f
            )
        )
        .drawBehind {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f * intensity),
                        Color.Transparent
                    ), center = Offset(x = 0.3f, y = 0.3f), radius = size.width * 0.8f
                ),
                radius = size.width * 0.8f,
                center = Offset(x = size.width * 0.3f, y = size.height * 0.3f)
            )
        }
        .border(BorderStroke(width = 1.dp, color = borderColor), shape = shape)
        .then(if (blurRadius > 0.dp) Modifier.blur(blurRadius) else Modifier)
}

@Composable
fun AnimatedParticles(particleCount: Int) {
    val particles = remember {
        List(particleCount) { Particle() }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            var position by remember { mutableStateOf(particle.reset()) }
            LaunchedEffect(particle) {
                while (true) {
                    position = particle.update()
                    delay(16L)
                }
            }
            Box(
                modifier = Modifier
                    .offset(x = position.x.dp, y = position.y.dp)
                    .size(particle.size.dp)
                    .graphicsLayer {
                        alpha = particle.opacity
                        rotationZ = particle.rotation
                    }
                    .background(
                        color = Color.White.copy(alpha = particle.opacity * 0.3f),
                        shape = CircleShape
                    )
            )
        }
    }
}

data class Particle(
    var x: Float = 0f,
    var y: Float = 0f,
    var size: Float = 0f,
    var speed: Float = 0f,
    var direction: Float = 0f,
    var opacity: Float = 0f,
    var rotation: Float = 0f,
    var rotationSpeed: Float = 0f,
) {
    fun reset(): Offset {
        x = (0..1000).random().toFloat()
        y = (0..2000).random().toFloat()
        size = (2..8).random().toFloat()
        speed = Random.nextFloat() * 1.3f + 0.2f
        direction = (0..360).random().toFloat()
        opacity = Random.nextFloat() * 1.3f + 0.1f
        rotation = (0..360).random().toFloat()
        rotationSpeed = Random.nextFloat() * (2f - (-2f)) + (-2f)
        return Offset(x, y)
    }

    fun update(): Offset {
        x += cos(Math.toRadians(direction.toDouble())).toFloat() * speed
        y += sin(Math.toRadians(direction.toDouble())).toFloat() * speed
        rotation += rotationSpeed
        if (x < -100f || x > 100f || y < -100f || y > 100f) {
            reset()
        }
        return Offset(x = x, y = y)
    }
}