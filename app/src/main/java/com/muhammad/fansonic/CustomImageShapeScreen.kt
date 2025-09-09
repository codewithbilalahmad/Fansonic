package com.muhammad.fansonic

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CustomImageShapeScreen() {
    var healthPercentage by remember { mutableFloatStateOf(1f) }
    val animatedHealth by animateFloatAsState(
        targetValue = healthPercentage,
        label = "healthAnimation",
        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
    )
    val bullets = remember { mutableStateListOf<Bullet>() }
    fun fireBullet(){
       bullets.add(Bullet(x = 0f, y = 400f))
    }
    LaunchedEffect(Unit) {
        while(true){
            for(i in bullets.indices){
                bullets[i] = bullets[i].copy(x = bullets[i].x + 15f)
            }
            bullets.removeAll { it.x > 800f }
            delay(30)
        }
    }
    LaunchedEffect(bullets) {
        bullets.forEach { bullet ->
            if(bullet.x >500 && bullet.y in 300f..500f){
                healthPercentage = (healthPercentage - 0.1f).coerceAtLeast(0f)
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            PlaneHealth(animatedHealth)
        }

        bullets.forEach { bullet ->
            Box(
                modifier = Modifier
                    .offset(x = bullet.x.dp, y = bullet.y.dp)
                    .size(20.dp)
                    .background(Color.Yellow, CircleShape)
            )
        }

        Button(
            onClick = { fireBullet() },
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        ) {
            Text("Fire 🔫")
        }
    }
}

@Composable
fun PlaneHealth(healthPercentage: Float) {
    var lastHealth by remember { mutableFloatStateOf(healthPercentage) }
    var flashTrigger by remember { mutableStateOf(false) }
   LaunchedEffect(healthPercentage) {
       if (healthPercentage < lastHealth) {
           flashTrigger = true
       }
       lastHealth = healthPercentage
   }
    val flashAlpha by animateFloatAsState(
        targetValue = if (flashTrigger) 0.4f else 0f,
        animationSpec = tween(durationMillis = 250, easing = LinearEasing),
        finishedListener = { flashTrigger = false },
        label = "damageFlash"
    )

    val imageBitmap = ImageBitmap.imageResource(R.drawable.plane)
    Box(
        modifier = Modifier
            .size(240.dp)
            .drawWithContent {
                with(drawContext.canvas.nativeCanvas) {
                    val checkPoint = saveLayer(null, null)
                    drawContent()
                    drawImage(
                        image = imageBitmap, dstSize = IntSize(
                            width = 240.dp.toPx().toInt(),
                            height = 240.dp.toPx().toInt(),
                        ), blendMode = BlendMode.DstIn
                    )
                    restoreToCount(checkPoint)
                }
            }) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((240 * healthPercentage).dp)
                    .background(Color.Green)
                    .align(
                        Alignment.BottomCenter
                    )
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Red.copy(alpha = flashAlpha))
            )
        }
    }
}
data class Bullet(val x: Float, val y: Float)