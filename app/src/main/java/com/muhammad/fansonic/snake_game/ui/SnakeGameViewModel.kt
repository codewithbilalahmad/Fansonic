package com.muhammad.fansonic.snake_game.ui

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SnakeGameViewModel : ViewModel() {
    private val _state = MutableStateFlow(SnakeGameState())
    val state = _state.asStateFlow()
    private var gameJob : Job?=null
    fun onAction(action: SnakeGameAction) {
        when (action) {
            SnakeGameAction.PauseGame -> onPauseGame()
            SnakeGameAction.ResetGame -> onResetGame()
            SnakeGameAction.StartGame -> onStartGame()
            is SnakeGameAction.UpdateSnakeDirection -> onUpdateSnakeDirection(
                offset = action.offset,
                canvasWidth = action.canvasWidth
            )
        }
    }

    private fun onStartGame() {
        gameJob?.cancel()
       gameJob =  viewModelScope.launch {
            _state.update { it.copy(state = GameState.STARTED) }
            while (state.value.state == GameState.STARTED) {
                val delayMillis = when (state.value.snake.size) {
                    in 1..5 -> 150L
                    in 6..10 -> 120L
                    else -> 100L
                }
                delay(delayMillis)
                _state.value = updateGame(state.value)
            }
        }
    }

    private fun onUpdateSnakeDirection(offset: Offset, canvasWidth: Int) {
        updateSnakeDirection(offset = offset, canvasWidth = canvasWidth)
    }

    private fun onResetGame() {
        gameJob?.cancel()
        _state.update { SnakeGameState() }
    }

    private fun onPauseGame() {
        _state.update { it.copy(state = GameState.PAUSED) }
    }

    private fun updateSnakeDirection(offset: Offset, canvasWidth: Int) {
        if (state.value.isGameOver) return
        val cellSize = canvasWidth / state.value.xAxisGridSize
        val tapOffsetX = (offset.x / cellSize).toInt()
        val tapOffsetY = (offset.y / cellSize).toInt()
        val head = state.value.snake.first()
        _state.update {
            it.copy(
                direction = when (state.value.direction) {
                    Direction.UP, Direction.DOWN -> if (tapOffsetX < head.x) Direction.LEFT else Direction.RIGHT
                    Direction.LEFT, Direction.RIGHT -> if (tapOffsetY < head.y) Direction.UP else Direction.DOWN
                }
            )
        }
    }

    private fun updateGame(state: SnakeGameState): SnakeGameState {
        if (state.isGameOver) return state
        val head = state.snake.first()
        val xAxisGridSize = state.xAxisGridSize
        val yAxisGridSize = state.yAxisGridSize
        val newHead = when (state.direction) {
            Direction.UP -> Coordinate(x = head.x, y = head.y - 1)
            Direction.DOWN -> Coordinate(x = head.x, y = head.y + 1)
            Direction.LEFT -> Coordinate(x = head.x - 1, y = head.y)
            Direction.RIGHT -> Coordinate(x = head.x + 1, y = head.y)
        }
        if (state.snake.contains(newHead) || !isWithInBounds(
                coordinate = newHead,
                xAxisGridSize = xAxisGridSize,
                yAxisGridSize = yAxisGridSize
            )
        ) return state.copy(isGameOver = true)
        var updatedSnake = mutableListOf(newHead) + state.snake
        val updatedFood =
            if (newHead == state.food) SnakeGameState.generateRandomFoodCoordinates() else state.food
        if (newHead != state.food) {
            updatedSnake = updatedSnake.toMutableList()
            updatedSnake.removeAt(updatedSnake.size - 1)
        }
        return state.copy(snake = updatedSnake, food = updatedFood)
    }

    private fun isWithInBounds(
        coordinate: Coordinate,
        xAxisGridSize: Int,
        yAxisGridSize: Int,
    ): Boolean {
        return coordinate.x in 1 until xAxisGridSize - 1 && coordinate.y in 1 until yAxisGridSize - 1
    }
}