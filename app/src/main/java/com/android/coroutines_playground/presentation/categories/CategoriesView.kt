package com.android.coroutines_playground.presentation.categories

import com.android.coroutines_playground.domain.model.entity.Category
import com.android.coroutines_playground.presentation.base.BaseView

interface CategoriesView : BaseView<CategoriesView.ViewModel, CategoriesView.Event> {

    data class ViewModel(
        val isLoading: Boolean = false,
        val data: Category? = null
    )

    sealed class Event {
        object LoadData : Event()
        class OpenCategory(val category: Category.CategoryItem) : Event()
    }
}