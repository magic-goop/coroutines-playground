package com.android.coroutines_playground.presentation.riddlelist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.coroutines_playground.R
import com.android.coroutines_playground.domain.model.entity.Question
import com.android.coroutines_playground.navigation.Screen
import com.android.coroutines_playground.presentation.base.RecyclerAdapter
import com.android.coroutines_playground.utils.isVisible
import com.android.coroutines_playground.utils.lazyUi
import kotlinx.android.synthetic.main.list_item_riddle_header.view.*
import kotlinx.android.synthetic.main.list_item_riddle_list.view.*

class RiddleListAdapter(
    private val context: Context,
    private val categoryData: Screen.RiddleListScreen.Data,
    private val listener: (Question.QuestionItem) -> Unit
) : RecyclerAdapter<Question.QuestionItem, RecyclerView.ViewHolder>() {

    companion object {
        private const val HEADER = 0
        private const val ITEM = 1
    }

    private val defaultDescriptionMaxLines: Int by lazyUi {
        context.resources.getInteger(R.integer.riddle_list_fragment_description_default_lines)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_riddle_header, parent, false).let { view ->
                    ViewHolderHeader(view).apply {
                        view.setOnClickListener {
                            val maxLines = description.maxLines
                            ellipsis.isVisible(maxLines != defaultDescriptionMaxLines)
                            description.maxLines =
                                if (maxLines < Integer.MAX_VALUE)
                                    Integer.MAX_VALUE else defaultDescriptionMaxLines
                        }
                    }
                }
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_riddle_list, parent, false).let { view ->
                    ViewHolder(view).apply {
                        view.setOnClickListener { listener(data[adapterPosition]) }
                    }
                }
        }
    }

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (getItemViewType(position)) {
            HEADER -> {
                with(h as ViewHolderHeader) {
                    description.text = categoryData.description?.trim() ?: ""
                    description.isVisible(description.text.isNotEmpty())
                }
            }
            ITEM -> {
                with(h as ViewHolder) {
                    title.text = item.title?.trim() ?: ""
                    description.text = item.description?.trim() ?: ""
                    imgAnswered.isVisible(item.isAnswered)
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return if (position >= itemCount) -1L else data[position].id.toLong()
    }

    class ViewHolderHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.tvHeaderTitle
        val description: TextView = itemView.tvHeaderDescription
        val ellipsis: View = itemView.tvShowFullDescription
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.tvTitle
        val description: TextView = itemView.tvDescription
        val imgAnswered: ImageView = itemView.imgAnswered
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 &&
            categoryData.description != null &&
            categoryData.description.trim().isNotEmpty()
        ) 0 else 1
    }
}