package com.muhammad.fansonic.slideToLock

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

@Stable
interface SlideToUnlockColors{
    @Stable
    @Composable
    fun trackColor(slideFraction : Float) : Color
    @Stable
    @Composable
    fun trackBrush(slideFraction: Float) : Brush?
    @Stable
    @Composable
    fun hintColor(slideFraction: Float) : Color
    @Stable
    @Composable
    fun slidedHintColor() : Color
    @Stable
    @Composable
    fun thumbColor() : Color
    @Stable
    @Composable
    fun thumbBrush(slideFraction: Float) : Brush?
    @Stable
    @Composable
    fun thumbIconColor() : Color
    @Stable
    @Composable
    fun progressColor() : Color
}
@Stable
data class DefaultSlideToUnlockColors(
    val startTrackColor : Color = Color(0xFF111111),
    val endTrackColor : Color = Color(0x9F9C9399),
    val trackBrush : Brush?=null,
    val startHintColor : Color = Color.White,
    val endHintColor : Color = Color.White.copy(0.5f),
    val slidedHintColor : Color = Color.Black,
    val thumbColor : Color = Color.White,
    val thumbIconColor : Color = Color.Black,
    val thumbBrush : Brush?=null,
    val progressColor : Color = Color(0xFF11D483)
) : SlideToUnlockColors{
    @Composable
    override fun trackColor(slideFraction: Float): Color {
        val endOfColorChangeFraction = 0.85f
        val fraction = (slideFraction/ endOfColorChangeFraction).coerceIn(0f..1f)
        return lerp(startTrackColor,endTrackColor,fraction)
    }

    @Composable
    override fun trackBrush(slideFraction: Float): Brush? {
        return trackBrush
    }

    @Composable
    override fun hintColor(slideFraction: Float): Color {
        val endOfFadeFraction = 0.45f
        val fraction = (slideFraction / endOfFadeFraction).coerceIn(0f..1f)
        return lerp(startHintColor,endHintColor,fraction)
    }

    @Composable
    override fun slidedHintColor(): Color {
        return slidedHintColor
    }

    @Composable
    override fun thumbColor(): Color {
        return thumbColor
    }

    @Composable
    override fun thumbBrush(slideFraction: Float): Brush? {
        return thumbBrush
    }


    @Composable
    override fun thumbIconColor(): Color {
        return thumbIconColor
    }

    @Composable
    override fun progressColor(): Color {
        return progressColor
    }
}
