package io.somedomain.stepiktestapp.base

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.somedomain.stepiktestapp.R
import io.somedomain.stepiktestapp.common.EndlessRecyclerViewScrollListener

abstract class ListFragment<V : BaseListView<T>, out P : BasePresenter<V>, T> : BaseFragment<V, P>() {

    protected lateinit var recyclerView: RecyclerView
    protected lateinit var adapter: ListAdapter<T>
    protected lateinit var emptyView: View
    protected lateinit var emptyViewText: TextView

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.list, container, false)
    }

    override fun findViews(root: View) {
        super.findViews(root)
        recyclerView = root.findViewById(R.id.list)
        emptyView = root.findViewById(R.id.empty_view)
        emptyViewText = root.findViewById(R.id.empty_text)
    }

    override fun initViews() {
        super.initViews()
        adapter = onCreateListAdapter()
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (adapter.hasNext) {
                    onLoadingMore(page + 1)
                }
            }
        })
    }

    open protected fun onLoadingMore(page: Int) {

    }

    fun showEmptyView(show: Boolean) {
        emptyView.visibility = if (!show) View.VISIBLE else View.GONE
    }

    abstract fun onCreateListAdapter(): ListAdapter<T>
}