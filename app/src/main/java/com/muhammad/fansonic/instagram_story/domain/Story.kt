package com.muhammad.fansonic.instagram_story.domain

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable

@Immutable
data class Story(
    val id : Long = System.currentTimeMillis(),
    val username : String,
    val userImage : Int,
    val duration : String,
    val emojis : List<String>,
    val message : TextFieldState = TextFieldState(),
    val isLiked : Boolean = false,
    val desp : String,
    val media : List<Int>
)