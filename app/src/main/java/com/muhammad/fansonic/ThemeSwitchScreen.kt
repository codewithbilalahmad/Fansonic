@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.muhammad.fansonic

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.muhammad.fansonic.ui.theme.FansonicTheme

val switchWidth = 160.dp
val switchHeight = 70.dp

@Composable
fun ThemeSwitchScreen() {
    var isDarkTheme by remember { mutableStateOf(false) }
    FansonicTheme(darkTheme = isDarkTheme) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            ThemeSwitch(isDarkTheme = isDarkTheme) { darkTheme ->
                isDarkTheme = darkTheme
            }
        }
    }
}

@Composable
fun ThemeSwitch(isDarkTheme: Boolean, onCheckChange: (Boolean) -> Unit) {
    val knobSize = 60.dp
    val knobPadding = 6.dp
    val starsCount = 20
    val offset by animateDpAsState(
        targetValue = if (isDarkTheme) switchWidth - knobSize - 4.dp else 4.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow), label = "offset"
    )
    val offsetFraction by animateFloatAsState(
        targetValue = if (isDarkTheme) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow), label = "offsetFraction"
    )
    val rotation by animateFloatAsState(
        targetValue = if (isDarkTheme) 360f else 0f, animationSpec = tween(durationMillis = 500)
    )
    val backgroundStart by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF000022) else Color(0xFF4478a9),
        animationSpec = tween(durationMillis = 1000)
    )
    val backgroundEnd by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF000033) else Color(0xFFB0E2FF),
        animationSpec = tween(durationMillis = 1000)
    )
    Box(
        modifier = Modifier
            .size(width = switchWidth, height = switchHeight)
            .clip(CircleShape)
            .border(width = 4.dp, color = Color(0x40000000), shape = CircleShape)
            .background(
                androidx.compose.ui.graphics.lerp(
                    backgroundStart,
                    backgroundEnd,
                    offsetFraction
                )
            )
            .toggleable(
                value = isDarkTheme,
                onValueChange = onCheckChange,
                role = Role.Switch,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .semantics {
                stateDescription = if (isDarkTheme) "Dark Mode" else "Light Mode"
            }, contentAlignment = Alignment.CenterStart
    ) {
        if (isDarkTheme) {
            val stars = remember {
                List(starsCount) {
                    Star(
                        x = (0..100).random().toFloat(),
                        y = (0..100).random().toFloat(), radius = (1..3).random().toFloat()
                    )
                }
            }
            val transition = rememberInfiniteTransition(label = "twinkle")
            val alphaAnim by transition.animateFloat(
                initialValue = 0.4f, targetValue = 1f, animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "twinkleAlpha"
            )
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                val padding = 10f
                val width = size.width - padding * 2
                val height = size.height - padding * 2
                for (star in stars) {
                    drawCircle(
                        color = Color.White.copy(alphaAnim),
                        center = Offset(
                            x = star.x / 100 * width,
                            y = star.y / 100 * height,
                        ), radius = star.radius
                    )
                }
            }
        }
        Image(
            painter = painterResource(R.drawable.glow),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(switchWidth)
                .graphicsLayer {
                    scaleX = 1.2f
                    scaleY = 1.2f
                    translationX = lerp(
                        -switchWidth.toPx() / 2f + knobSize.toPx() / 2f,
                        switchWidth.toPx() / 2f - knobSize.toPx() / 2f,
                        offsetFraction
                    )
                })
        Icon(
            imageVector = ImageVector.vectorResource(if (isDarkTheme) R.drawable.moon else R.drawable.sun),
            contentDescription = if (isDarkTheme) "Switch Light" else "Switch Dark",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(knobSize)
                .padding(knobPadding)
                .offset(x = offset)
                .rotate(rotation)
        )
    }
}

data class Star(val x: Float, val y: Float, val radius: Float)