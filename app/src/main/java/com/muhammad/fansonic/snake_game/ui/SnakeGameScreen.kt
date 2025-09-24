package com.muhammad.fansonic.snake_game.ui

import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muhammad.fansonic.R
import com.muhammad.fansonic.snake_game.ui.components.AppAlertDialog
import com.muhammad.fansonic.snake_game.ui.components.drawFood
import com.muhammad.fansonic.snake_game.ui.components.drawGameBoard
import com.muhammad.fansonic.snake_game.ui.components.drawSnake
import org.koin.androidx.compose.koinViewModel

@Composable
fun SnakeGameScreen() {
    val viewModel = koinViewModel<SnakeGameViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    SnakeGameContent(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SnakeGameContent(state: SnakeGameState, onAction: (SnakeGameAction) -> Unit) {
    val context = LocalContext.current
    val foodImageBitmap = ImageBitmap.imageResource(R.drawable.food)
    val snakeHeadImageBitmap = ImageBitmap.imageResource(
        when (state.direction) {
            Direction.UP -> R.drawable.snake_head_up
            Direction.DOWN -> R.drawable.snake_head_down
            Direction.LEFT -> R.drawable.snake_head_left
            Direction.RIGHT -> R.drawable.snake_head_right
        }
    )
    val foodSoundMediaPlayer = remember { MediaPlayer.create(context, R.raw.food) }
    val gameOverMediaPlayer = remember { MediaPlayer.create(context, R.raw.gameover) }
    LaunchedEffect(state.snake.size) {
        if (state.snake.size != 2) {
            foodSoundMediaPlayer.start()
        }
    }
    LaunchedEffect(state.isGameOver) {
        if (state.isGameOver) {
            gameOverMediaPlayer.start()
        }
    }
    val cellColor = Color(0xFFC1A13B)
    val borderCellColor = MaterialTheme.colorScheme.primary
    val snakeBodyColor = Color(0xFF186D18)
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Card(
                modifier = Modifier.padding(8.dp), shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = "Score:${state.snake.size - 2}",
                        style = MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.Monospace)
                    )
                }
            }
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2 / 3f)
                    .pointerInput(state.state) {
                        if (state.state != GameState.STARTED) return@pointerInput
                        detectTapGestures { offset ->
                            onAction(
                                SnakeGameAction.UpdateSnakeDirection(
                                    offset = offset,
                                    canvasWidth = size.width
                                )
                            )
                        }
                    }) {
                val cellSize = size.width / 20
                drawGameBoard(
                    cellSize = cellSize,
                    cellColor = cellColor,
                    borderCellColor = borderCellColor,
                    gridWidth = state.xAxisGridSize,
                    gridHeight = state.yAxisGridSize
                )
                drawFood(
                    foodBitmap = foodImageBitmap,
                    cellSize = cellSize.toInt(),
                    coordinate = state.food
                )
                drawSnake(
                    snakeHeadBitmap = snakeHeadImageBitmap,
                    cellSize = cellSize,
                    snakeBodyColor = snakeBodyColor,
                    snake = state.snake
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    16.dp,
                    Alignment.CenterHorizontally
                )
            ) {
                FilledIconButton(
                    onClick = {
                        onAction(SnakeGameAction.ResetGame)
                    }, enabled = state.state == GameState.PAUSED || state.isGameOver,
                    modifier = Modifier.size(IconButtonDefaults.largeContainerSize()),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (state.isGameOver) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                ) {
                    val icon = if (state.isGameOver) R.drawable.ic_reset else R.drawable.ic_heart
                    Icon(
                        imageVector = ImageVector.vectorResource(icon),
                        contentDescription = null,
                        modifier = Modifier.size(IconButtonDefaults.largeIconSize)
                    )
                }
                FilledIconButton(
                    modifier = Modifier.size(IconButtonDefaults.largeContainerSize()),
                    onClick = {
                        when (state.state) {
                            GameState.IDLE, GameState.PAUSED -> onAction(SnakeGameAction.StartGame)
                            GameState.STARTED -> onAction(SnakeGameAction.PauseGame)
                        }
                    }, colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    val icon =
                        if (state.state == GameState.STARTED) R.drawable.ic_pause else R.drawable.ic_play
                    Icon(
                        imageVector = ImageVector.vectorResource(icon),
                        modifier = Modifier.size(IconButtonDefaults.largeIconSize),
                        contentDescription = null
                    )
                }
            }
        }
    }
    if (state.isGameOver) {
        AppAlertDialog(
            onDismiss = {
                onAction(SnakeGameAction.ResetGame)
            },
            titleContent = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_game_over),
                    contentDescription = null, tint = Color.Unspecified,
                    modifier = Modifier.size(100.dp)
                )
            },
            message = "Game Over! You scored ${state.snake.size - 2} points!",
            confirmText = "New Game",
            cancelText = "Cancel", onConfirmClick = {
                onAction(SnakeGameAction.ResetGame)
                onAction(SnakeGameAction.StartGame)
            }, onCancelClick = {
                onAction(SnakeGameAction.ResetGame)
            }
        )
    }
}