package com.android.coroutines_playground.presentation.riddlelist

import com.android.coroutines_playground.domain.interactor.DetectiveRiddlesInteractor
import com.android.coroutines_playground.domain.model.entity.Question
import com.android.coroutines_playground.domain.model.result.QuestionResult
import com.android.coroutines_playground.navigation.Screen
import com.android.coroutines_playground.presentation.base.BasePresenter
import kotlinx.coroutines.CompletableDeferred

class RiddleListPresenter(
    private val riddlesInteractor: DetectiveRiddlesInteractor
) : BasePresenter<RiddleListView.ViewModel, RiddleListView.Event>() {

    companion object {
        private const val ELLIPSIS = "..."
        private const val MAX_DESCRIPTION_LENGTH = 100
    }

    override suspend fun handleEvent(event: RiddleListView.Event) {
        event.let {
            when (it) {
                is RiddleListView.Event.LoadQuestions -> loadQuestions()
                is RiddleListView.Event.OpenQuestion -> openQuestion(it.question)
            }
        }
    }

    lateinit var categoryData: Screen.RiddleListScreen.Data

    private suspend fun loadQuestions() {
        registerResult(RiddleListView.ViewModel(true, categoryData))
        val result = CompletableDeferred<QuestionResult.Get>()
        riddlesInteractor.getQuestions(categoryData.id, result)
        result.await().let {
            when (it) {
                is QuestionResult.Get.List -> {
                    val list = it.question.rows.toMutableList()
                    list.map {
                        if (it.description != null) {
                            it.description = it.description!!
                                    .substring(0, it.description!!.length.coerceAtMost(MAX_DESCRIPTION_LENGTH)) + ELLIPSIS }
                    }
                    categoryData.description?.let {
                        list.add(0, Question.QuestionItem())
                    }
                    registerResult(
                            RiddleListView.ViewModel(
                                    false,
                                    categoryData, list
                            )
                    )
                }
                is QuestionResult.Get.None ->
                    registerResult(RiddleListView.ViewModel(false, categoryData))
            }
        }
    }

    private fun openQuestion(q: Question.QuestionItem) {
        globalRouter {
            navigateTo(
                Screen.RiddleScreen(
                    Screen.RiddleScreen.Data(
                        q.id,
                        categoryData.id,
                        q.title,
                        categoryData.title,
                        q.description
                    )
                )
            )
        }
    }
}