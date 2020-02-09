package com.android.coroutines_playground.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.coroutines_playground.data.db.entity.detectiveriddles.CategoryEntity
import com.android.coroutines_playground.data.db.entity.detectiveriddles.QuestionEntity

@Dao
interface DetectiveRiddlesDao {

    @Query("SELECT id, title, description, image, PRIORITY, (SELECT count(Question.id) FROM Question WHERE Question.category_id = Category.id) AS total_count, (SELECT count(Question.id) FROM Question WHERE Question.category_id = Category.id and Question.is_answered = 1) AS answered FROM Category")
    fun getCategories(): List<CategoryEntity>

    @Query("SELECT * FROM ${QuestionEntity.TABLE_NAME} WHERE ${QuestionEntity.CATEGORY_ID} = :categoryId")
    fun getQuestions(categoryId: Int): List<QuestionEntity>

    @Query("SELECT * FROM ${QuestionEntity.TABLE_NAME} WHERE ${QuestionEntity.ID} = :id")
    fun getQuestion(id: Int): QuestionEntity

    @Query("UPDATE ${QuestionEntity.TABLE_NAME} set ${QuestionEntity.IS_ANSWERED} = :answer WHERE ${QuestionEntity.ID} = :id")
    fun switchQuestionAnswer(id: Int, answer: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestions(questions: List<QuestionEntity>)

    @Query("UPDATE ${QuestionEntity.TABLE_NAME} SET ${QuestionEntity.IS_ANSWERED} = 0")
    fun setAllQuestionsToNotAnswered()
}