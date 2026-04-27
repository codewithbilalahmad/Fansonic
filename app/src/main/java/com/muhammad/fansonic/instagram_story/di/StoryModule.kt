package com.muhammad.fansonic.instagram_story.di

import com.muhammad.fansonic.instagram_story.InstagramStoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val storyModule = module {
    viewModelOf(::InstagramStoryViewModel)
}