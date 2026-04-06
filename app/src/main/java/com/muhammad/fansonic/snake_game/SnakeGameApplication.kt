package com.muhammad.fansonic.snake_game

import android.app.Application
import com.muhammad.fansonic.snake_game.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import com.google.android.gms.ads.MobileAds
import org.koin.core.context.GlobalContext.startKoin

class SnakeGameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SnakeGameApplication)
            androidLogger()
            modules(appModule)
        }
        MobileAds.initialize(this)
    }
}