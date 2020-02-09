package com.android.coroutines_playground

import android.app.Application
import com.android.coroutines_playground.di.modules
import org.koin.android.ext.android.startKoin

class TheApplicationTest : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, modules)
    }
}