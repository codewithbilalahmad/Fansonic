package com.muhammad.fansonic.step_motion

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit


@Composable
fun StepIndictor(
    stepNumber : Int,
    isPrevious : Boolean,
    isCurrent: Boolean,
    isNext : Boolean,
    activeColor : Color,
    inactiveColor: Color,
    circleSize : Dp,
    circleFontSize : TextUnit,
    borderWidth : Float,
    animationDuration: Int
) {
//    val containerColor by animateColorAsState(
//        targetValue =
//    )
}


@Composable
private fun StepLabel(
    title : String,
    isActive : Boolean,
    activeColor : Color,
    inactiveColor: Color,
    fontSize : TextUnit,
    animationDuration : Int
) {
    val titleColor by animateColorAsState(
        targetValue = if(isActive) activeColor else inactiveColor,
        animationSpec = tween(durationMillis = animationDuration),
        label = "titleColor"
    )
    Text(
        text = title,
        color = titleColor,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ConnectorLine(
    modifier: Modifier = Modifier,
    isPrevious: Boolean,
    isCurrent: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    thickness: Dp
) {
    val connectorFill by animateFloatAsState(
        targetValue = if(isCurrent || isPrevious) 1f else 0f,
        animationSpec = StepperDefaults.smoothSpring(),
        label = "connectorFill"
    )
    Canvas(modifier = modifier.height(thickness)) {
        val lineY = size.height / 2f
        drawLine(
            color = inactiveColor,
            start = Offset(x = 0f, y = lineY),
            end = Offset(x = size.width, y = lineY),
            strokeWidth = thickness.toPx(),
            cap = StrokeCap.Round
        )
        if(connectorFill > 0f){
            drawLine(
                color = activeColor,
                start = Offset(x = 0f, y = lineY),
                end = Offset(x = size.width * connectorFill, y = lineY),
                strokeWidth = thickness.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}