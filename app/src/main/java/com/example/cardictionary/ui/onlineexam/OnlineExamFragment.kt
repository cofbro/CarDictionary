package com.example.cardictionary.ui.onlineexam



import android.os.Bundle
import android.util.Log
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
import com.example.cardictionary.data.adapter.OnlineExamAdapter
import com.example.cardictionary.data.model2.JData2
import com.example.cardictionary.data.network.Constants
import com.example.cardictionary.databinding.FragmentOnlineExamBinding
import kotlinx.coroutines.*
import kotlin.math.abs
import kotlin.random.Random

class OnlineExamFragment : Fragment() {
    private lateinit var binding: FragmentOnlineExamBinding
    private val onlineExamViewModel: OnlineExamViewModel by viewModels()
    private val onlineExamAdapter = OnlineExamAdapter()
    private val type: OnlineExamFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlineExamBinding.inflate(inflater, container, false)
        onlineExamViewModel.type = type.type
        onlineExamViewModel.setCountDownTimer("45:00", binding.timer, this)
        lifecycleScope.launch(Dispatchers.IO) {

            val dataList = arrayListOf<JData2>()
            for (i in 0 until 10) {
                val page = abs(Random(System.currentTimeMillis()).nextInt() % 100).toString()
                Log.d("chy","page is $page")
                val data = Constants.carDictionaryApi2.getCarDictionaryTopic2(page, type.type.toString())
                delay(101)
                data.body()!!.data.list.forEach {
                    dataList.add(it)
                }
            }
            async(Dispatchers.IO) {
                dataList.forEach {
                    val ansPair = onlineExamViewModel.getCarDictionaryTopic2Ans(it.id.toString())
                    onlineExamViewModel.ansList.add(ansPair.ans)
                    onlineExamViewModel.explainList.add(ansPair.explain)
                    Log.d("chy", "ans is ${ansPair.ans}")
                    delay(101)
                }

            }
            withContext(Dispatchers.Main) {
                onlineExamAdapter.setViewModelAndData(onlineExamViewModel, dataList)
                binding.onlineExamRecyclerView.apply {
                    setItemViewCacheSize(100)
                    recycledViewPool.setMaxRecycledViews(0, 0)
                    onlineExamViewModel.setRecyclerView(this)
                    adapter = onlineExamAdapter
                    val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    layoutManager = linearLayoutManager
                    val snapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(this)
                    addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                //获取当前是第几页
                                onlineExamViewModel.setCurrentScrollNum(linearLayoutManager.findFirstVisibleItemPosition() + 1)
                                binding.currentQ.text = onlineExamViewModel.currentPage.toString()
                                onlineExamViewModel.removeAllInMyAnsList()
                            }
                        }
                    })
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onlineExamViewModel.rightNum.observe(viewLifecycleOwner) {
            binding.rightNumTextView.text = it.toString()
        }
        onlineExamViewModel.falseNum.observe(viewLifecycleOwner) {
            binding.falseNumTextView.text = it.toString()
            if (it > 10) {
                onlineExamViewModel.showTipDialog(requireContext(), this)
            }
        }

        binding.submitPaper.setOnClickListener {
            onlineExamViewModel.showTipDialog(requireContext(), this)
        }
    }


}