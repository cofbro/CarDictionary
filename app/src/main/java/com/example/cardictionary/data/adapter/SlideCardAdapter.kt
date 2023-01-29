package com.example.cardictionary.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.data.model3.Data
import com.example.cardictionary.databinding.CardGameItemBinding
import com.example.cardictionary.tool.toNormalString

class SlideCardAdapter : RecyclerView.Adapter<SlideCardAdapter.MyViewHolder>() {
    var mData = arrayListOf<JokePair>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardGameItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class MyViewHolder(private val binding: CardGameItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.jokeTextView.text = mData[position].data.content.toNormalString()
            binding.currentPage.text = mData[position].position.toString()
            binding.updateTime.text = mData[position].data.updateTime
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setDataJokePair(data: List<Data>) {
        mData = arrayListOf()
        data.forEachIndexed { index, d ->
            mData.add(JokePair(index + 1, d))
        }
        notifyDataSetChanged()
    }

    class JokePair(
        val position: Int,
        val data: Data
    )
}