package com.muhammad.fansonic.snake_game.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.muhammad.fansonic.snake_game.ui.Coordinate

fun DrawScope.drawSnake(
    snakeHeadBitmap: ImageBitmap,
    cellSize: Float,snakeBodyColor : Color,
    snake: List<Coordinate>,
) {
    val cellSizeInt = cellSize.toInt()
    snake.forEachIndexed { index, coordinate ->
        val radius = if (index == snake.lastIndex) cellSize / 2.5f else cellSize / 2f
        if (index == 0) {
            drawImage(
                image = snakeHeadBitmap,
                dstOffset = IntOffset(
                    x = (coordinate.x * cellSizeInt),
                    y = (coordinate.y * cellSizeInt)
                ),
                dstSize = IntSize(cellSizeInt, cellSizeInt)
            )
        } else {
            drawCircle(
                color = snakeBodyColor,
                center = Offset(
                    x = (coordinate.x * cellSizeInt) + radius,
                    y = (coordinate.y * cellSizeInt) + radius
                ),
                radius = radius
            )
        }
    }
}