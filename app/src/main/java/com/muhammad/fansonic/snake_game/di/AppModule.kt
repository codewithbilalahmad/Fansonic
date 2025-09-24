package com.muhammad.fansonic.snake_game.di

import com.muhammad.fansonic.snake_game.ui.SnakeGameViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::SnakeGameViewModel)
}