package com.muhammad.fansonic.raised_button

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RaisedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    elevation: Dp = 10.dp,
    height: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    colorDark: Color = MaterialTheme.colorScheme.primaryFixedDim,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    content: @Composable () -> Unit,
) {
    var isPressed by remember { mutableStateOf(false) }
    val depth by animateDpAsState(
        targetValue = if (isPressed) elevation / 3 else elevation,
        animationSpec = tween(durationMillis = 150),
        label = "depth",
        finishedListener = { isPressed = false }
    )
    val offset by animateDpAsState(
        targetValue = if (isPressed) (elevation * 2 / 3) else 0.dp,
        animationSpec = tween(durationMillis = 150),
        label = "offset",
        finishedListener = { isPressed = false }
    )
    Box(modifier = modifier.height(height + elevation), contentAlignment = Alignment.TopCenter) {
        Box(
            modifier = Modifier
                .offset(y = offset)
                .widthIn(min = height)
                .height(height + depth)
                .background(color = colorDark, shape = shape)
        )
        ProvideContentColorTextStyle(
            contentColor = contentColor,
            textStyle = MaterialTheme.typography.labelLarge, content = {
                Row(
                    modifier = modifier
                        .offset(y = offset)
                        .widthIn(min = height)
                        .height(height)
                        .background(color = color, shape = shape)
                        .clickable{
                            onClick()
                            isPressed = true
                        },
                    horizontalArrangement = Arrangement.spacedBy(
                        12.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        content()
                    }
                )
            })
    }
}

@Composable
fun RaisedButtonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RaisedButton(onClick = {}, modifier = Modifier, shape = CircleShape, height = 56.dp) {
                Text(text = "Click")
            }
            RaisedButton(onClick = {}, modifier = Modifier.fillMaxWidth(), shape = CircleShape, height = 56.dp) {
                Text(text = "Click")
            }
        }
        Spacer(Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RaisedToggleButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(), height = 56.dp, content = {
                    Text(text = "Toggle")
                })
            RaisedToggleButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(), height = 56.dp, content = {
                    Text(text = "Toggle")
                })
        }
    }
}




@Composable
fun RaisedToggleButton(
    modifier: Modifier = Modifier,
    elevation: Dp = 10.dp,
    height: Dp = 48.dp,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    content: @Composable () -> Unit,
) {
    var isPressed by remember { mutableStateOf(false) }
    val depth by animateDpAsState(
        targetValue = if (isPressed) elevation / 3 else elevation,
        animationSpec = tween(durationMillis = 150),
        label = "depth"
    )
    val offset by animateDpAsState(
        targetValue = if (isPressed) (elevation * 2 / 3) else 0.dp,
        animationSpec = tween(durationMillis = 150),
        label = "offset"
    )
    Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
        Box(
            modifier = Modifier
                .offset(y = offset)
                .widthIn(min = height)
                .height(height + depth)
                .background(color = MaterialTheme.colorScheme.primaryFixedDim, shape = shape)
        )
        ProvideContentColorTextStyle(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            textStyle = MaterialTheme.typography.labelLarge, content = {
                Row(
                    modifier = modifier
                        .offset(y = offset)
                        .widthIn(min = height)
                        .height(height)
                        .background(color = MaterialTheme.colorScheme.primary, shape = shape)
                        .pointerInput(Unit) {
                            detectTapGestures(onPress = {
                                isPressed = true
                                if (tryAwaitRelease()) {
                                    isPressed = false
                                }
                            })
                        },
                    horizontalArrangement = Arrangement.spacedBy(
                        12.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        content()
                    }
                )
            }
        )
    }
}

@Composable
fun ProvideContentColorTextStyle(
    contentColor: Color,
    content: @Composable () -> Unit,
    textStyle: TextStyle,
) {
    val mergedStyle = LocalTextStyle.current.merge(textStyle)
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides mergedStyle,
        content = content
    )
}