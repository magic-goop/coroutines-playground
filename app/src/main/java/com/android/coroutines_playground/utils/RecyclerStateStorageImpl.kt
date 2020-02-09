package com.android.coroutines_playground.utils

import android.os.Bundle
import androidx.annotation.UiThread

class RecyclerStateStorageImpl : RecyclerStateStorage {

    private val storage: HashMap<String, Bundle> = hashMapOf()

    @UiThread
    override fun saveState(key: String, state: Bundle) {
        storage[key] = state
    }

    @UiThread
    override fun getState(key: String): Bundle? =
        if (storage.containsKey(key)) {
            storage[key]
        } else {
            null
        }

    @UiThread
    override fun cleanState(key: String) {
        storage.remove(key)
    }
}