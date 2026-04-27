package com.muhammad.fansonic.instagram_story

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muhammad.fansonic.R
import com.muhammad.fansonic.instagram_story.components.MessageTextField
import com.muhammad.fansonic.instagram_story.components.StoryBottomBar
import com.muhammad.fansonic.instagram_story.components.StoryProgressIndicator
import com.muhammad.fansonic.instagram_story.utils.rippleClickable
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun InstagramStoryScreen(viewModel: InstagramStoryViewModel = koinViewModel()) {
    val context = LocalContext.current
    val activity = context as Activity
    val configuration = LocalConfiguration.current
    val layoutDirection = LocalLayoutDirection.current
    val density = LocalDensity.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { state.stories.size }
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { page ->
        var currentTimelineIndex by remember(page) { mutableIntStateOf(0) }
        var isPaused by remember(page) { mutableStateOf(false) }
        var showMessageSection by remember { mutableStateOf(false) }
        var showEmojiAnimation by remember { mutableStateOf(false) }
        val focusRequester = remember(page) { FocusRequester() }
        val story = state.stories[page]
        val pageOffset = pagerState.getOffsetDistanceInPages(page)
        BackHandler {
            when {
                showMessageSection -> {
                    showMessageSection = false
                }

                else -> {
                    activity.finishAffinity()
                }
            }
        }
        LaunchedEffect(showMessageSection) {
            showEmojiAnimation = showMessageSection
            if (showMessageSection) {
                focusRequester.requestFocus()
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        val rotation = 60f * pageOffset
                        rotationY = rotation
                        cameraDistance = 12f * density.density
                        transformOrigin = TransformOrigin(
                            pivotFractionX = if (pageOffset < 0f) 1f else 0f,
                            pivotFractionY = 0.5f
                        )
                        alpha =
                            if (pageOffset.absoluteValue > 1f) 0f else 1f - pageOffset.absoluteValue.coerceIn(
                                0f,
                                0.7f
                            )
                    }, bottomBar = {
                    StoryBottomBar(
                        modifier = Modifier.fillMaxWidth(),
                        isLiked = story.isLiked,
                        message = story.message,
                        onMessageClick = {
                            showMessageSection = !showMessageSection
                        }, onToggleLike = {
                            viewModel.onAction(InstagramStoryAction.OnToggleStoryLike(story.id))
                        })
                }) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = { offset ->

                                    isPaused = true

                                    val startTime = System.currentTimeMillis()

                                    val released = tryAwaitRelease()

                                    val pressDuration = System.currentTimeMillis() - startTime

                                    isPaused = false

                                    if (!released) return@detectTapGestures

                                    if (pressDuration > 200L) return@detectTapGestures

                                    val width = size.width

                                    currentTimelineIndex =
                                        if (offset.x < width / 2f) {
                                            (currentTimelineIndex - 1).coerceAtLeast(0)
                                        } else {
                                            (currentTimelineIndex + 1)
                                                .coerceAtMost(story.media.lastIndex)
                                        }
                                }
                            )
                        }
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(
                            story.media[currentTimelineIndex.coerceIn(
                                0,
                                story.media.lastIndex
                            )]
                        ),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp)
                    ) {
                        StoryProgressIndicator(
                            totalSteps = story.media.size,
                            onStepFinished = { index ->
                                if (index < story.media.size - 1) {
                                    currentTimelineIndex++
                                } else {
                                    if (pagerState.currentPage < state.stories.lastIndex) {
                                        currentTimelineIndex = 0
                                        scope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    } else {
                                        currentTimelineIndex = 0
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            isPaused = isPaused || showMessageSection, isActive = pagerState.currentPage == page,
                            currentStep = currentTimelineIndex
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(story.userImage),
                                contentDescription = null, contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = story.username,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Spacer(Modifier.width(2.dp))
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_verified),
                                        contentDescription = null, modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = story.duration,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Text(
                                    text = story.desp,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_menu),
                                contentDescription = null, modifier = Modifier.size(22.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
            if (showMessageSection) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                showMessageSection = false
                            })
                        }
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                showMessageSection = false
                            }
                        },
                    containerColor = Color.Black.copy(0.8f)
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                paddingValues = PaddingValues(
                                    start = paddingValues.calculateLeftPadding(
                                        layoutDirection
                                    ),
                                    end = paddingValues.calculateRightPadding(layoutDirection),
                                    top = paddingValues.calculateTopPadding()
                                )
                            )
                    ) {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .padding(top = configuration.screenHeightDp.dp * 0.1f),
                            horizontalArrangement = Arrangement.spacedBy(
                                50.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalArrangement = Arrangement.spacedBy(50.dp),
                            maxLines = 2,
                            maxItemsInEachRow = 3
                        ) {
                            story.emojis.forEachIndexed { index, emoji ->
                                val delay = index * 80
                                val emojiScale by animateFloatAsState(
                                    targetValue = if (showEmojiAnimation) 1f else 0f,
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        delayMillis = delay
                                    ),
                                    label = "emojiScale$index"
                                )
                                Text(
                                    text = emoji, fontSize = 30.sp, modifier = Modifier
                                        .graphicsLayer {
                                            scaleX = emojiScale
                                            scaleY = emojiScale
                                        }
                                        .rippleClickable {
                                            story.message.edit {
                                                if (length > 0) append(" ")
                                                append(emoji)
                                                placeCursorAtEnd()
                                            }
                                        })
                            }
                        }
                        MessageTextField(
                            state = story.message,
                            hint = R.string.send_message,
                            onKeyboardAction = {
                                showMessageSection = false
                            },
                            trailingIcon = R.drawable.ic_send_message,
                            showTrailingIcon = story.message.text.isNotEmpty(),
                            onTrailingIconClick = {
                                showMessageSection = false
                                story.message.setTextAndPlaceCursorAtEnd("")
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.BottomCenter)
                                .navigationBarsPadding()
                                .imePadding()
                                .focusRequester(focusRequester)
                        )
                    }
                }
            }
        }
    }
}