package com.example.aura

import android.app.Application
import com.example.aura.di.AppContainer

class AuraApp : Application() {
    // Expose the container as a singleton for manual DI
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

