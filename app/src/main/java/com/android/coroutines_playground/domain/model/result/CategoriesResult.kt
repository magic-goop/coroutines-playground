package com.android.coroutines_playground.domain.model.result

import com.android.coroutines_playground.domain.model.entity.Category

sealed class CategoriesResult {
    sealed class Get : CategoriesResult() {
        class List(val result: Category) : Get()
        class Failure(val ex: Throwable) : Get()
        object None : Get()
    }
}