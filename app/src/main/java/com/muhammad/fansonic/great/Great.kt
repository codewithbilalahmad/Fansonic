package com.muhammad.fansonic.great

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R

@Composable
fun NeuMorphicUPScreen() {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale = remember { Animatable(1f) }
    val rotation = remember { Animatable(0f) }
    LaunchedEffect(isPressed) {
        if (isPressed) {
            rotation.snapTo(0f)
            scale.snapTo(1f)
            scale.animateTo(
                targetValue = 1.3f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            rotation.animateTo(
                targetValue = -30f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            rotation.animateTo(
                targetValue = 30f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            rotation.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        } else{
            rotation.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NeuMorphicUP(modifier = Modifier.size(300.dp), content = {
                val tint by animateColorAsState(
                    targetValue = if (isPressed) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surface,
                    animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                    label = "tint"
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_heart),
                    tint = tint,
                    contentDescription = null, modifier = Modifier.graphicsLayer{
                        scaleX = scale.value
                        scaleY = scale.value
                        rotationZ = rotation.value
                    }.size(100.dp)
                )
            }, interactionSource = interactionSource, isPressed = isPressed)
        }
    }
}

@Composable
fun NeuMorphicUP(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialShapes.Cookie12Sided.toShape(),
    bumpElevation: Dp = 8.dp,
    contentAlignment: Alignment = Alignment.Center,
    interactionSource: MutableInteractionSource, isPressed: Boolean,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier.neumorphic(
            shape = shape,
            shadowPadding = bumpElevation, onClick = {},
            isPressed = isPressed, interactionSource = interactionSource
        ),
        content = content,
        contentAlignment = contentAlignment
    )
}

@Composable
fun Modifier.neumorphic(
    shape: Shape,
    isPressed: Boolean,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
    shadowPadding: Dp,
    color: Color = MaterialTheme.colorScheme.background,
): Modifier {
    return this
        .then(
            if (isPressed) Modifier else Modifier
                .dropShadow(
                    shape = shape,
                    shadow = Shadow(
                        radius = 4.dp,
                        color = MaterialTheme.colorScheme.surface,
                        offset = DpOffset(x = 4.dp, 4.dp)
                    )
                )
                .dropShadow(
                    shape = shape,
                    shadow = Shadow(
                        radius = 4.dp,
                        color = Color(0xFFC5E1A5),
                        offset = DpOffset(x = -(4).dp, y = -(4).dp)
                    )
                )
        )
        .clip(shape)
        .background(color = color, shape = shape)
        .then(
            if (isPressed) {
                Modifier
                    .innerShadow(
                        shape = shape,
                        shadow = Shadow(
                            radius = shadowPadding * 2,
                            color = Color(0xFFC5E1A5),
                            offset = DpOffset(x = shadowPadding, y = shadowPadding)
                        )
                    )
                    .innerShadow(
                        shape = shape,
                        shadow = Shadow(
                            radius = shadowPadding * 2,
                            color = MaterialTheme.colorScheme.surface,
                            offset = DpOffset(x = -shadowPadding, y = -shadowPadding)
                        )
                    )
            } else Modifier
        )
        .clickable(onClick = onClick, interactionSource = interactionSource)
}