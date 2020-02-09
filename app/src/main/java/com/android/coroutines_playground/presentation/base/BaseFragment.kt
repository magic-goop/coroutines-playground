package com.android.coroutines_playground.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<TViewModel, TViewEvent, out TPresenter : BasePresenter<TViewModel, TViewEvent>> :
    Fragment(), BaseView<TViewModel, TViewEvent>, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    protected abstract val resourceId: Int
    protected abstract val presenter: TPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(resourceId, container, false)

    fun registerEvent(event: TViewEvent) {
        if (presenter.eventChannel.isClosedForSend) {
            Timber.d("Event channel is closed for send.")
        } else {
            Timber.d("Send event to presenter")
            presenter.eventChannel.offer(event)
        }
    }

    override val renderChannel: SendChannel<TViewModel?> = actor(capacity = Channel.UNLIMITED) {
        consumeEach {
            try {
                Timber.d("Render response model")
                handleViewModel(it)
            } catch (ex: Throwable) {
                Timber.e(
                    ex,
                    "[${this@BaseFragment.javaClass.simpleName}]: An exception caught during render."
                )
            }
        }
    }

    override fun onStart() {
        presenter.attachView(this)
        super.onStart()
    }

    override fun onStop() {
        presenter.detachView()
        super.onStop()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    protected abstract fun handleViewModel(model: TViewModel?)
}