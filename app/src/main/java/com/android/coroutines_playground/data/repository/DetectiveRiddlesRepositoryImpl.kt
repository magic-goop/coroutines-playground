package com.android.coroutines_playground.data.repository

import com.android.coroutines_playground.data.db.MainDB
import com.android.coroutines_playground.data.db.entity.mapper.CategoryEntityMapper
import com.android.coroutines_playground.data.db.entity.mapper.QuestionEntityMapper
import com.android.coroutines_playground.domain.model.entity.Category
import com.android.coroutines_playground.domain.model.entity.Question
import com.android.coroutines_playground.domain.model.result.CategoriesResult
import com.android.coroutines_playground.domain.model.result.QuestionResult
import com.android.coroutines_playground.domain.repository.DetectiveRiddlesRepository

class DetectiveRiddlesRepositoryImpl(
    private val mainDB: MainDB,
    private val categoriesMapper: CategoryEntityMapper,
    private val questionsMapper: QuestionEntityMapper
) : DetectiveRiddlesRepository {

    override suspend fun getQuestions(categoryId: Int): QuestionResult.Get =
            try {
                val questions = mainDB.detectiveRiddlesDao()
                        .getQuestions(categoryId)
                        .sortedWith(compareBy({ it.isAnswered }, { it.id }))
                        .map { questionsMapper.map(it) }
                if (questions.isEmpty()) {
                    QuestionResult.Get.None
                } else {
                    QuestionResult.Get.List(Question(questions))
                }
            } catch (ex: Exception) {
                QuestionResult.Get.Failure(ex)
            }.let {
                it
            }

    override suspend fun getQuestion(questionId: Int): QuestionResult.GetOne =
            try {
                val question = mainDB.detectiveRiddlesDao()
                        .getQuestion(questionId)
                QuestionResult.GetOne.Data(questionsMapper.map(question))
            } catch (ex: Exception) {
                QuestionResult.GetOne.Failure(ex)
            }.let {
                it
            }

    override suspend fun switchQuestionAnswer(questionId: Int, currentAnswer: Int): QuestionResult.GetOne =
            try {
                mainDB.detectiveRiddlesDao().switchQuestionAnswer(questionId, 1 - currentAnswer)
                val question = mainDB.detectiveRiddlesDao()
                        .getQuestion(questionId)
                QuestionResult.GetOne.Data(questionsMapper.map(question))
            } catch (ex: Exception) {
                QuestionResult.GetOne.Failure(ex)
            }.let {
                it
            }

    override suspend fun loadNextQuestion(currentQuestionId: Int, categoryId: Int): QuestionResult.GetOne =
            try {
                val questions = mainDB.detectiveRiddlesDao()
                        .getQuestions(categoryId)
                        .sortedWith(compareBy({ it.isAnswered }, { it.id }))
                        .map { questionsMapper.map(it) }
                if (questions.isEmpty()) {
                    QuestionResult.GetOne.None
                } else {
                    var q = questions[0]
                    if (q.isAnswered) { // it means that all questions in this category has been answered
                        for (i in questions.indices) {
                            if (questions[i].id == currentQuestionId) {
                                q = questions[if (i + 1 == questions.size) 0 else i + 1]
                                break
                            }
                        }
                    }
                    QuestionResult.GetOne.Data(q)
                }
            } catch (ex: Exception) {
                QuestionResult.GetOne.Failure(ex)
            }.let {
                it
            }

    override suspend fun getCategories(): CategoriesResult.Get =
            try {
                val cat = mainDB.detectiveRiddlesDao()
                        .getCategories()
                        .sortedBy { it.priority }
                        .map { categoriesMapper.map(it) }

                if (cat.isEmpty()) {
                    CategoriesResult.Get.None
                } else {
                    CategoriesResult.Get.List(Category(cat))
                }
            } catch (ex: Exception) {
                CategoriesResult.Get.Failure(ex)
            }.let {
                it
            }
}