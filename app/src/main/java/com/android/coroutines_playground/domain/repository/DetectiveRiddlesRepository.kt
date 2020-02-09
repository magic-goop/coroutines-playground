package com.android.coroutines_playground.domain.repository

import com.android.coroutines_playground.domain.model.result.CategoriesResult
import com.android.coroutines_playground.domain.model.result.QuestionResult

interface DetectiveRiddlesRepository {

    suspend fun getCategories(): CategoriesResult.Get
    suspend fun getQuestions(categoryId: Int): QuestionResult.Get
    suspend fun getQuestion(questionId: Int): QuestionResult.GetOne
    suspend fun switchQuestionAnswer(questionId: Int, currentAnswer: Int): QuestionResult.GetOne
    suspend fun loadNextQuestion(currentQuestionId: Int, categoryId: Int): QuestionResult.GetOne
}