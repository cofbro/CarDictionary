package com.example.cardictionary.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.data.model2.JData2
import com.example.cardictionary.data.repository.Suit
import com.example.cardictionary.databinding.OnlineExamLayoutBinding
import com.example.cardictionary.ui.gallery.topic.RedoViewModel

class RedoAdapter : RecyclerView.Adapter<RedoAdapter.MyViewHolder>() {
    private lateinit var dataList: List<Suit>
    private lateinit var redoViewModel: RedoViewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OnlineExamLayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(private val binding: OnlineExamLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            redoViewModel.setDataIntoItemView(binding, createJData2(position), position)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: List<Suit>, redoViewModel: RedoViewModel) {
        this.dataList = dataList
        this.redoViewModel = redoViewModel
        notifyDataSetChanged()
    }

    fun createJData2(position: Int): JData2 {
        return JData2(
            0,
            dataList[position].choiceA,
            dataList[position].choiceB,
            dataList[position].choiceC,
            dataList[position].choiceD,
            0,
            dataList[position].question,
            dataList[position].titlePic,
            dataList[position].type.toInt(),
            0
        )
    }
}