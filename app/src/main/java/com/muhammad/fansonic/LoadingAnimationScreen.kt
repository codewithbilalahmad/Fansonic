package com.muhammad.fansonic

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
@Preview
fun LoadingAnimationScreen() {
    val scope = rememberCoroutineScope()
    val infiniteTransition = rememberInfiniteTransition()
    val yOffset = infiniteTransition.animateFloat(initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
        animation = tween(1000, easing = LinearEasing)
    ))
    val progress = remember { Animatable(0f) }
    val forwardAnimationSpec = remember {
        tween<Float>(
            durationMillis = 10_000, easing = LinearEasing
        )
    }
    val resetAnimationSpec = remember {
        tween<Float>(
            durationMillis = 1_000, easing = EaseInSine
        )
    }

    fun reset() {
        scope.launch {
            progress.stop()
            progress.animateTo(0f, resetAnimationSpec)
        }
    }

    fun togglePlay() {
        scope.launch {
            if (progress.isRunning) {
                progress.stop()
            } else {
                if (progress.value == 1f) {
                    progress.snapTo(0f)
                }
                progress.animateTo(1f, forwardAnimationSpec)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            Text(
                text = "Loading\nPlease\nWait.",
                modifier = Modifier
                    .align(Alignment.Center)
                    .loadingRevealAnimation(progress = progress.asState(), yOffset = yOffset),
                fontSize = 90.sp,
                lineHeight = 90.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(24.dp)
                    .safeContentPadding()
                    .align(
                        Alignment.BottomCenter
                    )
            ) {
                FilledIconButton(onClick = ::reset) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_skip_back),
                        contentDescription = null
                    )
                }
                Button(onClick = ::togglePlay) {
                    AnimatedContent(
                        label = "playbackButton",
                        targetState = progress.isRunning
                    ) { isPlaying ->
                        val icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                        Icon(
                            imageVector = ImageVector.vectorResource(icon),
                            contentDescription = null
                        )
                    }
                    Text(text = if (progress.isRunning) "Pause" else "Play")
                }
            }
        }
    }
}

private fun Modifier.loadingRevealAnimation(
    progress: State<Float>,
    yOffset: State<Float>,
    wavesCount: Int = 2,
    amplitudeProvider: (totalSize: Size) -> Float = { size -> size.minDimension * 0.1f },
): Modifier = this
    .graphicsLayer {
        compositingStrategy = CompositingStrategy.Offscreen
    }
    .drawWithCache {
        val height = size.height
        val waveLength = height / wavesCount
        val nextPointOffset = waveLength / 2f
        val controlPointOffset = nextPointOffset / 2f
        val amplitude = amplitudeProvider(size)
        val wavePath = Path()
        onDrawWithContent {
            drawContent()
            val waveStartX = (size.width + 2 * amplitude) * progress.value - amplitude
            wavePath.reset()
            wavePath.relativeLineTo(waveStartX, -waveLength)
            wavePath.relativeLineTo(0f, waveLength * yOffset.value)
            repeat((wavesCount + 1) * 2) { i ->
                val direction = if (i and 1 == 0) -1 else 1
                wavePath.relativeQuadraticTo(dx1 = direction * amplitude, dy1 = controlPointOffset, dx2 = 0f, dy2 = nextPointOffset)
            }
            wavePath.lineTo(0f, height)
            wavePath.close()
            drawPath(path = wavePath, brush = Brush.linearGradient(colorStops = arrayOf(
                0.0f to Color(0xFFE91E63),
                0.4f to Color(0xFF9C27B0),
                0.7f to Color(0xFFFFCC80),
                1f to Color.Yellow,
            )), blendMode = BlendMode.SrcAtop)
        }
    }