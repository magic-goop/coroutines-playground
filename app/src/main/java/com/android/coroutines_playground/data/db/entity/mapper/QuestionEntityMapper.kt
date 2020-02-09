package com.android.coroutines_playground.data.db.entity.mapper

import com.android.coroutines_playground.data.db.entity.detectiveriddles.QuestionEntity
import com.android.coroutines_playground.domain.model.entity.Question

class QuestionEntityMapper : Mapper<QuestionEntity,
        Question.QuestionItem> {
    override fun map(t: QuestionEntity) =
            Question.QuestionItem(
                    id = t.id,
                    categoryId = t.categoryId,
                    title = t.title,
                    description = t.description,
                    imagePath = parseImages(t.image),
                    answer = t.answer,
                    question = t.question,
                    isAnswered = t.isAnswered == 1
            )

    private fun parseImages(images: String?): List<String> {
        return if (!images.isNullOrEmpty()) {
            images.split(";")
        } else {
            listOf()
        }
    }
}