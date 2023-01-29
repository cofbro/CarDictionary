package com.example.cardictionary.ui.choice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.data.adapter.ChoiceAdapter
import com.example.cardictionary.data.network.Constants
import com.example.cardictionary.databinding.FragmentChoiceBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChoiceFragment : Fragment() {
    private lateinit var binding: FragmentChoiceBinding
    private val choiceAdapter = ChoiceAdapter()
    private val choiceViewModel: ChoiceViewModel by viewModels()
    private val type: ChoiceFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChoiceBinding.inflate(inflater, container, false)


        lifecycleScope.launch(Dispatchers.IO) {
            val data = Constants.carDictionaryApi.getCarDictionaryTopic(type.type.toString())
            data.let {
                choiceAdapter.setDataAndViewModel(it, choiceViewModel)
            }
            withContext(Dispatchers.Main) {
                binding.homeRecyclerView.apply {
                    choiceViewModel.setRecyclerView(this)
                    setItemViewCacheSize(50)
                    recycledViewPool.setMaxRecycledViews(0, 0)
                    val helper = PagerSnapHelper()
                    helper.attachToRecyclerView(this)
                    adapter = choiceAdapter
                    val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    layoutManager = linearLayoutManager
                    addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                //获取当前是第几页
                                choiceViewModel.setCurrentScrollNum(linearLayoutManager.findFirstVisibleItemPosition() + 1)
                            }
                        }
                    })
                }
            }
        }


        return binding.root
    }


}