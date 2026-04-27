package com.muhammad.fansonic.instagram_story

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InstagramStoryViewModel : ViewModel() {
    private val _state = MutableStateFlow(InstagramStoryState())
    val state = _state.asStateFlow()
    fun onAction(action: InstagramStoryAction) {
        when (action) {
            is InstagramStoryAction.OnToggleStoryLike -> onToggleStoryLike(action.id)
        }
    }

    private fun onToggleStoryLike(id: Long) {
        _state.update { currentState ->
            currentState.copy(stories = currentState.stories.map { story ->
                if (story.id == id) story.copy(isLiked = !story.isLiked) else story
            })
        }
    }
}