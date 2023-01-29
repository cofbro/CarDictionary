package com.example.cardictionary.ui.game

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CardLayoutManager : RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        detachAndScrapAttachedViews(recycler!!)
        val bottomPosition = itemCount - 4
        for (i in bottomPosition until itemCount) {
            val view = recycler.getViewForPosition(i)
            addView(view)

            measureChildWithMargins(view, 0, 0)
            val width = width - getDecoratedMeasuredWidth(view)
            val height = height - getDecoratedMeasuredHeight(view)

            val level = itemCount - i - 1

            layoutDecoratedWithMargins(
                view,
                width / 2,
                height / 2,
                width / 2 + getDecoratedMeasuredWidth(view),
                height / 2 + getDecoratedMeasuredHeight(view)
            )
            view.translationY = (itemCount - i - 1f) * CardConfig.TRANSLATE_GAP
            view.scaleX = 1 - level * CardConfig.SCALE_GAP
            view.scaleY = 1 - level * CardConfig.SCALE_GAP



        }
    }
}
object CardConfig {
    val SCALE_GAP = 0.08f
    val TRANSLATE_GAP = 60f
}