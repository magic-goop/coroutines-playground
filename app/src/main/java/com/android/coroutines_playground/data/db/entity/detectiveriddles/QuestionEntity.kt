package com.android.coroutines_playground.data.db.entity.detectiveriddles

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = QuestionEntity.TABLE_NAME)
data class QuestionEntity(
    @PrimaryKey
    @ColumnInfo(name = ID) var id: Int,
    @ColumnInfo(name = CATEGORY_ID) var categoryId: Int,
    @ColumnInfo(name = TITLE) var title: String?,
    @ColumnInfo(name = DESCRIPTION) var description: String?,
    @ColumnInfo(name = IMAGE) var image: String?,
    @ColumnInfo(name = QUESTION) var question: String?,
    @ColumnInfo(name = ANSWER) var answer: String?,
    @ColumnInfo(name = IS_ANSWERED) var isAnswered: Int?
) {
    companion object {
        const val TABLE_NAME = "Question"
        const val ID = "id"
        const val CATEGORY_ID = "category_id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val IMAGE = "image"
        const val QUESTION = "question"
        const val ANSWER = "answer"
        const val IS_ANSWERED = "is_answered"
    }
}