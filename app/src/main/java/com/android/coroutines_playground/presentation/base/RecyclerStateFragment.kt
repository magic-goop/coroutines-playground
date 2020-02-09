package com.android.coroutines_playground.presentation.base

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.android.coroutines_playground.utils.RecyclerStateStorage
import org.koin.android.ext.android.inject

abstract class RecyclerStateFragment<TViewModel, TViewEvent, out TPresenter : BasePresenter<TViewModel, TViewEvent>> :
    BaseFragment<TViewModel, TViewEvent, TPresenter>() {

    protected abstract val recyclerStorageKey: String

    private val recycleStateStorage: RecyclerStateStorage by inject()

    protected fun saveRecyclerState(lm: RecyclerView.LayoutManager) {
        val b = Bundle()
        b.putParcelable(RecyclerStateStorage.STATE_EXTRA, lm.onSaveInstanceState())
        recycleStateStorage.saveState(recyclerStorageKey, b)
    }

    protected fun restoreRecyclerState(lm: RecyclerView.LayoutManager) {
        val b = recycleStateStorage.getState(recyclerStorageKey)
        b?.let {
            lm.onRestoreInstanceState(b.getParcelable(RecyclerStateStorage.STATE_EXTRA))
        }
    }

    protected fun cleanState() {
        recycleStateStorage.cleanState(recyclerStorageKey)
    }
}