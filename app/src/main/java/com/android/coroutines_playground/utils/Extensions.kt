package com.android.coroutines_playground.utils

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

fun View.isVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun Toolbar.setToolbarTitleAutoScroll() {
    for (i in 0 until childCount) {
        val view = getChildAt(i)
        if (view is TextView && view.text == title) {
            with(view) {
                isFocusable = true
                isFocusableInTouchMode = true
                canScrollHorizontally(1)
                ellipsize = TextUtils.TruncateAt.MARQUEE
                marqueeRepeatLimit = -1
                isSelected = true
            }
            return
        }
    }
}