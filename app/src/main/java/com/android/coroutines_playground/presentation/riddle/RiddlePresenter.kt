package com.android.coroutines_playground.presentation.riddle

import com.android.coroutines_playground.domain.interactor.DetectiveRiddlesInteractor
import com.android.coroutines_playground.domain.model.entity.Question
import com.android.coroutines_playground.domain.model.result.QuestionResult
import com.android.coroutines_playground.navigation.Screen
import com.android.coroutines_playground.presentation.base.BasePresenter
import kotlinx.coroutines.CompletableDeferred

class RiddlePresenter(private val detectiveRiddlesInteractor: DetectiveRiddlesInteractor) :
    BasePresenter<RiddleView.ViewModel, RiddleView.Event>() {

    lateinit var parentData: Screen.RiddleScreen.Data
    private var questionItem: Question.QuestionItem? = null

    override suspend fun handleEvent(event: RiddleView.Event) {
        when (event) {
            is RiddleView.Event.LoadQuestion -> loadQuestion(event.id)
            is RiddleView.Event.Answer -> switchAnswerStatus(event.id, event.currentlyIsAnswered)
            is RiddleView.Event.NextQuestion ->
                loadNextQuestion(event.currentQuestionId, event.categoryId)
        }
    }

    private suspend fun loadQuestion(id: Int) {
        registerResult(RiddleView.ViewModel(true, questionItem, parentData))
        val result = CompletableDeferred<QuestionResult.GetOne>()
        detectiveRiddlesInteractor.getQuestion(id, result)
        processQuestion(result)
    }

    private suspend fun switchAnswerStatus(questionId: Int, currentlyIsAnswered: Boolean) {
        registerResult(RiddleView.ViewModel(true, questionItem, parentData))
        val result = CompletableDeferred<QuestionResult.GetOne>()
        detectiveRiddlesInteractor.switchQuestionAnswer(
            questionId,
            if (currentlyIsAnswered) 1 else 0,
            result
        )
        processQuestion(result)
    }

    private suspend fun loadNextQuestion(currentQuestionId: Int, categoryId: Int) {
        registerResult(RiddleView.ViewModel(true, questionItem, parentData))
        val result = CompletableDeferred<QuestionResult.GetOne>()
        detectiveRiddlesInteractor.loadNextQuestion(currentQuestionId, categoryId, result)
        processQuestion(result)
    }

    private suspend fun processQuestion(question: CompletableDeferred<QuestionResult.GetOne>) {
        question.await().let {
            when (it) {
                is QuestionResult.GetOne.Failure ->
                    registerResult(RiddleView.ViewModel(false, questionItem, parentData))
                is QuestionResult.GetOne.None ->
                    registerResult(RiddleView.ViewModel(false, questionItem, parentData))
                is QuestionResult.GetOne.Data -> {
                    questionItem = it.some
                    registerResult(RiddleView.ViewModel(false, questionItem, parentData))
                }
            }
        }
    }
}