package com.example.cardictionary.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardictionary.data.model2.JData2
import com.example.cardictionary.data.network.Constants
import com.example.cardictionary.databinding.OnlineExamLayoutBinding
import com.example.cardictionary.ui.onlineexam.OnlineExamViewModel

class OnlineExamAdapter : RecyclerView.Adapter<OnlineExamAdapter.MyViewHolder>() {
    private lateinit var dataList: ArrayList<JData2>
    private lateinit var model: OnlineExamViewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OnlineExamLayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 100
    }

    inner class MyViewHolder(private val binding: OnlineExamLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            model.setDataIntoItemView(binding, dataList[position], position)
        }
    }

    fun setViewModelAndData(viewModel: OnlineExamViewModel, data: ArrayList<JData2>) {
        model = viewModel
        dataList = data
        dataList.shuffle()
    }
}