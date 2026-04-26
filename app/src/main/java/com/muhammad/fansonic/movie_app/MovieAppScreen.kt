package com.muhammad.fansonic.movie_app

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.muhammad.fansonic.R

@Composable
fun MovieAppScreen() {
    val infiniteTransition = rememberInfiniteTransition()
    val cinematicDim = listOf(
        Color.White.copy(0.10f),
        Color.White.copy(0.08f),
        Color.White.copy(0.06f),
        Color.White.copy(0.04f),
        Color.White.copy(0.02f),
    )
    var swipeAction by remember { mutableStateOf<SwipeResult?>(null) }
    val likeScale by animateFloatAsState(
        targetValue = if (swipeAction == SwipeResult.ACCEPTED) 1f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "likeScale"
    )
    val likeContainerColor by animateColorAsState(
        targetValue = if (swipeAction == SwipeResult.ACCEPTED) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "voteContainerColor"
    )
    val likeContentColor by animateColorAsState(
        targetValue = if (swipeAction == SwipeResult.ACCEPTED) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "voteContentColor"
    )
    val cancelContainerColor by animateColorAsState(
        targetValue = if (swipeAction == SwipeResult.REJECTED) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "voteContainerColor"
    )
    val cancelContentColor by animateColorAsState(
        targetValue = if (swipeAction == SwipeResult.REJECTED) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "voteContentColor"
    )
    val likeShape =
        if (swipeAction == SwipeResult.ACCEPTED) MaterialShapes.Cookie12Sided.toShape() else MaterialShapes.Circle.toShape()
    val cancelShape =
        if (swipeAction == SwipeResult.REJECTED) MaterialShapes.Cookie12Sided.toShape() else MaterialShapes.Circle.toShape()
    val cancelScale by animateFloatAsState(
        targetValue = if (swipeAction == SwipeResult.REJECTED) 1f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cancelScale"
    )
    val likeRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (swipeAction == SwipeResult.ACCEPTED) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing)
        ), label = "likeRotation"
    )
    val cancelRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (swipeAction == SwipeResult.REJECTED) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing)
        ), label = "likeRotation"
    )
    val bubbleScale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "likeRotation"
    )
    val bubbleOffset by infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "bubbleOffset"
    )
    val movies = remember {
        mutableStateListOf<MovieItem>().apply {
            addAll(
                listOf(
                    MovieItem(
                        image = R.drawable.movie_1,
                        title = "The Silent Horizon",
                        description = "A lone astronaut drifts beyond known space while struggling to survive and uncover the truth behind a mysterious signal coming from the void.",
                        rating = "8.5"
                    ),
                    MovieItem(
                        image = R.drawable.movie_2,
                        title = "Crimson Chase",
                        description = "A determined detective tracks a dangerous criminal across multiple cities, only to discover a hidden conspiracy that changes everything.",
                        rating = "7.9"
                    ),
                    MovieItem(
                        image = R.drawable.movie_3,
                        title = "Echoes of Time",
                        description = "A brilliant scientist discovers a way to hear echoes from the past, but altering events leads to unexpected and dangerous consequences.",
                        rating = "8.2"
                    ),
                    MovieItem(
                        image = R.drawable.movie_4,
                        title = "Midnight Run",
                        description = "A skilled getaway driver is forced into one final job that quickly turns into a chaotic night filled with betrayal and survival.",
                        rating = "7.8"
                    ),
                    MovieItem(
                        image = R.drawable.movie_5,
                        title = "Frozen Kingdom",
                        description = "In a land covered by ice, a fearless warrior must unite divided tribes to face an ancient threat that could destroy their world.",
                        rating = "8.0"
                    ),
                    MovieItem(
                        image = R.drawable.movie_6,
                        title = "Neon City",
                        description = "In a futuristic cyberpunk world, a hacker rises against powerful corporations as technology begins to blur the line between human and machine.",
                        rating = "8.4"
                    ),
                    MovieItem(
                        image = R.drawable.movie_7,
                        title = "The Last Signal",
                        description = "A radio operator intercepts a mysterious distress signal that leads him to an abandoned facility hiding a terrifying secret.",
                        rating = "7.6"
                    ),
                    MovieItem(
                        image = R.drawable.movie_8,
                        title = "Shadow Realm",
                        description = "A young girl accidentally enters a dark parallel world and must face her deepest fears to find her way back home safely.",
                        rating = "8.1"
                    ),
                    MovieItem(
                        image = R.drawable.movie_9,
                        title = "Broken Vows",
                        description = "A couple’s relationship begins to fall apart as hidden secrets surface, forcing them to confront painful truths about their past.",
                        rating = "7.7"
                    ),
                    MovieItem(
                        image = R.drawable.movie_10,
                        title = "Skyfall Warriors",
                        description = "Elite fighter pilots defend Earth against powerful aerial threats, pushing their limits in battles that decide humanity’s future.",
                        rating = "8.3"
                    ),
                    MovieItem(
                        image = R.drawable.movie_3,
                        title = "Desert Storm",
                        description = "A group of soldiers must survive harsh desert conditions while completing a dangerous mission that tests their courage and loyalty.",
                        rating = "7.5"
                    ),
                    MovieItem(
                        image = R.drawable.movie_2,
                        title = "Hidden Truth",
                        description = "An ambitious journalist uncovers a powerful secret that puts her life at risk as she dives deeper into a dangerous investigation.",
                        rating = "8.0"
                    ),
                    MovieItem(
                        image = R.drawable.movie_13,
                        title = "Ocean Depths",
                        description = "A team of divers explores the deepest parts of the ocean, only to encounter a mysterious presence that defies explanation.",
                        rating = "8.2"
                    ),
                    MovieItem(
                        image = R.drawable.movie_15,
                        title = "Final Stand",
                        description = "A retired hero is called back for one final battle where the fate of many depends on his strength, experience, and sacrifice.",
                        rating = "8.6"
                    ),
                    MovieItem(
                        image = R.drawable.movie_14,
                        title = "Dreamcatcher",
                        description = "A man gains the ability to enter dreams and solve mysteries, but soon struggles as reality and imagination begin to merge.",
                        rating = "8.1"
                    )
                )
            )
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
        , topBar = {
        CenterAlignedTopAppBar(title = {
            Text(text = stringResource(R.string.movies))
        }, actions = {
            IconButton(
                onClick = {},
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                shapes = IconButtonDefaults.shapes()
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_heart),
                    contentDescription = null
                )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val center = Offset(size.width * 0.2f + bubbleOffset, size.height * 0.1f + bubbleOffset)
                    val base = size.minDimension * bubbleScale

                    drawCircle(
                        color =  cinematicDim[4],
                        radius = base * 0.7f,
                        center = center
                    )
                    drawCircle(
                        color = cinematicDim[3],
                        radius = base * 0.6f,
                        center = center
                    )
                    drawCircle(
                        color = cinematicDim[2],
                        radius = base * 0.5f,
                        center = center
                    )
                    drawCircle(
                        color = cinematicDim[1],
                        radius = base * 0.4f,
                        center = center
                    )
                    drawCircle(
                        color = cinematicDim[0],
                        radius = base * 0.3f,
                        center = center
                    )
                }
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                movies.forEachIndexed { index, movie ->
                    val isTop = index == movies.size - 1
                    val scale by animateFloatAsState(
                        targetValue = if (isTop) 1f else 0.95f,
                        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                        label = "scale"
                    )
                    MovieDraggableCard(
                        movieItem = movie,
                        onSwiped = { swipeResult, movie ->
                            swipeAction = swipeResult
                            movies.remove(movie as MovieItem)
                        }, enabled = isTop,
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                            .fillMaxWidth(0.85f)
                            .zIndex(index.toFloat())
                            .padding(top = ((index + 1) * 6).dp)
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    24.dp,
                    Alignment.CenterHorizontally
                )
            ) {
                Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = cancelScale
                                scaleY = cancelScale
                                rotationZ = cancelRotation
                            }
                            .fillMaxSize()
                            .clip(cancelShape)
                            .background(cancelContainerColor)
                            .clickable{
                                swipeAction = SwipeResult.REJECTED
                                movies.removeLastOrNull()
                            }
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_cancel),
                        tint = cancelContentColor,
                        modifier = Modifier.size(35.dp),
                        contentDescription = null
                    )
                }
                Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = likeScale
                                scaleY = likeScale
                                rotationZ = likeRotation
                            }
                            .fillMaxSize()
                            .clip(likeShape)
                            .background(likeContainerColor)
                            .clickable{
                                swipeAction = SwipeResult.ACCEPTED
                                movies.removeLastOrNull()
                            }
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_heart),
                        tint = likeContentColor,
                        modifier = Modifier.size(35.dp),
                        contentDescription = null
                    )
                }
            }
        }
    }
}