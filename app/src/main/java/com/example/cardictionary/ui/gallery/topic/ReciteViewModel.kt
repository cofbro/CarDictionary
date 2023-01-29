package com.example.cardictionary.ui.gallery.topic

import android.app.Application
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.cardictionary.MainViewModel
import com.example.cardictionary.R
import com.example.cardictionary.data.adapter.MessageListAdapter
import com.example.cardictionary.data.repository.Suit
import com.example.cardictionary.databinding.MessageListLayoutBinding
import com.example.cardictionary.databinding.ReciteItemLayoutBinding
import com.example.cardictionary.tool.LCUtils
import com.example.cardictionary.tool.User
import com.example.cardictionary.tool.toNormalString
import java.util.*
import kotlin.collections.ArrayList

class ReciteViewModel(app: Application) : AndroidViewModel(app) {
    private val mainViewModel = MainViewModel.getInstance()
    private var messagePairList = arrayListOf<MessageListAdapter.MessagePair>()
    private val lcUtils = LCUtils()
    fun setDataIntoReciteItem(binding: ReciteItemLayoutBinding, suit: Suit) {
        when (suit.type) {
            "1" -> {
                binding.typeText.text = "单项选择题"
            }
            "2" -> {
                binding.typeText.text = "判断题"
            }
            "3" -> {
                binding.typeText.text = "多项选择题"
            }
        }
        binding.choiceATextView.text = suit.choiceA.substringAfter("、")
        binding.choiceBTextView.text = suit.choiceB.substringAfter("、")
        binding.choiceCTextView.text = suit.choiceC.substringAfter("、")
        binding.choiceDTextView.text = suit.choiceD.substringAfter("、")
        binding.onlineExamQuestion.text = suit.question
        binding.RAns.text = suit.rightAns
        binding.YAns.text = suit.faultAns
        binding.explain.text = suit.explain.toNormalString()
        if (suit.titlePic != "") {
            Glide.with(binding.root.context)
                .load(suit.titlePic)
                .into(binding.onlineExamPic)
        } else {
            binding.onlineExamPic.visibility = View.GONE
        }
        bindUIFinally(binding, suit)
    }

    private fun bindUIFinally(binding: ReciteItemLayoutBinding, suit: Suit) {
        suit.rightAns.forEach {
            when (it) {
                'A' -> {
                    binding.alphaA.setImageResource(R.drawable.isright)
                }
                'B' -> {
                    binding.alphaB.setImageResource(R.drawable.isright)
                }
                'C' -> {
                    binding.alphaC.setImageResource(R.drawable.isright)
                }
                else -> {
                    binding.alphaD.setImageResource(R.drawable.isright)
                }
            }
        }
    }

    fun loadMessageDataCirculating(adapter: MessageListAdapter, alreadyLoadData: () -> Unit = {}) {
        adapter.setMessageData(messagePairList)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                lcUtils.getAllCommentsObject {
                    messagePairList = it
                    alreadyLoadData()
                }
            }
        }, 0, 500000)
    }

    private fun loadMessageDataRightNow(adapter: MessageListAdapter) {
        lcUtils.getAllCommentsObject {
            messagePairList = it
            adapter.setMessageData(it)
        }
    }

    fun getMessagePairList(): ArrayList<MessageListAdapter.MessagePair> {
        return messagePairList
    }

    fun bindClickEvent(
        adapter: MessageListAdapter,
        binding: MessageListLayoutBinding,
        position: Int,
        parent: ViewGroup
    ) {
        binding.likeBtn.setOnClickListener {
           if (mainViewModel.getCurrentUser() != null) {
               if (binding.likeBtn.tag == "0") {
                   lcUtils.likeTheComment(
                       messagePairList[position].id,
                       binding.likeNum.text.toString()
                   ) {
                       loadMessageDataRightNow(adapter)
                       binding.likeBtn.setImageResource(R.drawable.ic_like_red)
                       binding.likeBtn.tag = "1"
                   }

               } else {
                   lcUtils.cancelLike(
                       messagePairList[position].id,
                       binding.likeNum.text.toString()
                   ) {
                       loadMessageDataRightNow(adapter)
                       binding.likeBtn.setImageResource(R.drawable.like)
                       binding.likeBtn.tag = "0"
                   }
               }
           } else {
               parent.findNavController().navigate(R.id.action_wrongTopicFragment_to_loginFragment)
           }
        }


    }

    fun sendComment(adapter: MessageListAdapter, text: String, user: User) {
        lcUtils.sendComments(user, text) {
            loadMessageDataRightNow(adapter)
        }
    }
}
