package com.muhammad.fansonic.slideToLock

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammad.fansonic.R
import kotlinx.coroutines.delay

@Composable
fun SlideToUnlockScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        SlideToUnlockVersion1()
        SlideToUnlockVersion2()
        SlideToUnlockVersion3()
        SlideToUnlockVersion4()
        SlideToUnlockVersion5()
    }
}

@Composable
fun SlideToUnlockVersion1() {
    var isSlided by remember { mutableStateOf(false) }
    SlideToUnlock(
        isSlided = isSlided,
        onSlideCompleted = {
            isSlided = true
        },
        hintText = HintText.defaultHintText().copy(
            defaultText = "Slide to play"
        ),
        colors = DefaultSlideToUnlockColors(slidedHintColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SlideToUnlockVersion2() {
    var isSlided by remember { mutableStateOf(false) }
    SlideToUnlock(
        isSlided = isSlided,
        modifier = Modifier.fillMaxWidth(),
        trackShape = RoundedCornerShape(10.dp),
        hintText = HintText(defaultText = "Slide to follow", slidedText = "Following.."),
        onSlideCompleted = {
            isSlided = true
        },
        colors = DefaultSlideToUnlockColors(
            endTrackColor = Color(0xFF416AEE),
            slidedHintColor = Color.White
        ),
        thumb = { slided, fraction, colors, size, orientation ->
            Box(
                modifier = Modifier
                    .size(size),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.linkdlin),
                    modifier = Modifier.size(40.dp),
                    contentDescription = "Slide to follow"
                )
            }
        }, hint = { slided, fraction, hintText, colors, padding, orientation ->
            val layoutDirection = LocalLayoutDirection.current
            AnimatedContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                targetState = slided
            ) { slideState ->
                if (slideState) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .size(30.dp), color = Color.White
                        )
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = hintText.slidedText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                textAlign = TextAlign.Center,
                                color = colors.slidedHintColor()
                            )
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = padding.calculateStartPadding(layoutDirection)),
                        text = hintText.defaultText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.Center,
                            color = colors.hintColor(fraction)
                        )
                    )
                }
            }
        }
    )
}

@Composable
fun SlideToUnlockVersion3() {
    val layoutDirection = LocalLayoutDirection.current
    var isSlided by remember { mutableStateOf(false) }
    val colorStops = arrayOf(
        0.0f to Color.Black,
        1f to Color(0xDC393636)
    )
    SlideToUnlock(
        isSlided = isSlided,
        modifier = Modifier.fillMaxWidth(),
        trackShape = RoundedCornerShape(12.dp),
        thumbSize = DpSize(width = 65.dp, height = 45.dp),
        colors = DefaultSlideToUnlockColors(
            slidedHintColor = Color.White,
            thumbColor = Color(0xDC9B9B9F),
            thumbIconColor = Color(0xDC49494B),
            trackBrush = Brush.verticalGradient(colorStops = colorStops)
        ),
        onSlideCompleted = {
            isSlided = true
        },
        thumb = { slided, fraction, colors, size, orientation ->
            val colorStops = arrayOf(
                0.0f to Color.White,
                1f to colors.thumbColor()
            )
            Box(
                modifier = Modifier
                    .size(size)
                    .background(
                        brush = Brush.verticalGradient(colorStops = colorStops),
                        shape = RoundedCornerShape(12.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_next),
                    contentDescription = "Slide to unlock", tint = colors.thumbIconColor()
                )
            }
        }, hint = { slided, fraction, hintText, colors, padding, orientation ->
            AnimatedVisibility(
                visible = !isSlided, enter = fadeIn(), exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                LoadingText(
                    modifier = Modifier.padding(
                        start = padding.calculateStartPadding(
                            layoutDirection
                        )
                    ),
                    text = hintText.defaultText,
                    fontSize = 21.sp
                )
            }
        })
}

@Composable
fun SlideToUnlockVersion4() {
    val layoutDirection = LocalLayoutDirection.current
    var isSlided by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (isCompleted) 1f else 0f,
        label = "alpha",
        animationSpec = tween(durationMillis = 700)
    )
    val colors = if (isCompleted) {
        DefaultSlideToUnlockColors(
            slidedHintColor = Color.White,
            thumbIconColor = Color(0xFF11D483),
            endTrackColor = lerp(Color(0xFFB4AFB4), Color(0xFF11D483), alpha)
        )
    } else {
        DefaultSlideToUnlockColors(
            endTrackColor = Color(0xFFB4AFB4), slidedHintColor = Color.White
        )
    }
    LaunchedEffect(isSlided) {
        if (isSlided) {
            delay(1500)
            isCompleted = true
        }
    }
    SlideToUnlock(
        isSlided = isSlided,
        onSlideCompleted = {
            isSlided = true
        },
        colors = colors,
        modifier = Modifier.fillMaxWidth(),
        hintText = HintText.defaultHintText().copy(
            defaultText = "Add to cart"
        ),
        thumb = { slided, fraction, colors, size, orientation ->
            Box(
                modifier = Modifier
                    .size(size)
                    .background(color = colors.thumbColor(), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCompleted -> {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_done),
                            contentDescription = "Cart added",
                            modifier = Modifier.size(30.dp),
                            tint = colors.thumbIconColor()
                        )
                    }

                    isSlided -> {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(8.dp),
                            color = colors.progressColor(),
                            strokeWidth = 3.dp
                        )
                    }

                    else -> {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_cart),
                            contentDescription = "Add to cart",
                            modifier = Modifier
                                .size(30.dp)
                                .rotate(fraction * -360),
                            tint = colors.thumbIconColor()
                        )
                    }
                }
            }
        },
        hint = { slided, fraction, hintText, colors, padding, orientation ->
            AnimatedContent(
                targetState = isSlided,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) { slided ->
                if (isCompleted) {
                    Text(
                        text = "Completed!",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.Center,
                            color = colors.slidedHintColor()
                        )
                    )
                } else if (!slided) {
                    Text(
                        text = hintText.defaultText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = padding.calculateStartPadding(layoutDirection)),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = colors.hintColor(
                                fraction
                            ), textAlign = TextAlign.Center
                        )
                    )
                } else {
                    Text(
                        text = hintText.slidedText,
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = colors.slidedHintColor(), textAlign = TextAlign.Center
                        )
                    )
                }
            }
        })
}

@Composable
fun SlideToUnlockVersion5() {
    val layoutDirection = LocalLayoutDirection.current
    var isSlided by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (isCompleted) 1f else 0f,
        label = "alpha",
        animationSpec = tween(durationMillis = 700)
    )
    val colors = if (isCompleted) {
        DefaultSlideToUnlockColors(
            slidedHintColor = Color.White,
            thumbIconColor = Color(0xFF11D483),
            endTrackColor = lerp(Color(0xFFB4AFB4), Color(0xFFC91224), alpha)
        )
    } else {
        DefaultSlideToUnlockColors(
            endTrackColor = Color(0xFFB4AFB4), slidedHintColor = Color.White
        )
    }
    LaunchedEffect(isSlided) {
        if (isSlided) {
            delay(1500)
            isCompleted = true
        }
    }
    SlideToUnlock(
        isSlided = isSlided,
        onSlideCompleted = {
            isSlided = true
        },
        colors = colors,
        modifier = Modifier.fillMaxWidth(),
        hintText = HintText.defaultHintText().copy(
            defaultText = "Add to cart"
        ),
        thumb = { slided, fraction, colors, size, orientation ->
            Box(
                modifier = Modifier
                    .size(size)
                    .background(color = colors.thumbColor(), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCompleted -> {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_error),
                            contentDescription = "Cart added",
                            modifier = Modifier.size(30.dp),
                            tint = colors.thumbIconColor()
                        )
                    }

                    isSlided -> {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(8.dp),
                            color = colors.progressColor(),
                            strokeWidth = 3.dp
                        )
                    }

                    else -> {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_cart),
                            contentDescription = "Add to cart",
                            modifier = Modifier
                                .size(30.dp)
                                .rotate(fraction * -360),
                            tint = colors.thumbIconColor()
                        )
                    }
                }
            }
        },
        hint = { slided, fraction, hintText, colors, padding, orientation ->
            AnimatedContent(
                targetState = isSlided,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) { slided ->
                if (isCompleted) {
                    Text(
                        text = "Item not added to cart.",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.Center,
                            color = colors.slidedHintColor()
                        )
                    )
                } else if (!slided) {
                    Text(
                        text = hintText.defaultText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = padding.calculateStartPadding(layoutDirection)),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = colors.hintColor(
                                fraction
                            ), textAlign = TextAlign.Center
                        )
                    )
                } else {
                    Text(
                        text = hintText.slidedText,
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = colors.slidedHintColor(), textAlign = TextAlign.Center
                        )
                    )
                }
            }
        })
}
