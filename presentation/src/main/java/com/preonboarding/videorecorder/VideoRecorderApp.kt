package com.preonboarding.videorecorder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * @Created by 김현국 2022/10/17
 * @Time 6:39 PM
 */
@HiltAndroidApp
class VideoRecorderApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
