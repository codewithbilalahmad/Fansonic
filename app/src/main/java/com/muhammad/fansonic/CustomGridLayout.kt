package com.muhammad.fansonic

import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun CustomGridLayoutScreen() {
    val colors = List(254) {
        Random.nextColor()
    }
    CustomGridLayout(
        items = colors,
        rowHeight = 200.dp,
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) { color ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color ?: Color.LightGray)
        )
    }
}

@Composable
fun <T> CustomGridLayout(
    modifier: Modifier = Modifier,
    items: List<T>,
    rowHeight: Dp,
    state: LazyListState = rememberLazyListState(),
    chunkSize: Int = 10,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    content: @Composable (T?) -> Unit,
) {
    LazyColumn(
        state = state,
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment, flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled, overscrollEffect = overscrollEffect,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout
    ) {
        items(items.chunked(chunkSize)) { chuck ->
            if (chuck.size < chunkSize) {
                chuck.chunked(3).forEach { rowItems ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        rowItems.forEach { item ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.background
                                    )
                            ) {
                                content(item)
                            }
                        }
                        repeat(3 - rowItems.size) {
                            Spacer(Modifier
                                .weight(1f)
                                .aspectRatio(1f))
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(rowHeight)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxHeight()
                    ) {
                        TwoItemRow(
                            rowHeight = rowHeight,
                            item1 = chuck.getOrNull(0),
                            item2 = chuck.getOrNull(1),
                            content = content
                        )
                        TwoItemRow(
                            rowHeight = rowHeight,
                            item1 = chuck.getOrNull(2),
                            item2 = chuck.getOrNull(3),
                            content = content
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight()
                            .border(2.dp, MaterialTheme.colorScheme.background)
                    ) {
                        content(chuck.getOrNull(4))
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(rowHeight)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight()
                            .border(2.dp, MaterialTheme.colorScheme.background)
                    ) {
                        content(chuck.getOrNull(5))
                    }
                    Column(
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxHeight()
                    ) {
                        TwoItemRow(
                            rowHeight = rowHeight,
                            item1 = chuck.getOrNull(6),
                            item2 = chuck.getOrNull(7),
                            content = content
                        )
                        TwoItemRow(
                            rowHeight = rowHeight,
                            item1 = chuck.getOrNull(8),
                            item2 = chuck.getOrNull(9),
                            content = content
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> ColumnScope.TwoItemRow(
    rowHeight: Dp,
    item1: T?,
    item2: T?,
    content: @Composable (T?) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(rowHeight / 2)
                .border(2.dp, MaterialTheme.colorScheme.background)
        ) {
            content(item1)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(rowHeight / 2)
                .border(2.dp, MaterialTheme.colorScheme.background)
        ) {
            content(item2)
        }
    }
}

fun Random.nextColor(): Color {
    return Color(
        red = nextFloat(),
        blue = nextFloat(),
        green = nextFloat(), alpha = 1f
    )
}