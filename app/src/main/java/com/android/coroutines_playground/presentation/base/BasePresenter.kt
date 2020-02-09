package com.android.coroutines_playground.presentation.base

import androidx.lifecycle.ViewModel
import com.android.coroutines_playground.navigation.BaseRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber

abstract class BasePresenter<TViewModel, TEvent> : ViewModel(), KoinComponent {

    private val bgJob = SupervisorJob()
    private val bgContext = Dispatchers.IO + bgJob
    private val job = SupervisorJob()
    private val uiContext = Dispatchers.Main + job

    private val router: BaseRouter by inject()

    private var view: BaseView<TViewModel, TEvent>? = null

    @ObsoleteCoroutinesApi
    internal val eventChannel = CoroutineScope(bgContext).actor<TEvent>(capacity = Channel.UNLIMITED) {
        consumeEach { event ->
            try {
                Timber.d("[${this@BasePresenter.javaClass.simpleName}]:[Event] = $event")
                handleEvent(event)
            } catch (e: Exception) {
                Timber.e(e, "Unknown event.")
            }
        }
    }

    fun attachView(view: BaseView<TViewModel, TEvent>) {
        Timber.d("[${javaClass.simpleName}]: View is being attached")
        this.view = view
    }

    fun detachView() {
        Timber.d("[${javaClass.simpleName}]: View is being detached")
        this.view = null
    }

    protected abstract suspend fun handleEvent(event: TEvent)

    protected fun globalRouter(block: BaseRouter.() -> Unit) = CoroutineScope(uiContext).launch { router.block() }

    protected fun registerResult(model: TViewModel?) {
        CoroutineScope(bgContext).launch {
            view?.renderChannel?.offer(model)
        }
    }

    override fun onCleared() {
        bgJob.cancel()
        job.cancel()
        super.onCleared()
    }
}