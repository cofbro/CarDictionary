package com.example.cardictionary.ui.gallery.topic

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.data.adapter.MessageListAdapter

class MessageItemDecoration(private val adapter: MessageListAdapter) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //parent.getChildAdapterPosition(view)
        val position = parent.getChildLayoutPosition(view)
        if (position == 0) {
            outRect.set(0, 50, 0, 0)
        }
    }
}