package com.android.coroutines_playground.presentation.riddlelist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.coroutines_playground.R
import com.android.coroutines_playground.navigation.Screen
import com.android.coroutines_playground.presentation.base.RecyclerStateFragment
import com.android.coroutines_playground.presentation.widgets.RecyclerItemDecorator
import com.android.coroutines_playground.utils.lazyUi
import com.android.coroutines_playground.utils.setToolbarTitleAutoScroll
import kotlinx.android.synthetic.main.fragment_riddles_list.*
import kotlinx.android.synthetic.main.header.*
import org.koin.android.architecture.ext.viewModel

class RiddleListFragment :
    RecyclerStateFragment<RiddleListView.ViewModel, RiddleListView.Event,
            RiddleListPresenter>() {

    companion object {
        private val EXTRA_DATA = RiddleListFragment::class.java.name + "extra.DATA"

        fun createInstance(data: Screen.RiddleListScreen.Data): RiddleListFragment =
            RiddleListFragment()
                .apply {
                    arguments = Bundle().apply {
                        putParcelable(EXTRA_DATA, data)
                    }
                }
    }

    override val recyclerStorageKey: String = RiddleListFragment::class.java.name

    private val data by lazyUi {
        arguments?.getParcelable(EXTRA_DATA)
            ?: Screen.RiddleListScreen.Data(-1, "", "", "")
    }

    override val resourceId: Int = R.layout.fragment_riddles_list
    override val presenter: RiddleListPresenter by viewModel()

    private lateinit var rAdapter: RiddleListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.categoryData = data

        rAdapter = RiddleListAdapter(context!!, data) {
            rvQuestions.layoutManager?.let { lm ->
                saveRecyclerState(lm)
            }
            registerEvent(RiddleListView.Event.OpenQuestion(it))
        }.apply {
            setHasStableIds(true)
        }

        rvQuestions.apply {
            adapter = rAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(
                RecyclerItemDecorator(
                    resources.getDimension(
                        R.dimen.list_item_spacing
                    ).toInt()
                )
            )
        }

        toolbar?.let {
            with(requireActivity() as AppCompatActivity) {
                setSupportActionBar(it)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
            it.title = data.title ?: resources.getString(R.string.app_name_title)
            it.setToolbarTitleAutoScroll()
        }
    }

    override fun onStart() {
        super.onStart()
        registerEvent(RiddleListView.Event.LoadQuestions)
    }

    override fun handleViewModel(model: RiddleListView.ViewModel?) {
        model?.let {
            it.list?.let {
                rAdapter.setListData(it)
                rvQuestions.layoutManager?.let { lm ->
                    restoreRecyclerState(lm)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanState()
    }
}