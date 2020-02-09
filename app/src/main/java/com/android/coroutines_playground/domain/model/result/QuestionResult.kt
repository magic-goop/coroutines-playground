package com.android.coroutines_playground.domain.model.result

import com.android.coroutines_playground.domain.model.entity.Question

sealed class QuestionResult {
    sealed class Get : QuestionResult() {
        class List(val question: Question) : Get()
        class Failure(val ex: Throwable) : Get()
        object None : Get()
    }

    sealed class GetOne : QuestionResult() {
        class Data(val some: Question.QuestionItem) : GetOne()
        class Failure(val ex: Throwable) : GetOne()
        object None : GetOne()
    }
}