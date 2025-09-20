package com.muhammad.fansonic.foreground_service

import android.app.Application

class ForegroundApplication : Application(){
    companion object{
        lateinit var INSTANCE : ForegroundApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}