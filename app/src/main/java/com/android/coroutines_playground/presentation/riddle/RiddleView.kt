package com.android.coroutines_playground.presentation.riddle

import com.android.coroutines_playground.domain.model.entity.Question
import com.android.coroutines_playground.navigation.Screen
import com.android.coroutines_playground.presentation.base.BaseView

interface RiddleView : BaseView<RiddleView.ViewModel, RiddleView.Event> {

    data class ViewModel(
        val isLoading: Boolean = false,
        val questionItem: Question.QuestionItem? = null,
        val parent: Screen.RiddleScreen.Data? = null
    )

    sealed class Event {
        data class LoadQuestion(val id: Int) : Event()
        data class Answer(val id: Int, val currentlyIsAnswered: Boolean) : Event()
        data class NextQuestion(val currentQuestionId: Int, val categoryId: Int) : Event()
    }
}