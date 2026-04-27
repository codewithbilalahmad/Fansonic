package com.muhammad.fansonic.instagram_story

sealed interface InstagramStoryAction{
    data class OnToggleStoryLike(val id : Long) : InstagramStoryAction
}