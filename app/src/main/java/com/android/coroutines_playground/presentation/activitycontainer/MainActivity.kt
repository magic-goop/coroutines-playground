package com.android.coroutines_playground.presentation.activitycontainer

import android.content.Context
import android.content.Intent
import com.android.coroutines_playground.R
import com.android.coroutines_playground.navigation.Screen
import com.android.coroutines_playground.presentation.base.BaseActivity

class MainActivity : BaseActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    override val fragmentContainer: Int = R.id.container
    override val resourceId: Int = R.layout.activity_main
    override val startFragment = Screen.MainScreen()
}