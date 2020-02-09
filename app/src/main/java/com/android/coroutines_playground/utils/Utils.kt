package com.android.coroutines_playground.utils

fun <T> lazyUi(block: () -> T): Lazy<T> =
    lazy(mode = LazyThreadSafetyMode.NONE, initializer = block)
