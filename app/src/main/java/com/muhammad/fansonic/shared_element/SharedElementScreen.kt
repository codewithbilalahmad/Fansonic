package com.muhammad.fansonic.shared_element

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
sealed interface SharedElementScreen{
    @Serializable
    data object Slide : SharedElementScreen
    @Serializable
    data object Scale : SharedElementScreen
}

@Composable
fun SharedElementScreen() {
    val navHostController= rememberNavController()
    SharedTransitionLayout{
        NavHost(navController = navHostController, startDestination = SharedElementScreen.Slide){
            composable<SharedElementScreen.Slide>{
                SharedElementToolbarSlideAnimation(onNavigateToScale = {
                    navHostController.navigate(SharedElementScreen.Scale)
                })
            }
            composable<SharedElementScreen.Scale>{
                SharedElementToolbarScaleAnimation(onNavigateToSlide = {
                    navHostController.navigate(SharedElementScreen.Slide)
                })
            }
        }
    }
}