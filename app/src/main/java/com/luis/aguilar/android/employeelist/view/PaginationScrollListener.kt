package com.luis.aguilar.android.employeelist.view

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class PaginationScrollListener(
    val staggeredGridLayoutManager: StaggeredGridLayoutManager
) : RecyclerView.OnScrollListener() {

    private val lastVisiblePositionsArray: IntArray = IntArray(staggeredGridLayoutManager.spanCount)

    override fun onScrolled(
        recyclerView: RecyclerView,
        dx: Int,
        dy: Int
    ) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount: Int = staggeredGridLayoutManager.childCount
        val totalItemCount: Int = staggeredGridLayoutManager.itemCount
        staggeredGridLayoutManager.findLastVisibleItemPositions(lastVisiblePositionsArray)
        val lastVisibleItemPosition: Int = lastVisiblePositionsArray.maxOrNull() ?: 0

        if (!isLoading()) {
            if (visibleItemCount + lastVisibleItemPosition >= totalItemCount && lastVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()

    abstract fun isLoading(): Boolean
}