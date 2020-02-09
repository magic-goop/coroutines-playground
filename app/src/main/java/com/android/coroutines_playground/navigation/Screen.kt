package com.android.coroutines_playground.navigation

import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.android.coroutines_playground.presentation.categories.CategoriesFragment
import com.android.coroutines_playground.presentation.riddle.RiddleFragment
import com.android.coroutines_playground.presentation.riddlelist.RiddleListFragment
import kotlinx.android.parcel.Parcelize
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screen {
    class MainScreen : SupportAppScreen() {
        override fun getFragment(): Fragment = CategoriesFragment.createInstance()
    }

    class RiddleListScreen(private val data: Data) : SupportAppScreen() {
        @Parcelize
        data class Data(
            val id: Int,
            val title: String?,
            val description: String?,
            val image: String?
        ) : Parcelable

        override fun getFragment(): Fragment = RiddleListFragment.createInstance(data)
    }

    class RiddleScreen(private val data: Data) : SupportAppScreen() {
        @Parcelize
        data class Data(
            val id: Int,
            val categoryId: Int,
            val title: String?,
            val categoryTitle: String?,
            val description: String?
        ) : Parcelable

        override fun getFragment(): Fragment = RiddleFragment.createInstance(data)
    }
}