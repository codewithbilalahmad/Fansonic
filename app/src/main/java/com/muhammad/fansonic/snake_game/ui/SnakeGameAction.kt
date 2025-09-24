package com.muhammad.fansonic.snake_game.ui

import androidx.compose.ui.geometry.Offset

sealed class SnakeGameAction {
    data object StartGame : SnakeGameAction()
    data object PauseGame : SnakeGameAction()
    data object ResetGame : SnakeGameAction()
    data class UpdateSnakeDirection(val offset: Offset, val canvasWidth: Int) : SnakeGameAction()
}