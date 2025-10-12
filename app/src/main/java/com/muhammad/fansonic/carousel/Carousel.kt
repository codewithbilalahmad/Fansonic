package com.muhammad.fansonic.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue

@Composable
fun CarouselScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), contentAlignment = Alignment.Center) {
        Carousel(colors = listOf(
            MaterialTheme.colorScheme.surfaceTint,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.surfaceTint,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.surfaceTint,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.surfaceTint,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.surfaceTint,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary,
        ), modifier = Modifier.fillMaxWidth().height(500.dp), itemContent = {
            Box(
                modifier = Modifier
                    .background(
                        color = it.copy(0.2f),
                        shape = RoundedCornerShape(10)
                    )
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = it,
                            shape = RoundedCornerShape(10)
                        )
                )
            }
        })
    }
}

@Composable
fun Carousel(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    pageState: PagerState = rememberPagerState { colors.size },
    itemContent: @Composable BoxScope.(Color) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(64.dp),
    pageSpacing: Dp = 6.dp,
) {
    HorizontalPager(
        state = pageState,
        contentPadding = contentPadding,
        pageSpacing = pageSpacing,
        flingBehavior = PagerDefaults.flingBehavior(
            state = pageState, pagerSnapDistance = PagerSnapDistance.atMost(3)
        ),
        modifier = modifier.fillMaxSize()
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val pageOffset =
                        ((pageState.currentPage - page) + pageState.currentPageOffsetFraction).absoluteValue
                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }
                    alpha =
                        lerp(start = 0.5f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f))
                }) {
            itemContent(this, colors[page])
        }
    }
}