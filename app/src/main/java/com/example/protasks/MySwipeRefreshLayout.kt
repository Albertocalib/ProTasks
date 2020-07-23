package com.example.protasks

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.ViewCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MySwipeRefreshLayout : SwipeRefreshLayout {
    private var mScrollingView: View? = null

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
    }

    override fun canChildScrollUp(): Boolean {
        return mScrollingView != null && ViewCompat.canScrollVertically(mScrollingView, -1)
    }

    fun setScrollingView(scrollingView: View?) {
        mScrollingView = scrollingView
    }
}