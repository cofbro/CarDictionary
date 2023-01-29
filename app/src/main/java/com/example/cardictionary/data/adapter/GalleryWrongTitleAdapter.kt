package com.example.cardictionary.data.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.R
import com.example.cardictionary.data.repository.Suit
import com.example.cardictionary.databinding.FragmentGalleryBinding
import com.example.cardictionary.databinding.WrongTopicLayoutBinding
import com.example.cardictionary.tool.DiffCallback
import com.example.cardictionary.ui.gallery.GalleryFragmentDirections
import com.example.cardictionary.ui.gallery.GalleryViewModel

class GalleryWrongTitleAdapter : RecyclerView.Adapter<GalleryWrongTitleAdapter.MyViewHolder>() {
    private var orderedList = emptyList<Suit>()
    private var originList = emptyList<Suit>()
    private var suitList = emptyList<Suit>()
    private lateinit var fragmentGalleryBinding: FragmentGalleryBinding
    private lateinit var galleryViewModel: GalleryViewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WrongTopicLayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return suitList.size
    }

    inner class MyViewHolder(private val binding: WrongTopicLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.wrongTitle.text = suitList[position].question
            itemView.setOnClickListener {
                val action = GalleryFragmentDirections.actionNavGalleryToWrongTopicFragment(position)
                fragmentGalleryBinding.root.findNavController().navigate(action)
            }
        }

    }

    fun setModel(
        galleryViewModel: GalleryViewModel,
        fragmentGalleryBinding: FragmentGalleryBinding
    ) {
        this.galleryViewModel = galleryViewModel
        this.fragmentGalleryBinding = fragmentGalleryBinding
    }

    fun setSuitList(dataList: List<Suit>) {
        val result = DiffUtil.calculateDiff(DiffCallback(dataList, suitList))
        suitList = dataList
        originList = dataList
        orderedList = suitList.reversed()
        result.dispatchUpdatesTo(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reversSuitList(isOrdered: Boolean) {
        suitList = if (isOrdered) {
            orderedList
        } else {
            originList

        }
        notifyDataSetChanged()
    }

}