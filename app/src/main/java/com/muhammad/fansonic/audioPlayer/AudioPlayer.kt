package com.muhammad.fansonic.audioPlayer

import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import com.muhammad.fansonic.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin



@Composable
fun AudioPlayerScreen() {
    val context = LocalContext.current
    val player = remember {
        MediaPlayer.create(context, R.raw.song)
    }
    var currentPosition by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    LaunchedEffect(player) {
        while (true) {
            delay(100)
            currentPosition = player.currentPosition
            isPlaying = player.isPlaying
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            player.stop()
            player.release()
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AudioPlayer(
            isPlaying = isPlaying,
            progress = { currentPosition.toFloat() / player.duration },
            onPlay = {
                player.start()
            }, onPause = {
                player.pause()
            })
    }
}

@Composable
fun AudioPlayer(
    isPlaying: Boolean,
    progress: () -> Float,
    modifier: Modifier = Modifier,
    onPlay: () -> Unit,
    onPause: () -> Unit,
) {
    val radius = 140f
    val stroke = 16f
    Box(
        modifier = modifier
            .size((radius * 2 + stroke * 2).dp)
            .padding(5.dp)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape,
                ambientColor = Color(0xFFA3B1C6).copy(alpha = 0.5f),
                spotColor = MaterialTheme.colorScheme.background.copy(0.8f)
            )
            .background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
        ) {
            val center = Offset(x = size.width / 2, y = size.height / 2)
            val radiusPx = min(size.width, size.height) / 2 - stroke
            drawCircle(
                color = Color(0xFFD7E2EA),
                radius = radiusPx,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                color = Color.Black,
                startAngle = -90f,
                sweepAngle = 360f * progress(),
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
                size = Size(radiusPx * 2, radiusPx * 2),
                topLeft = Offset(x = center.x - radiusPx, y = center.y - radiusPx)
            )
            val angleRad = Math.toRadians(progress() * 360.0 - 90)
            val thumbX = center.x + radiusPx * cos(angleRad).toFloat()
            val thumbY = center.y + radiusPx * sin(angleRad).toFloat()
            drawCircle(
                color = Color.Black,
                radius = stroke * 1.3f,
                center = Offset(thumbX, thumbY)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ControlButton(onClick = {}) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_skip_back),
                        contentDescription = "Backward",
                    )
                }
                ControlButton(onClick = if (isPlaying) onPause else onPlay, size = 90.dp) {
                    Icon(
                        imageVector = ImageVector.vectorResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                }
                ControlButton(onClick = {}) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                        contentDescription = "Forward"
                    )
                }
            }
        }
    }
}

@Composable
fun ControlButton(onClick: () -> Unit, size: Dp = 60.dp, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(size)
            .shadow(elevation = 6.dp, shape = CircleShape)
            .background(
                MaterialTheme.colorScheme.background, CircleShape
            )
            .clickable(onClick = onClick), content = {
            content()
        }, contentAlignment = Alignment.Center)
}