package com.android.coroutines_playground.presentation.categories

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.coroutines_playground.R
import com.android.coroutines_playground.presentation.base.RecyclerStateFragment
import com.android.coroutines_playground.presentation.widgets.RecyclerItemDecorator
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.header.*
import org.koin.android.architecture.ext.viewModel

class CategoriesFragment :
    RecyclerStateFragment<CategoriesView.ViewModel, CategoriesView.Event, CategoriesPresenter>() {

    companion object {
        fun createInstance() = CategoriesFragment()
    }

    override val recyclerStorageKey: String = CategoriesFragment::class.java.name

    override val presenter: CategoriesPresenter by viewModel()

    override val resourceId: Int = R.layout.fragment_main

    private lateinit var rvAdapter: CategoriesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        rvAdapter = CategoriesAdapter(context!!) {
            rvCategories.layoutManager?.let { lm ->
                saveRecyclerState(lm)
            }
            registerEvent(CategoriesView.Event.OpenCategory(it))
        }
        with(rvCategories) {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(
                RecyclerItemDecorator(resources.getDimension(R.dimen.list_item_spacing).toInt())
            )
        }
        toolbar?.let {
            (activity as AppCompatActivity).setSupportActionBar(it)
            it.title = resources.getString(R.string.app_name_title)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun handleViewModel(model: CategoriesView.ViewModel?) {
        model?.let {
            it.data?.let { data ->
                rvAdapter.setListData(data.rows)
                rvCategories.layoutManager?.let { lm ->
                    restoreRecyclerState(lm)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerEvent(CategoriesView.Event.LoadData)
    }
}