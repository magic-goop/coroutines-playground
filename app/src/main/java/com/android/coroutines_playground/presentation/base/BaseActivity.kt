package com.android.coroutines_playground.presentation.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.android.coroutines_playground.navigation.BaseNavigator
import com.android.coroutines_playground.navigation.BaseRouter
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppScreen

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var baseNavigator: BaseNavigator
    private val baseNavigationHolder: NavigatorHolder by inject()
    private val baseRouter: BaseRouter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(resourceId)

        baseNavigator = BaseNavigator(this, fragmentContainer)

        savedInstanceState ?: run { baseRouter.newRootScreen(startFragment) }
    }

    protected abstract val fragmentContainer: Int

    protected abstract val resourceId: Int

    protected abstract val startFragment: SupportAppScreen

    override fun onResumeFragments() {
        super.onResumeFragments()
        baseNavigationHolder.setNavigator(baseNavigator)
    }

    override fun onPause() {
        baseNavigationHolder.removeNavigator()
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        else -> super.onOptionsItemSelected(item)
    }
}