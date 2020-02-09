package com.android.coroutines_playground.domain.interactor

import com.android.coroutines_playground.domain.model.result.CategoriesResult
import com.android.coroutines_playground.domain.model.result.QuestionResult
import com.android.coroutines_playground.domain.repository.DetectiveRiddlesRepository
import kotlinx.coroutines.CompletableDeferred

class DetectiveRiddlesInteractor(
    private val detectiveRiddlesRepository: DetectiveRiddlesRepository
) {

    suspend fun getCategories(c: CompletableDeferred<CategoriesResult.Get>) {
        try {
            c.complete(detectiveRiddlesRepository.getCategories())
        } catch (e: Exception) {
            c.completeExceptionally(e)
        }
    }

    suspend fun getQuestions(categoryId: Int, c: CompletableDeferred<QuestionResult.Get>) {
        try {
            c.complete(detectiveRiddlesRepository.getQuestions(categoryId))
        } catch (e: Exception) {
            c.completeExceptionally(e)
        }
    }

    suspend fun getQuestion(questionId: Int, c: CompletableDeferred<QuestionResult.GetOne>) {
        try {
            c.complete(detectiveRiddlesRepository.getQuestion(questionId))
        } catch (e: Exception) {
            c.completeExceptionally(e)
        }
    }

    suspend fun switchQuestionAnswer(
        questionId: Int,
        currentAnswer: Int,
        c: CompletableDeferred<QuestionResult.GetOne>
    ) {
        try {
            c.complete(detectiveRiddlesRepository.switchQuestionAnswer(questionId, currentAnswer))
        } catch (e: Exception) {
            c.completeExceptionally(e)
        }
    }

    suspend fun loadNextQuestion(
        currentQuestionId: Int,
        categoryId: Int,
        c: CompletableDeferred<QuestionResult.GetOne>
    ) {
        try {
            c.complete(detectiveRiddlesRepository.loadNextQuestion(currentQuestionId, categoryId))
        } catch (e: Exception) {
            c.completeExceptionally(e)
        }
    }
}