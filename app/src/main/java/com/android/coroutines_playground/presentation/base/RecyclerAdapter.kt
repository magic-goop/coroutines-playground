package com.android.coroutines_playground.presentation.base

import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerAdapter<Model, ViewHolder : RecyclerView.ViewHolder> : RecyclerView.Adapter<ViewHolder>() {

    protected var data: List<Model> = emptyList()

    override fun getItemCount() = data.size

    open fun setListData(list: List<Model>) {
        if (data == list) return

        data = list
        notifyDataSetChanged()
    }

    override fun onFailedToRecycleView(holder: ViewHolder): Boolean = true
}