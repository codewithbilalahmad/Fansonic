package com.muhammad.fansonic.instagram_story.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R
import com.muhammad.fansonic.instagram_story.utils.rippleClickable
import kotlinx.coroutines.launch

@Composable
fun StoryBottomBar(
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    message: TextFieldState,
    onToggleLike: () -> Unit,
    onMessageClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val likeScale = remember { Animatable(1f) }
    val likeTint by animateColorAsState(
        targetValue = if (isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "likeTint"
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MessageTextField(
            state = message,
            readOnly = true,
            onClick = onMessageClick,
            modifier = Modifier
                .weight(1f),
            hint = R.string.send_message
        )
        Spacer(Modifier.width(12.dp))
        Icon(
            imageVector = ImageVector.vectorResource(if (isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart_outlined),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer{
                    scaleX = likeScale.value
                    scaleY = likeScale.value
                }
                .size(32.dp)
                .rippleClickable(onClick = {
                    onToggleLike()
                    scope.launch {
                        likeScale.snapTo(1f)
                        likeScale.animateTo(
                            targetValue = 1.3f,
                            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                        )
                        likeScale.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                        )
                    }
                }),
            tint = likeTint
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_send),
            contentDescription = null, modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}