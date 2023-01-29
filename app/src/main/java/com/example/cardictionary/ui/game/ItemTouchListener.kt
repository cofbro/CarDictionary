package com.example.cardictionary.ui.game

import android.graphics.RectF
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class ItemTouchListener(private val rectF: RectF, private val itemDecoration: ItemDecoration): RecyclerView.OnItemTouchListener {
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        if (e.action == MotionEvent.ACTION_DOWN) {
            if (rectF.contains(e.x, e.y)) {
                itemDecoration.click(rv)

            }
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}