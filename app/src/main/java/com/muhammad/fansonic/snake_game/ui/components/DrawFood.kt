package com.muhammad.fansonic.snake_game.ui.components

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.muhammad.fansonic.snake_game.ui.Coordinate

fun DrawScope.drawFood(
    foodBitmap: ImageBitmap,
    cellSize: Int, coordinate: Coordinate,
) {
    drawImage(
        image = foodBitmap,
        dstOffset = IntOffset(x = (coordinate.x * cellSize), y = (coordinate.y * cellSize)),
        dstSize = IntSize(cellSize, cellSize)
    )
}