package com.muhammad.fansonic.movie_app

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class MovieItem(
    @get:DrawableRes val image : Int,
    val title : String,
    val description : String,
    val rating : String
)