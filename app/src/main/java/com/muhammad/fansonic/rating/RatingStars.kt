package com.muhammad.fansonic.rating

import androidx.annotation.IntRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RatingStarsScreen() {
    var rating by remember { mutableIntStateOf(2) }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), contentAlignment = Alignment.Center) {
        RatingStars(
            modifier = Modifier.padding(20.dp),
            rating = rating,
            onRatingChange = { rate -> rating = rate })
    }
}

@Composable
fun RatingStars(
    @IntRange(0, 5) rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 40.dp,
    itemSpacing: Dp = 8.dp,
    animOverlapDelay: Long = 200L,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioHighBouncy,
        stiffness = Spring.StiffnessLow
    ),
) {
    var animatedRating by remember { mutableIntStateOf(rating) }
    val boxesScale = List(5) {
        remember { Animatable(if (rating <= it) 0.8f else 1f) }
    }
    var prevRating by remember { mutableIntStateOf(rating) }
    LaunchedEffect(rating) {
        val isDecreased = rating < prevRating
        val boxes = if (isDecreased) boxesScale.take(prevRating).drop(rating)
            .reversed() else boxesScale.drop(prevRating).take(rating - prevRating)
        prevRating = rating
        boxes.forEachIndexed { index, animatable ->
            launch {
                delay(index * animOverlapDelay)
                animatedRating = if (isDecreased) animatedRating - 1 else animatedRating + 1
                animatable.animateTo(if (isDecreased) 0.8f else 1f, animationSpec)
            }
        }
    }
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(itemSpacing)) {
        repeat(5) { index ->
            val icon =
                if (index < animatedRating) R.drawable.ic_star else R.drawable.ic_star_outined
            val tint =
                if (index < animatedRating) Color.Unspecified else MaterialTheme.colorScheme.onBackground
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .size(iconSize)
                    .scale(boxesScale[index].value)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            onRatingChange(index + 1)
                        })
            )
        }
    }
}