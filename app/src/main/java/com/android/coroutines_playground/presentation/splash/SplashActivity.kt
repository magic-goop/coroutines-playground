package com.android.coroutines_playground.presentation.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.coroutines_playground.presentation.activitycontainer.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }
}