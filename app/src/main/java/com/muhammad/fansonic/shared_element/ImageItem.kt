package com.muhammad.fansonic.shared_element

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ImageItem(
    url: String, height: Int, alpha: Float,
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val imageRequest = remember(url) {
        ImageRequest.Builder(context).data(url).crossfade(true).memoryCacheKey(url).diskCacheKey(url).build()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AsyncImage(
            model = imageRequest, contentDescription = null, onLoading = {
                isLoading = true
            }, onSuccess = {
                isLoading = false
            }, contentScale = ContentScale.Crop, modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .height(height.dp)
                .graphicsLayer {
                    this.alpha = alpha
                }
        )
        AnimatedVisibility(enter = fadeIn(), exit = fadeOut(), visible = isLoading) {
            ContainedLoadingIndicator()
        }
    }
}