package com.muhammad.fansonic.snake_game

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.muhammad.fansonic.instagram_story.di.storyModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class SnakeGameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SnakeGameApplication)
            androidLogger()
            modules(storyModule)
        }
        MobileAds.initialize(this)
    }
}