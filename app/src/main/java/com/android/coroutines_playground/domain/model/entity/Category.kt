package com.android.coroutines_playground.domain.model.entity

data class Category(val rows: List<CategoryItem>) {
    data class CategoryItem(
        val id: Int,
        val title: String?,
        var description: String?,
        var descriptionShortened: String?,
        val imagePath: String?,
        val answered: Int = 0,
        val total: Int = 0
    )
}

fun String?.buildImgUrl(): String {
    return this?.let { "file:///android_asset/images/$this" } ?: ""
}
