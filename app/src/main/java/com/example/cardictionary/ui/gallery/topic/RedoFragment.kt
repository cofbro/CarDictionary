package com.example.cardictionary.ui.gallery.topic

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.R
import com.example.cardictionary.data.adapter.RedoAdapter
import com.example.cardictionary.databinding.FragmentRedoBinding
import com.example.cardictionary.ui.gallery.GalleryViewModel
import com.example.cardictionary.ui.onlineexam.OnlineExamViewModel


class RedoFragment : Fragment() {
    private lateinit var binding: FragmentRedoBinding
    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private val redoViewModel: RedoViewModel by activityViewModels()
    private val redoAdapter = RedoAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRedoBinding.inflate(inflater, container, false)
        redoViewModel.init(binding)
        redoViewModel.setRedoAdapter(redoAdapter)
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        redoAdapter.setDataList(galleryViewModel.getWrongTopicList(), redoViewModel)
        binding.questionRecyclerView.apply {
            setItemViewCacheSize(galleryViewModel.getWrongTopicList().size)
            recycledViewPool.setMaxRecycledViews(0, 0)
            redoViewModel.setRecyclerView(this)
            val helper = PagerSnapHelper()
            helper.attachToRecyclerView(this)
            adapter = redoAdapter
            layoutManager = linearLayoutManager
            scrollToPosition(redoViewModel.locatedNum)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                @SuppressLint("SetTextI18n")
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        //获取当前是第几页
                        redoViewModel.setCurrentScrollNum(linearLayoutManager.findFirstVisibleItemPosition())
                        binding.currentQ.text = (redoViewModel.locatedNum + 1).toString()
                        redoViewModel.removeAllInMyAnsList()
                    }
                }
            })
        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        redoViewModel.observeBottomData(viewLifecycleOwner, binding)
        binding.deleteBtn.setOnClickListener {
            val suit = redoViewModel.getWrongTopicList()[redoViewModel.locatedNum]
            redoViewModel.deleteCurrentQuestion(requireContext(), suit, redoAdapter, this, binding)
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        binding.questionRecyclerView.scrollToPosition(redoViewModel.locatedNum)
        binding.currentQ.text = (redoViewModel.locatedNum + 1).toString()
    }

}