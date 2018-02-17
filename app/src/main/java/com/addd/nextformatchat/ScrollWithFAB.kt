package com.addd.nextformatchat

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class ScrollWithFAB(internal var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0 ||dy<0 && isShown())
        {
            fabHide()
        }

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE)
        {
            fabShow()
        }
        super.onScrollStateChanged(recyclerView, newState)
    }

    protected abstract fun loadMoreItems()

    protected abstract fun fabHide()

    protected abstract fun fabShow()

    abstract fun getTotalPageCount(): Int

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

    abstract fun isShown(): Boolean
}