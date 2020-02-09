package com.android.coroutines_playground.presentation.categories

import com.android.coroutines_playground.domain.interactor.DetectiveRiddlesInteractor
import com.android.coroutines_playground.domain.model.entity.Category
import com.android.coroutines_playground.domain.model.result.CategoriesResult
import com.android.coroutines_playground.navigation.Screen
import com.android.coroutines_playground.presentation.base.BasePresenter
import kotlinx.coroutines.CompletableDeferred

class CategoriesPresenter(
    private val detectiveRiddlesInteractor: DetectiveRiddlesInteractor
) : BasePresenter<CategoriesView.ViewModel, CategoriesView.Event>() {

    override suspend fun handleEvent(event: CategoriesView.Event) {
        event.let {
            when (it) {
                is CategoriesView.Event.LoadData -> loadCategories()
                is CategoriesView.Event.OpenCategory -> openCategory(it.category)
            }
        }
    }

    private suspend fun loadCategories() {
        registerResult(CategoriesView.ViewModel(isLoading = true, data = null))
        val result = CompletableDeferred<CategoriesResult.Get>()
        detectiveRiddlesInteractor.getCategories(result)
        result.await().let {
            when (it) {
                is CategoriesResult.Get.List -> {
                    registerResult(CategoriesView.ViewModel(isLoading = false, data = it.result))
                }
                is CategoriesResult.Get.None ->
                    registerResult(CategoriesView.ViewModel(isLoading = false))
            }
        }
    }

    private fun openCategory(category: Category.CategoryItem) {
        globalRouter {
            navigateTo(
                Screen.RiddleListScreen(
                    Screen.RiddleListScreen.Data(
                        category.id,
                        category.title,
                        category.description,
                        category.imagePath
                    )
                )
            )
        }
    }
}