package sathya.com.movieblipp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CoreContext : Application() {

    override fun onCreate() {
        super.onCreate()

    }

}