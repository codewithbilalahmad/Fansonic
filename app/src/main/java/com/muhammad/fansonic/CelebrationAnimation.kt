package com.muhammad.fansonic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.animations.components.animations.WaveEffect
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun CelebrationScreen() {
    var isCelebrating by remember { mutableStateOf(false) }
    var burstCenter by remember { mutableStateOf(Offset.Zero) }
    Box(modifier = Modifier.fillMaxSize().background(Color.Black).pointerInput(Unit){
        detectTapGestures { offset ->
            burstCenter = offset
            isCelebrating = true
        }
    }, contentAlignment = Alignment.Center){
            WaveEffect(modifier = Modifier.size(100.dp), content = {
                IconButton(
                    onClick = {},
                    modifier = Modifier.fillMaxSize(),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_next),
                        contentDescription = "Linkdlin",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Unspecified
                    )
                }
            })
//        Button(onClick = {
//            burstCenter = Offset(600f, 1200f)
//            isCelebrating = true
//        }) {
//            Text("Celebrate 🎉")
//        }
        CelebrationAnimation(isPlaying = isCelebrating, onFinished = {
            isCelebrating = false
        }, burstCenter = burstCenter)
    }
}

@Composable
fun CelebrationAnimation(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    burstCenter: Offset,
    onFinished: () -> Unit,
    durationMillis: Int = 4000,
    particleCount: Int = 120
) {
    val particles = remember { mutableStateListOf<Particle>() }
    var startTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            particles.clear()
            repeat(particleCount) {
                particles.add(Particle.create(burstCenter))
            }
            startTime = System.currentTimeMillis()

            // run frame-by-frame animation
            withFrameNanos { } // sync to first frame
            while (System.currentTimeMillis() - startTime < durationMillis) {
                withFrameNanos { frameTimeNanos ->
                    val now = frameTimeNanos / 1_000_000f
                    particles.forEach { it.update(now) }
                }
            }
            particles.clear()
            onFinished()
        }
    }

    if (particles.isNotEmpty()) {
        Canvas(modifier = modifier.fillMaxSize()) {
            particles.forEach { particle ->
                if (particle.alpha > 0f) {
                    drawCircle(
                        color = particle.color,
                        radius = particle.size,
                        center = Offset(x = particle.x, y = particle.y),
                        alpha = particle.alpha
                    )
                }
            }
        }
    }
}

data class Particle(
    var x: Float,
    var y: Float,
    val vx: Float,
    var vy: Float,
    val size: Float,
    val color: Color,
    val lifeTime: Long,
    val startTime: Float,
    var alpha: Float = 1f,
) {
    fun update(now: Float) {
        val age = now - startTime
        if (age > lifeTime) {
            alpha = 0f
        } else {
            x += vx
            y += vy
            vy += 0.15f // gravity
            alpha = 1f - (age / lifeTime.toFloat())
        }
    }

    companion object {
        fun create(center: Offset): Particle {
            val random = Random(System.currentTimeMillis())
            val angle = random.nextFloat() * (2 * PI.toFloat())
            val speed = random.nextFloat() * 10f + 4f
            return Particle(
                x = center.x,
                y = center.y,
                vx = cos(angle) * speed,
                vy = sin(angle) * speed,
                size = random.nextFloat() * 6f + 3f,
                color = Color(
                    red = random.nextFloat(),
                    green = random.nextFloat(),
                    blue = random.nextFloat(),
                    alpha = 1f
                ),
                lifeTime = (1200..2200).random().toLong(),
                startTime = System.currentTimeMillis().toFloat()
            )
        }
    }
}
