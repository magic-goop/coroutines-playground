package com.android.coroutines_playground.presentation.categories

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.coroutines_playground.GlideApp
import com.android.coroutines_playground.R
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.android.coroutines_playground.domain.model.entity.Category
import com.android.coroutines_playground.domain.model.entity.buildImgUrl
import com.android.coroutines_playground.presentation.base.RecyclerAdapter
import com.android.coroutines_playground.utils.isVisible
import com.android.coroutines_playground.utils.lazyUi
import kotlinx.android.synthetic.main.list_item_main.view.*

class CategoriesAdapter(
    private val context: Context,
    private val listener: (Category.CategoryItem) -> Unit
) : RecyclerAdapter<Category.CategoryItem, CategoriesAdapter.ViewHolder>() {

    private val ALL_ANSWERED: Drawable? by lazyUi {
        ContextCompat.getDrawable(context, R.drawable.ic_check)
    }

    private val NOT_ALL_ANSWERED: Drawable? by lazyUi {
        ContextCompat.getDrawable(context, R.drawable.ic_check_grey)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.itemView.setOnClickListener { listener(item) }
        with(holder) {
            title.text = item.title?.trim() ?: ""
            description.text = item.descriptionShortened?.trim() ?: ""
            description.isVisible(!description.text.isEmpty())
            answered.text = item.answered.toString()
            notAnswered.text = (item.total - item.answered).toString()

            imgAnswered.setImageDrawable(
                if (item.total - item.answered == 0)
                    ALL_ANSWERED else NOT_ALL_ANSWERED
            )

            item.imagePath?.let {
                GlideApp
                    .with(context)
                    .load(Uri.parse(item.imagePath.buildImgUrl()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .fallback(R.mipmap.ic_launcher)
                    .into(image)
            } ?: GlideApp.with(context).load(R.mipmap.ic_launcher).into(image)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.tvTitle
        val description: TextView = itemView.tvDescription
        val image: ImageView = itemView.imgFront
        val answered: TextView = itemView.tvSolved
        val notAnswered: TextView = itemView.tvUnSolved
        val imgAnswered: ImageView = itemView.imgAnswered
    }
}