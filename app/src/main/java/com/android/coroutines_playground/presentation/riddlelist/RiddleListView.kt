package com.android.coroutines_playground.presentation.riddlelist

import com.android.coroutines_playground.domain.model.entity.Question
import com.android.coroutines_playground.navigation.Screen
import com.android.coroutines_playground.presentation.base.BaseView

interface RiddleListView : BaseView<RiddleListView.ViewModel, RiddleListView.Event> {

    data class ViewModel(
        val isLoading: Boolean = false,
        val category: Screen.RiddleListScreen.Data? = null,
        val list: List<Question.QuestionItem>? = null
    )

    sealed class Event {
        class OpenQuestion(val question: Question.QuestionItem) : Event()
        object LoadQuestions : Event()
    }
}