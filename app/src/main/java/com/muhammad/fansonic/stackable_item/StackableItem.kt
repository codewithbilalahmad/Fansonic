package com.muhammad.fansonic.stackable_item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun StackableItemScreen() {
    val listState = rememberLazyListState()
    val colors = listOf(
        Color.White.copy(0.8f),
        MaterialTheme.colorScheme.primary.copy(0.5f),
        MaterialTheme.colorScheme.secondary.copy(0.5f),
        MaterialTheme.colorScheme.tertiary.copy(0.5f),
        Color.White.copy(0.8f),
        MaterialTheme.colorScheme.primary.copy(0.5f),
        MaterialTheme.colorScheme.secondary.copy(0.5f),
        MaterialTheme.colorScheme.tertiary.copy(0.5f),
        Color.White.copy(0.8f),
        MaterialTheme.colorScheme.primary.copy(0.5f),
        MaterialTheme.colorScheme.secondary.copy(0.5f),
        MaterialTheme.colorScheme.tertiary.copy(0.5f),
        Color.White.copy(0.8f),
        MaterialTheme.colorScheme.primary.copy(0.5f),
        MaterialTheme.colorScheme.secondary.copy(0.5f),
        MaterialTheme.colorScheme.tertiary.copy(0.5f),
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(20.dp)
    ) {
        itemsIndexed(items = colors) { index, color ->
            StackableItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
                    .stackingEffect(listState, index)
            )
        }
    }
}

@Composable
fun Modifier.stackingEffect(state: LazyListState, index: Int): Modifier {
    val isCurrentItem by remember {
        derivedStateOf { index == state.firstVisibleItemIndex }
    }
    val isUnderStack by remember {
        derivedStateOf { index < state.firstVisibleItemIndex }
    }
    val offset by remember {
        derivedStateOf {
            if (isCurrentItem) state.firstVisibleItemScrollOffset.toFloat() else 0f
        }
    }
    val scale by remember {
        derivedStateOf {
            val progress = state.layoutInfo.visibleItemsInfo.firstOrNull()?.let { item ->
                offset / item.size.toFloat()
            } ?: 0f
            when {
                isUnderStack -> 0f
                isCurrentItem -> 1f - (progress * 0.2f)
                else -> 1f
            }
        }
    }
    return graphicsLayer {
        translationY = offset
        scaleX = scale
        scaleY = scale
    }
}

@Composable
fun StackableItem(modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.primary) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = color.copy(0.2f), shape = RoundedCornerShape(10))
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = color,
                    shape = RoundedCornerShape(10)
                )
        )
    }
}