package com.android.coroutines_playground.presentation.riddle

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.android.coroutines_playground.R
import com.android.coroutines_playground.navigation.Screen
import com.android.coroutines_playground.presentation.base.BaseFragment
import com.android.coroutines_playground.presentation.riddle.imageviewer.ImageViewerDialog
import com.android.coroutines_playground.utils.isVisible
import com.android.coroutines_playground.utils.lazyUi
import com.android.coroutines_playground.utils.setToolbarTitleAutoScroll
import kotlinx.android.synthetic.main.fragment_riddle.*
import kotlinx.android.synthetic.main.header.*
import org.koin.android.architecture.ext.viewModel
import timber.log.Timber

class RiddleFragment : BaseFragment<RiddleView.ViewModel, RiddleView.Event, RiddlePresenter>() {

    companion object {
        private val EXTRA_DATA = RiddleFragment::class.java.name + "extra.DATA"

        fun createInstance(data: Screen.RiddleScreen.Data): RiddleFragment = RiddleFragment()
            .apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_DATA, data)
                }
            }

        private const val SCROLL_DELAY = 10L
    }

    private val SHOW_ANSWER_TEXT by lazyUi { resources.getString(R.string.show_answer_text) }
    private val HIDE_ANSWER_TEXT by lazyUi { resources.getString(R.string.hide_answer_text) }

    private val data by lazyUi {
        arguments?.getParcelable(EXTRA_DATA)
            ?: Screen.RiddleScreen.Data(-1, -1, "", "", "")
    }

    override val resourceId: Int = R.layout.fragment_riddle
    override val presenter: RiddlePresenter by viewModel()

    private val rvAdapter by lazyUi {
        RiddleImagesAdapter(requireContext()) {
            openImages(it)
        }
    }

    private var shouldScrollDown = false
    private var shouldScrollTop = false

    private val imagesUrl: ArrayList<String> = ArrayList()
    private var currentQuestionId: Int = -1

    override fun handleViewModel(model: RiddleView.ViewModel?) {

        model?.let {
            if (it.isLoading) {
                return
            }
            it.questionItem?.let { q ->
                toolbar?.title = q.title?.trim() ?: resources.getString(R.string.app_name_title)
                tvQuestionText.text = q.description?.trim()
                if (q.question != null && q.question.isNotEmpty()) {
                    tvQuestionItself.isVisible(true)
                    tvQuestionItselfDivider.isVisible(true)
                    tvQuestionItself.text = q.question.trim()
                } else {
                    tvQuestionItself.isVisible(false)
                    tvQuestionItselfDivider.isVisible(false)
                }

                rvImages.isVisible(q.imagePath != null && q.imagePath.isNotEmpty())
                if (q.imagePath != null && q.imagePath.isNotEmpty()) {
                    rvImages.scrollToPosition(0)
                    val imagesSize = q.imagePath.size
                    rvAdapter.setListData(q.imagePath)
                    indicator.isVisible(imagesSize > 1)
                    indicator.setDotCount(imagesSize)
                    imagesUrl.clear()
                    imagesUrl.addAll(q.imagePath)
                }
                val isAnswered = q.isAnswered
                val id = q.id
                btnAnswer.text = if (isAnswered) HIDE_ANSWER_TEXT else SHOW_ANSWER_TEXT
                btnAnswer.setOnClickListener {
                    registerEvent(RiddleView.Event.Answer(id, isAnswered))
                    shouldScrollDown = !isAnswered
                }
                tvAnswer.isVisible(q.answer != null && isAnswered)
                tvAnswer.text = q.answer
                if (shouldScrollDown and isAnswered) {
                    scrollContent(View.FOCUS_DOWN)
                }
                shouldScrollDown = false

                btnNextQuestion.isVisible(isAnswered)
                btnNextQuestion.setOnClickListener {
                    registerEvent(RiddleView.Event.NextQuestion(currentQuestionId, data.categoryId))
                    shouldScrollTop = true
                }
                if (shouldScrollTop) {
                    scrollContent(View.FOCUS_UP)
                }
                shouldScrollTop = false
                currentQuestionId = q.id
            }
        }
    }

    private fun scrollContent(direction: Int) {
        Handler().postDelayed({
            try {
                scrollRiddle.fullScroll(direction)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }, SCROLL_DELAY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.parentData = data

        with(rvImages) {
            adapter = rvAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            PagerSnapHelper().attachToRecyclerView(this)
            indicator.attachToRecyclerView(this)
        }

        toolbar?.let {
            with(requireActivity() as AppCompatActivity) {
                setSupportActionBar(it)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
            it.title = data.title ?: resources.getString(R.string.app_name_title)
            it.setToolbarTitleAutoScroll()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun openImages(position: Int) {
        childFragmentManager.findFragmentByTag(ImageViewerDialog.TAG)
            ?: ImageViewerDialog.createDialog(position, imagesUrl) {
                rvImages.scrollToPosition(it)
            }.let {
                childFragmentManager.beginTransaction().add(it, ImageViewerDialog.TAG)
                    .commitNowAllowingStateLoss()
            }
    }

    override fun onStart() {
        super.onStart()
        registerEvent(RiddleView.Event.LoadQuestion(data.id))
    }
}