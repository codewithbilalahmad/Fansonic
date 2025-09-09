package com.muhammad.fansonic

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.ui.theme.FansonicTheme

val BlueSky = Color(0xFF4478a9)
val NightSky = Color(0xFF333333)
val BorderColor = Color(0x40000000)

@Composable
fun ThemeSwitchExpressiveScreen() {
    var isDarkTheme by remember { mutableStateOf(false) }
    FansonicTheme(darkTheme = isDarkTheme) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            ThemeSwitchExpressive(isDarkTheme = isDarkTheme) { darkTheme ->
                isDarkTheme = darkTheme
            }
        }
    }
}

@Composable
fun ThemeSwitchExpressive(isDarkTheme: Boolean, onCheckChange: (Boolean) -> Unit) {
    val switchWidth = 160.dp
    val switchHeight = 64.dp
    val handleSize = 52.dp
    val handlePadding = 10.dp
    val offsetFraction by animateFloatAsState(
        targetValue = if (isDarkTheme) 1f else 0f,
        label = "offset",
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )
    val rotation by animateFloatAsState(
        targetValue = if (isDarkTheme) 360f else 0f, animationSpec = tween(durationMillis = 500)
    )
    Box(
        modifier = Modifier
            .size(width = switchWidth, height = switchHeight)
            .clip(RoundedCornerShape(switchHeight))
            .background(
                lerp(
                    BlueSky,
                    NightSky,
                    offsetFraction
                )
            )
            .border(3.dp, BorderColor, RoundedCornerShape(switchHeight))
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
        val backgroundPainter = painterResource(R.drawable.background)
        Canvas(modifier = Modifier.fillMaxSize()) {
            with(backgroundPainter) {
                val scale = size.width / intrinsicSize.width
                val scaledHeight = intrinsicSize.height * scale
                translate(top = (size.height - scaledHeight) * (1f - offsetFraction)) {
                    draw(Size(size.width, scaledHeight))
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
                    scaleX = scaleX
                    scaleY = scaleY
                    translationX = androidx.compose.ui.util.lerp(
                        -switchWidth.toPx() / 2f + handleSize.toPx() / 2f,
                        switchWidth.toPx() / 2f - handleSize.toPx() / 2f,
                        offsetFraction
                    )
                }
        )
        Box(
            modifier = Modifier
                .padding(horizontal = handlePadding)
                .size(handleSize)
                .offset(
                    x = (switchWidth - handleSize - handlePadding * 2f) * offsetFraction
                )
                .rotate(rotation)
                .paint(painterResource(R.drawable.sun))
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.moon),
                contentDescription = if (isDarkTheme) "Switch Light" else "Switch Dark",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(handleSize)
                    .graphicsLayer {
                        translationX = size.width * (1f - offsetFraction)
                    }
            )
        }
    }
}