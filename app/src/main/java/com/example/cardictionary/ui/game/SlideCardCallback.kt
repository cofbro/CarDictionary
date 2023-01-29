package com.example.cardictionary.ui.game

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.data.adapter.SlideCardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.sqrt

class SliderCardCallback(
    private val adapter: SlideCardAdapter,
) : ItemTouchHelper.SimpleCallback(0, 15) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val data = adapter.mData
        val remove = data[viewHolder.layoutPosition]
        data.removeAt(viewHolder.layoutPosition)
        data.add(0, remove)
        adapter.notifyDataSetChanged()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val distance = sqrt(dX * dX + dY * dY)
        var fraction = distance / 200f
        if (fraction > 1f) {
            fraction = 1f
        }
        val itemCount = recyclerView.childCount

        for (i in 0 until itemCount) {
            val level = itemCount - i - 1
            val child = recyclerView.getChildAt(i)
            child.translationY = (itemCount - i - 1f) * CardConfig.TRANSLATE_GAP - CardConfig.TRANSLATE_GAP * fraction
            child.scaleX = 1 - level * CardConfig.SCALE_GAP + CardConfig.SCALE_GAP * fraction
            child.scaleY = 1 - level * CardConfig.SCALE_GAP + CardConfig.SCALE_GAP * fraction
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.2f
    }

}