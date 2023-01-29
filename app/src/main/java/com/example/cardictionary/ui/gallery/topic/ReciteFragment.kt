package com.example.cardictionary.ui.gallery.topic

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.MainViewModel
import com.example.cardictionary.R
import com.example.cardictionary.data.adapter.MessageListAdapter
import com.example.cardictionary.data.adapter.ReciteAdapter
import com.example.cardictionary.databinding.FragmentReciteBinding



class ReciteFragment : Fragment() {
    private lateinit var binding: FragmentReciteBinding
    private val reciteViewModel: ReciteViewModel by activityViewModels()
    private val redoViewModel: RedoViewModel by activityViewModels()
    private val mainViewModel = MainViewModel.getInstance()
    private lateinit var messageListAdapter: MessageListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReciteBinding.inflate(inflater, container, false)
        val reciteAdapter = ReciteAdapter()
        reciteAdapter.setModel(reciteViewModel)
        reciteAdapter.setDataList(redoViewModel.getWrongTopicList())
        binding.questionRecyclerView.apply {
            setItemViewCacheSize(redoViewModel.getWrongTopicList().size)
            recycledViewPool.setMaxRecycledViews(0, 0)
            val linearLayoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            val helper = PagerSnapHelper()
            helper.attachToRecyclerView(this)
            adapter = reciteAdapter
            layoutManager = linearLayoutManager
            scrollToPosition(redoViewModel.locatedNum)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                @SuppressLint("SetTextI18n")
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        //获取当前是第几页
                        redoViewModel.locatedNum =
                            linearLayoutManager.findFirstVisibleItemPosition()
                    }
                }
            })
        }

        messageListAdapter = MessageListAdapter(reciteViewModel)
        reciteViewModel.loadMessageDataCirculating(messageListAdapter) {
            messageListAdapter.setMessageData(reciteViewModel.getMessagePairList())
        }
        binding.messageRecyclerView.apply {
            adapter = messageListAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.questionRecyclerView.scrollToPosition(redoViewModel.locatedNum)

        binding.button2.setOnClickListener {
            val text = binding.editText.text.toString()
            if (mainViewModel.getCurrentUser() != null && text != "") {
                reciteViewModel.sendComment(messageListAdapter, text, mainViewModel.getCurrentUser()!!)
                binding.editText.setText("")
            } else if (mainViewModel.getCurrentUser() == null){
                findNavController().navigate(R.id.action_wrongTopicFragment_to_loginFragment)
            }
        }
    }
}