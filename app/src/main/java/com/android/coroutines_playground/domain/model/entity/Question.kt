package com.android.coroutines_playground.domain.model.entity

data class Question(val rows: List<QuestionItem>) {
    data class QuestionItem(
        val id: Int = -1,
        val categoryId: Int = -1,
        val imagePath: List<String>? = null,
        val title: String? = "",
        var description: String? = "",
        val question: String? = "",
        val answer: String? = "",
        val isAnswered: Boolean = false
    )
}