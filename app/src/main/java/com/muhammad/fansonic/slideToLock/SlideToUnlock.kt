package com.muhammad.fansonic.slideToLock

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.muhammad.fansonic.slideToLock.SlideToUnlockDefaults.ThumbSize
import kotlin.math.roundToInt

@Composable
fun SlideToUnlock(
    isSlided: Boolean,
    onSlideCompleted: () -> Unit = {},
    modifier: Modifier = Modifier,
    colors: SlideToUnlockColors = DefaultSlideToUnlockColors(),
    hintText: HintText = HintText.defaultHintText(),
    trackShape: Shape = RoundedCornerShape(50),
    thumbSize: DpSize = DpSize(ThumbSize, ThumbSize),
    padding: PaddingValues = PaddingValues(SlideToUnlockDefaults.Padding),
    hintPadding: PaddingValues = PaddingValues(start = thumbSize.width, end = thumbSize.width),
    onSlideFractionChanged: (Float) -> Unit = {},
    orientation: SlideOrientation = SlideOrientation.Horizontal,
    thumb: @Composable BoxScope.(isSlided: Boolean, slideFraction: Float, colors: SlideToUnlockColors, size: DpSize, orientation: SlideOrientation) -> Unit = { slided, _, _, size, orient ->
        SlideToUnlockDefaults.Thumb(
            modifier = Modifier.size(thumbSize),
            isSlided = slided,
            thumbSize = size,
            colors = colors,
            orientation = orient
        )
    },
    hint: @Composable BoxScope.(isSlided: Boolean, slideFraction: Float, hintText: HintText, colors: SlideToUnlockColors, padding: PaddingValues, orientation: SlideOrientation) -> Unit = { slided, fraction, _, _, padding, orient ->
        SlideToUnlockDefaults.Hint(
            modifier = Modifier.align(Alignment.Center),
            slideFraction = fraction,
            isSlided = slided,
            hintText = hintText,
            colors = colors,
            paddingValues = padding
        )
    },
) {
    val hapticFeedback = LocalHapticFeedback.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    var measuredWidth by remember { mutableIntStateOf(0) }
    var measuredHeight by remember { mutableIntStateOf(0) }
    val dragState = remember {
        AnchoredDraggableState(
            initialValue = if (isSlided) SlideToUnlockValue.End else SlideToUnlockValue.Start,
            confirmValueChange = { target ->
                if (target == SlideToUnlockValue.End) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSlideCompleted()
                }
                true
            }
        )
    }
    LaunchedEffect(isSlided) {
        dragState.animateTo(if(isSlided) SlideToUnlockValue.End else SlideToUnlockValue.Start)
    }
    LaunchedEffect(measuredWidth, measuredHeight, orientation) {
        val endAnchorPx = with(density) {
            when (orientation) {
                SlideOrientation.Horizontal ->{
                    val totalPadding = padding.calculateStartPadding(layoutDirection) + padding.calculateStartPadding(layoutDirection)
                    measuredWidth - (totalPadding + thumbSize.width).toPx()
                }
                SlideOrientation.Vertical ->{
                    val totalPadding = padding.calculateTopPadding() + padding.calculateBottomPadding()
                    measuredHeight -(totalPadding + thumbSize.height).toPx()
                }
            }
        }
        dragState.updateAnchors(
            DraggableAnchors {
                SlideToUnlockValue.Start at 0f
                SlideToUnlockValue.End at endAnchorPx
            }
        )
    }
    val slideFraction by remember {
        derivedStateOf {
            val anchors = dragState.anchors
            val start = anchors.positionOf(SlideToUnlockValue.Start)
            val end = anchors.positionOf(SlideToUnlockValue.End)
            val offset = dragState.offset
            if (end > start) (offset - start) / (end - start) else 0f
        }
    }
    LaunchedEffect(slideFraction) {
        onSlideFractionChanged(slideFraction.coerceIn(0f, 1f))
    }
    SlideToUnlockTrack(
        dragState = dragState,
        onSizeChanged = { size ->
            measuredWidth = size.width
            measuredHeight = size.height
        },
        slideFraction = slideFraction,
        enabled = !isSlided,
        colors = colors,
        trackShape = trackShape, modifier = modifier,
        paddingValues = padding,
        orientation = orientation,
        content = {
            hint(isSlided, slideFraction, hintText, colors, hintPadding, orientation)
            Box(modifier = Modifier.offset {
                val safeOffset = dragState.offset.takeIf {offset -> offset.isFinite()  } ?: 0f
                when (orientation) {
                    SlideOrientation.Horizontal -> IntOffset(x = safeOffset.roundToInt(), 0)
                    SlideOrientation.Vertical -> IntOffset(x = 0, y = safeOffset.roundToInt())
                }
            }) {
                thumb(isSlided, slideFraction, colors, thumbSize, orientation)
            }
        })
}

@Composable
private fun SlideToUnlockTrack(
    modifier: Modifier = Modifier,
    dragState: AnchoredDraggableState<SlideToUnlockValue>,
    slideFraction: Float,
    enabled: Boolean,
    colors: SlideToUnlockColors,
    trackShape: Shape,
    paddingValues: PaddingValues,
    orientation: SlideOrientation, onSizeChanged: (IntSize) -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val trackColor = colors.trackColor(slideFraction)
    val trackBrush = colors.trackBrush(slideFraction)
    Box(
        modifier = modifier
            .run {
                when (orientation) {
                    SlideOrientation.Horizontal -> fillMaxWidth()
                    SlideOrientation.Vertical -> fillMaxHeight()
                }
            }
            .onSizeChanged(onSizeChanged = onSizeChanged)
            .run {
                if (trackBrush != null) {
                    background(brush = trackBrush, shape = trackShape)
                } else {
                    background(color = trackColor, shape = trackShape)
                }
            }
            .padding(paddingValues = paddingValues)
            .anchoredDraggable(
                state = dragState,
                orientation = when (orientation) {
                    SlideOrientation.Horizontal -> Orientation.Horizontal
                    SlideOrientation.Vertical -> Orientation.Vertical
                },
                enabled = enabled
            ), content = content)
}
