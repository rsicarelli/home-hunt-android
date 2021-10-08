package com.rsicarelli.homehunt

import android.app.Application
import androidx.constraintlayout.compose.override
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.tag("HomeHunt")
        Timber.plant(Timber.DebugTree())
    }
}

private class CustomDebugThree() : Timber.DebugTree() {

}