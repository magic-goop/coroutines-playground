package com.android.coroutines_playground.presentation.base

import kotlinx.coroutines.channels.SendChannel

interface BaseView<in TViewModel, out TViewEvent> {
    val renderChannel: SendChannel<TViewModel?>
}