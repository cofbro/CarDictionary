package com.example.cardictionary.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.data.repository.Suit
import com.example.cardictionary.databinding.ReciteItemLayoutBinding
import com.example.cardictionary.ui.gallery.topic.ReciteViewModel

class ReciteAdapter() : RecyclerView.Adapter<ReciteAdapter.MyViewHolder>() {
    private lateinit var dataList: List<Suit>
    private lateinit var model: ReciteViewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReciteItemLayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(private val binding: ReciteItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            model.setDataIntoReciteItem(binding, dataList[position])
        }

    }

    fun setDataList(data: List<Suit>) {
        dataList = data
    }

    fun setModel(model: ReciteViewModel) {
        this.model = model
    }
}