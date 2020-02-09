package com.android.coroutines_playground

import android.app.Application
import com.android.coroutines_playground.di.modules
import com.android.coroutines_playground.logging.ExtendedTree
import com.facebook.stetho.Stetho
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class TheApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, modules)

        initLogger()

        initStetho()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(ExtendedTree())
        }
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this.applicationContext)
        }
    }
}