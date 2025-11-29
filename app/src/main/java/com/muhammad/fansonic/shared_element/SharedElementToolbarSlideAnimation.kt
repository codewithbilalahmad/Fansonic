package com.muhammad.fansonic.shared_element

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.SharedContentConfig
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.muhammad.fansonic.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCoerceAtMost
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay

private val TechBlack = Color(0xFF0B0B0D)

@OptIn(
    ExperimentalSharedTransitionApi::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun SharedTransitionScope.SharedElementToolbarSlideAnimation(
    onNavigateToScale: () -> Unit,
) {
    val key = "key_shared_transition_toolbar_slide_anim"
    val gridState = rememberLazyStaggeredGridState()
    val density = LocalDensity.current
    var scaffordPaddingTopPx by remember { mutableFloatStateOf(0f) }
    var contentTitleY by remember { mutableFloatStateOf(0f) }
    var contentTitleHeight by remember { mutableFloatStateOf(0f) }
    val isLayoutReady by remember {
        derivedStateOf {
            contentTitleHeight > 0f && contentTitleY > 0f && scaffordPaddingTopPx > 0f
        }
    }
    val config = remember {
        object : SharedContentConfig {
            override val SharedTransitionScope.SharedContentState.isEnabled: Boolean
                get() = isLayoutReady
        }
    }
    val scrollThreshold by remember {
        derivedStateOf {
            val contentTitleRelativeY = contentTitleY - scaffordPaddingTopPx
            val contentTitleMidPointRelative = contentTitleRelativeY + (contentTitleHeight / 2f)
            contentTitleMidPointRelative
        }
    }
    val transitionProgress by remember {
        derivedStateOf {
            val firstItemIndex = gridState.firstVisibleItemIndex
            val firstItemOffset = gridState.firstVisibleItemScrollOffset
            when {
                firstItemIndex > 0 -> 1f
                else -> (firstItemOffset / scrollThreshold).coerceIn(0f, 1f)
            }
        }
    }
    val isTitleInAppBar by remember {
        derivedStateOf { transitionProgress >= 1f }
    }
    val images = remember {
        List(30) { index ->
            val imageHeight = if (index % 2 == 0) 300 else 200
            imageHeight to "https://picsum.photos/id/${index + 10}/${imageHeight}/${imageHeight * 2}"
        }
    }
    var showImages by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(500)
        showImages = true
    }
    val imageAlpha by animateFloatAsState(
        targetValue = if (showImages) 1f else 0f,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "imagesAlpha"
    )
    val titleContent = remember {
        movableContentOf {
            Text(text = "Photos",color = Color.White, fontSize = if (isTitleInAppBar) 20.sp else 48.sp)
        }
    }
    Scaffold(containerColor = TechBlack, topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = TechBlack,
                navigationIconContentColor = Color.White,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White
            ), navigationIcon = {
                IconButton(onClick = onNavigateToScale) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                        contentDescription = null, modifier = Modifier.size(24.dp)
                    )
                }
            }, title = {
                val slideInAnimation by animateFloatAsState(
                    targetValue = if (isTitleInAppBar) 1f else 0f, animationSpec = keyframes {
                        durationMillis = 300
                        0f at 0
                        1f at 300 using FastOutSlowInEasing
                    }, label = "slideInAnimation"
                )
                val alphaAnimation by animateFloatAsState(
                    targetValue = if (isTitleInAppBar) 1f else 0f, animationSpec = keyframes {
                        durationMillis = 500
                        0f at 0
                        1f at 500 using FastOutSlowInEasing
                    }, label = "alphaAnimation"
                )
                Box(
                    modifier = Modifier
                        .clipToBounds()
                        .sharedElementWithCallerManagedVisibility(
                            renderInOverlayDuringTransition = false,
                            sharedContentState = rememberSharedContentState(
                                key = key, config = config
                            ), visible = isTitleInAppBar
                        )
                        .skipToLookaheadSize()
                        .graphicsLayer {
                            alpha = alphaAnimation
                            translationX = if (isTitleInAppBar) {
                                lerp(-size.width, 0f, slideInAnimation)
                            } else 0f
                        }) {
                    if (isTitleInAppBar) titleContent()
                }
            }, actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_done),
                        contentDescription = null
                    )
                }
            })
    }) { paddingValues ->
        if (scaffordPaddingTopPx == 0f) {
            scaffordPaddingTopPx = with(density) {
                paddingValues.calculateTopPadding().toPx()
            }
        }
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            state = gridState,
            contentPadding = paddingValues,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            item(key = "title", span = StaggeredGridItemSpan.FullLine) {
                Box(
                    modifier = Modifier
                        .clipToBounds()
                        .padding(top = 32.dp)
                        .height(60.dp)
                        .onPlaced { coordinates ->
                            if (contentTitleY == 0f || contentTitleHeight == 0f) {
                                contentTitleY = coordinates.positionInRoot().y
                                contentTitleHeight = coordinates.size.height.toFloat()
                            }
                        }
                        .sharedElementWithCallerManagedVisibility(
                            renderInOverlayDuringTransition = false,
                            sharedContentState = rememberSharedContentState(
                                key = key,
                                config = config
                            ), visible = !isTitleInAppBar
                        )
                        .skipToLookaheadSize()
                        .skipToLookaheadPosition()
                        .graphicsLayer {
                            alpha = 1f - transitionProgress.fastCoerceAtMost(0.75f)
                        }
                ) {
                    if (!isTitleInAppBar) titleContent()
                }
            }
            items(items = images, key = { index ->
                "photo_$index"
            }) { item ->
                ImageItem(
                    url = item.second,
                    height = item.first, alpha = imageAlpha
                )
            }
        }
    }
}