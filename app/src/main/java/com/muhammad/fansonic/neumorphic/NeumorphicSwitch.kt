package com.muhammad.fansonic.neumorphic

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp


@Composable
fun NeumorphicSwitchScreen() {
    var checked by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        NeumorphicSwitch(checked = checked, onCheckedChange = {isChecked ->
            checked = isChecked
        })
    }
}

@Composable
fun NeumorphicSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    val trackWidth = 60.dp
    val trackHeight = 32.dp
    val thumbSize = 24.dp
    val padding = 4.dp

    val maxOffset = trackWidth - thumbSize - padding * 2

    val offset by animateDpAsState(
        targetValue = if (checked) maxOffset else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val trackColor by animateColorAsState(
        targetValue = if (checked)
            MaterialTheme.colorScheme.primary
        else
            NeumorphicTheme.colorScheme.background
    )

    Box(
        modifier = modifier
            .size(trackWidth, trackHeight)
            .clip(CircleShape)
            .background(trackColor)
            .toggleable(checked, enabled) {
                onCheckedChange(!checked)
            }
            .innerShadow(
                shape = CircleShape,
                shadow = Shadow(
                    radius = 8.dp,
                    color = NeumorphicTheme.colorScheme.light.copy(0.5f),
                    offset = DpOffset(4.dp, -(4).dp)
                )
            )
            .innerShadow(
                shape = CircleShape,
                shadow = Shadow(
                    radius = 8.dp,
                    color = NeumorphicTheme.colorScheme.shadow.copy(0.3f),
                    offset = DpOffset(-(4).dp, 4.dp)
                )
            )
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset { IntOffset(offset.toPx().toInt(), 0) }
                .size(thumbSize)
                .clip(CircleShape)
                .background(NeumorphicTheme.colorScheme.background)
                .dropShadow(
                    shape = CircleShape,
                    shadow = Shadow(
                        radius = 6.dp,
                        color = NeumorphicTheme.colorScheme.darkShadow.copy(0.5f),
                        offset = DpOffset(2.dp, 2.dp)
                    )
                )
                .innerShadow(
                    shape = CircleShape,
                    shadow = Shadow(
                        radius = 2.dp,
                        color = NeumorphicTheme.colorScheme.light.copy(0.8f),
                        offset = DpOffset(-1.dp, -1.dp)
                    )
                )
        )
    }
}