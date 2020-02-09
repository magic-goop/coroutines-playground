package com.android.coroutines_playground.utils

import android.os.Bundle

interface RecyclerStateStorage {
    companion object {
        val STATE_EXTRA: String = RecyclerStateStorage::class.java.name + "extra.STATE"
    }

    fun saveState(key: String, state: Bundle)
    fun getState(key: String): Bundle?
    fun cleanState(key: String)
}