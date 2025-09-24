package com.muhammad.fansonic.snake_game.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawGameBoard(
    cellSize: Float,
    cellColor: Color,
    borderCellColor: Color,
    gridWidth: Int,
    gridHeight: Int,
) {
    for (i in 0 until gridWidth) {
        for (j in 0 until gridHeight) {
            val isBorderCell = i == 0 || j == 0 || i == gridWidth - 1 || j == gridHeight - 1
            val color = when {
                isBorderCell -> borderCellColor
                ((i + j) % 2) == 0 -> cellColor
                else -> cellColor.copy(alpha = 0.5f)
            }
            drawRect(
                color = color,
                topLeft = Offset(x = i * cellSize, y = j * cellSize),
                size = Size(width = cellSize, height = cellSize)
            )
        }
    }
}