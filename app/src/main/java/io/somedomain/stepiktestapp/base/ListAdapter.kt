package io.somedomain.stepiktestapp.base

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.somedomain.stepiktestapp.data.model.PageResponse

abstract class ListAdapter<T>(
        protected val data: MutableList<T>
) : RecyclerView.Adapter<ViewHolder<T>>() {

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        return onCreateViewHolder(LayoutInflater.from(parent.context), parent, viewType)
    }

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewHolder<T>

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(position, data[position])
    }

    public fun getDataList(): MutableList<T> {
        return data
    }

    var hasNext = true
    var page = 1

    fun update(data: PageResponse<MutableList<T>>) {
        hasNext = data.meta.hasNext
        page = data.meta.page
        val lastItem = this.data.size - 1
        this.data.addAll(data.data)
        if (lastItem > 0) {
            notifyItemInserted(lastItem)
        } else {
            notifyDataSetChanged()
        }
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    fun remove(item: T) {
        val index = data.indexOf(item)
        if (index < 0) return // throw NoSuchElementException()

        removeAt(index)
    }

    fun removeAt(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
    }

    fun notifyItemChanged(item: T) {
        val index = data.indexOf(item)
        notifyItemChanged(index)
    }
}

abstract class ViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(position: Int, data: T)
}