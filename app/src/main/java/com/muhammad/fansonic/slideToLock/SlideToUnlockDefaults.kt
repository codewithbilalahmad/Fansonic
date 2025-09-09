package com.muhammad.fansonic.slideToLock

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R

object SlideToUnlockDefaults {
    val ThumbSize: Dp = 42.dp
    val Padding: Dp = 8.dp
    val VelocityThreshold: Dp = 60.dp

    @Composable
    fun Thumb(
        isSlided: Boolean,
        thumbSize: DpSize,
        colors: SlideToUnlockColors = DefaultSlideToUnlockColors(),
        orientation: SlideOrientation = SlideOrientation.Horizontal,
        modifier: Modifier = Modifier,
    ) {
        Box(
            modifier = modifier
                .size(thumbSize)
                .background(color = colors.thumbColor(), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(targetState = isSlided, transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }) { slided ->
                if (slided) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(8.dp),
                        color = colors.progressColor(),
                        strokeWidth = 3.dp
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            when (orientation) {
                                SlideOrientation.Horizontal -> R.drawable.ic_next
                                SlideOrientation.Vertical -> R.drawable.ic_down
                            }
                        ), contentDescription = when (orientation) {
                            SlideOrientation.Horizontal -> "Slide to unlock"
                            SlideOrientation.Vertical -> "Slide down to unlock"
                        }, tint = colors.thumbIconColor()
                    )
                }
            }
        }
    }

    @Composable
    fun Hint(
        modifier: Modifier = Modifier,
        hintText: HintText,
        isSlided: Boolean,
        slideFraction: Float,
        colors: SlideToUnlockColors,
        paddingValues: PaddingValues,
        orientation: SlideOrientation = SlideOrientation.Horizontal,
    ) {
        val layoutDirection = LocalLayoutDirection.current
        AnimatedContent(modifier = modifier.run {
            when (orientation) {
                SlideOrientation.Horizontal -> fillMaxWidth()
                SlideOrientation.Vertical -> fillMaxHeight()
            }
        }, targetState = isSlided, transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }) { slided ->
            if (!slided) {
                when (orientation) {
                    SlideOrientation.Horizontal -> {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = paddingValues.calculateStartPadding(layoutDirection)),
                            text = hintText.defaultText,
                            textAlign = TextAlign.Center,
                            color = colors.hintColor(slideFraction),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    SlideOrientation.Vertical -> {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(top = paddingValues.calculateTopPadding()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = hintText.defaultText,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    textAlign = TextAlign.Center,
                                    color = colors.hintColor(slideFraction)
                                ))
                        }
                    }
                }
            } else{
                when (orientation) {
                    SlideOrientation.Horizontal -> {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = hintText.slidedText,
                            textAlign = TextAlign.Center,
                            color = colors.hintColor(slideFraction),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    SlideOrientation.Vertical -> {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = hintText.slidedText,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    textAlign = TextAlign.Center,
                                    color = colors.hintColor(slideFraction)
                                ))
                        }
                    }
                }
            }
        }
    }
}