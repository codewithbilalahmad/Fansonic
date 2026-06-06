package com.muhammad.fansonic.like_animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.muhammad.fansonic.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

data class ClockParticle(
    val angle: Float
)

@Composable
fun YoutubeLikeAnimationScreen() {

    var showAnimation by remember {
        mutableStateOf(false)
    }

    var liked by remember {
        mutableStateOf(false)
    }

    val particles = remember {
        List(12) { index ->
            ClockParticle(
                angle = index * 30f
            )
        }
    }

    val smallHeartScale by animateFloatAsState(
        targetValue = if (liked) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = 0.4f,
            stiffness = 300f
        ),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {

                detectTapGestures(

                    onDoubleTap = {

                        liked = true
                        showAnimation = true
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {


        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_heart),
            contentDescription = null,
            tint = if (liked) Color.Red else Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset((-24).dp, (-24).dp)
                .size(36.dp)
                .scale(smallHeartScale)
        )
        if(showAnimation) {

            AnimatedHeartWithParticles(
                onAnimationFinished = {
                    showAnimation = false
                },
                particles = particles
            )
        }
    }
}

@Composable
fun AnimatedHeartWithParticles(
    particles: List<ClockParticle>,
    onAnimationFinished: () -> Unit
) {

    val heartScale = remember {
        Animatable(0f)
    }

    val heartAlpha = remember {
        Animatable(1f)
    }

    LaunchedEffect(Unit) {

        launch {

            heartScale.animateTo(
                targetValue = 1.4f,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )

            heartScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = LinearOutSlowInEasing
                )
            )
        }

        launch {

            delay(500)

            heartAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(300)
            )

            onAnimationFinished()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(180.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_heart),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .graphicsLayer{
                    scaleX = heartScale.value
                    scaleY = heartScale.value
                }
                .size(120.dp)
        )
        particles.forEach { particle ->
            ClockParticleItem(
                particle = particle
            )
        }
    }
}

@Composable
fun ClockParticleItem(
    particle: ClockParticle
) {
    val progress = remember {
        Animatable(0f)
    }
    LaunchedEffect(Unit) {
        launch  {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 700,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    val radius = 220f

    val radians = Math.toRadians(
        particle.angle.toDouble()
    )

    val targetX = cos(radians).toFloat() * radius
    val targetY = sin(radians).toFloat() * radius

    val currentX = targetX * progress.value
    val currentY = targetY * progress.value

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    currentX.toInt(),
                    currentY.toInt()
                )
            }
            .size(lerp(8.dp, 4.dp, progress.value))
            .background(
                color = Color.Red,
                shape = CircleShape
            )
    )
}