package com.example.cardictionary.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.data.model.JData
import com.example.cardictionary.databinding.ItemLayoutBinding
import com.example.cardictionary.ui.choice.ChoiceViewModel
import retrofit2.Response

class ChoiceAdapter : RecyclerView.Adapter<ChoiceAdapter.MyViewHolder>() {
    private var dataList: Response<JData>? = null
    private lateinit var choiceViewModel: ChoiceViewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 50
    }

    inner class MyViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if (dataList != null) {
                choiceViewModel.setDataIntoItemView(binding, dataList!!.body()!!.result[position], position + 1)
            }

        }
    }

    fun setDataAndViewModel(data: Response<JData>, viewModel: ChoiceViewModel) {
        dataList = data
        choiceViewModel = viewModel
        dataList!!.body()!!.result.forEach {
            choiceViewModel.initChoiceItemResultList(it)
        }
    }
}