package com.example.cardictionary.tool

import androidx.recyclerview.widget.DiffUtil
import com.example.cardictionary.data.repository.Suit

class DiffCallback(private val newDataList: List<Suit>, private val oldDataList: List<Suit>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldDataList.size
    }

    override fun getNewListSize(): Int {
        return newDataList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newDataList[newItemPosition] === oldDataList[oldItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newDataList[newItemPosition] == oldDataList[oldItemPosition]
    }
}