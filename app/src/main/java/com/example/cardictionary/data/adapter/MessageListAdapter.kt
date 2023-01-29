package com.example.cardictionary.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.cardictionary.databinding.MessageListLayoutBinding
import com.example.cardictionary.tool.UserWithNoPsd
import com.example.cardictionary.ui.gallery.topic.ReciteViewModel

class MessageListAdapter(private val model: ReciteViewModel) : RecyclerView.Adapter<MessageListAdapter.MyViewHolder>() {
    private lateinit var messagePair: List<MessagePair>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MessageListLayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return messagePair.size
    }

    inner class MyViewHolder(private val binding: MessageListLayoutBinding, private val parent: ViewGroup) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Glide.with(binding.root.context)
                .load(messagePair[position].user.userAvatar)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(binding.accountAvator)

            binding.accountName.text = messagePair[position].user.username
            binding.comment.text = messagePair[position].message
            binding.likeNum.text = messagePair[position].like
            model.bindClickEvent(this@MessageListAdapter, binding, position, parent)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMessageData(messagePair: List<MessagePair>) {
        this.messagePair = messagePair
        notifyDataSetChanged()
    }

    class MessagePair(
        val user: UserWithNoPsd,
        val message: String,
        val like: String,
        val id: String
    )
}