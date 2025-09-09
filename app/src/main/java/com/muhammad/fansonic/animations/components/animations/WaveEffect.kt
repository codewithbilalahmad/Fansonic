package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R
import kotlinx.coroutines.delay

@Composable
fun WaveEffect(
    modifier: Modifier = Modifier,
    waveCount: Int = 4,
    waveSize : Dp = 50.dp,
    durationMillis: Int = 4000,
    waveColor: Color = MaterialTheme.colorScheme.onBackground,
    content: @Composable () -> Unit,
) {
    val waves = List(waveCount) { remember { Animatable(0f) } }
    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(durationMillis = durationMillis, easing = FastOutLinearInEasing),
        repeatMode = RepeatMode.Restart
    )

    waves.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * (durationMillis / waveCount).toLong())
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = animationSpec
            )
        }
    }

    val progresses = waves.map { it.value }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        progresses.forEach { progress ->
            Box(
                modifier = Modifier
                    .size(waveSize)
                    .graphicsLayer {
                        scaleX = 1f + progress * 3f
                        scaleY = 1f + progress * 3f
                        alpha = 1f - progress
                    }
                    .background(waveColor, CircleShape)

            )
        }
        content()
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun WaveEffectPreview() {
    WaveEffect(modifier = Modifier.size(120.dp), content = {
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
                modifier = Modifier.size(32.dp)
            )
        }
    })
}