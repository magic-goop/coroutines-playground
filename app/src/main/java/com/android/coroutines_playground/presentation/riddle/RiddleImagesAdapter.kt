package com.android.coroutines_playground.presentation.riddle

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.coroutines_playground.GlideApp
import com.android.coroutines_playground.R
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.android.coroutines_playground.domain.model.entity.buildImgUrl
import com.android.coroutines_playground.presentation.base.RecyclerAdapter
import kotlinx.android.synthetic.main.list_item_riddle_image.view.*

class RiddleImagesAdapter(
    private val context: Context,
    private val listener: (position: Int) -> Unit
) : RecyclerAdapter<String, RiddleImagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_riddle_image, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = data[position]
        GlideApp
            .with(context)
            .load(Uri.parse(url.buildImgUrl()))
            .transition(DrawableTransitionOptions.withCrossFade())
            .fallback(R.mipmap.ic_launcher)
            .into(holder.img)
        holder.itemView.setOnClickListener {
            listener(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.imgQuestion
    }
}