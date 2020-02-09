package com.android.coroutines_playground.data.db.entity.mapper

import com.android.coroutines_playground.data.db.entity.detectiveriddles.CategoryEntity
import com.android.coroutines_playground.domain.model.entity.Category

class CategoryEntityMapper : Mapper<CategoryEntity,
        Category.CategoryItem> {

    companion object {
        private const val MAX_DESCRIPTION_VALUE = 100
    }

    override fun map(t: CategoryEntity) = Category
            .CategoryItem(
                    id = t.id,
                    title = t.title,
                    description = t.description,
                    descriptionShortened = t.description?.substring(0, Math.min(t.description!!.length, MAX_DESCRIPTION_VALUE)),
                    imagePath = t.image?.let { "clean_$it" },
                    total = t.total,
                    answered = t.answered

            )
}