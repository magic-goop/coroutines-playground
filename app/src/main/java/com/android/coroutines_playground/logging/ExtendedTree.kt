package com.android.coroutines_playground.logging

import timber.log.Timber

open class ExtendedTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return String.format("%s::%s::%s",
                super.createStackElementTag(element),
                element.methodName,
                element.lineNumber)
    }
}
