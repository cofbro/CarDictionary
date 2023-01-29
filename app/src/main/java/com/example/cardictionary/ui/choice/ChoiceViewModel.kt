package com.example.cardictionary.ui.choice

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardictionary.data.model.Result
import com.example.cardictionary.databinding.ItemLayoutBinding
import com.example.cardictionary.tool.toNormalString
import com.example.cardictionary.tool.vSimple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChoiceViewModel: ViewModel() {
    private lateinit var recyclerView: RecyclerView
    private val choiceItemResultList = arrayListOf<Result>()
    private val choiceItemViewList = arrayListOf<ChoiceItem>()
    private var currentScrollNum = 1

    @SuppressLint("SetTextI18n")
    fun setDataIntoItemView(binding: ItemLayoutBinding, result: Result, position: Int) {
        // 设置问题
        binding.questionTextView.text = position.toString()  + ". " + result.question
        // 设置选项
        if (result.item1.isEmpty()) {
            binding.choiceItem1.setQuestion("正确")
            binding.choiceItem2.setQuestion("错误")
        }
        binding.choiceItem1.setAnswer(result.answer.toInt())
        binding.choiceItem2.setAnswer(result.answer.toInt())
        binding.choiceItem1.setQuestion(result.item1)
        binding.choiceItem2.setQuestion(result.item2)
        if (result.item3 != "") {
            binding.choiceItem3.setQuestion(result.item3)
            binding.choiceItem4.setQuestion(result.item4)
            binding.choiceItem3.setAnswer(result.answer.toInt())
            binding.choiceItem4.setAnswer(result.answer.toInt())
        } else {
            binding.choiceItem3.visibility = View.GONE
            binding.choiceItem4.visibility = View.GONE
        }
        // 设置解析
        val js = "解析：" + result.explains.toNormalString()
        binding.answerTextView.text = js
        // 设置图片
        if (result.url != "") {
            Glide.with(binding.root.context)
                .load(result.url)
                .into(binding.pictrue)
        } else {
            binding.pictrue.visibility = View.GONE
        }

        binding.choiceItem1.answerCallback = {
            juggleTheAnswer(recyclerView, binding, it, result)
        }
        binding.choiceItem2.answerCallback = {
            juggleTheAnswer(recyclerView, binding, it, result)
        }
        binding.choiceItem3.answerCallback = {
            juggleTheAnswer(recyclerView, binding, it, result)
        }
        binding.choiceItem4.answerCallback = {
            juggleTheAnswer(recyclerView, binding, it, result)
        }
    }

    fun initChoiceItemResultList(result: Result) {
        choiceItemResultList.add(result)
    }

    private fun juggleTheAnswer(recyclerView: RecyclerView, binding: ItemLayoutBinding, it: Boolean, result: Result) {
        if (it) {
            viewModelScope.launch(Dispatchers.IO) {
                delay(200)
                withContext(Dispatchers.Main) {
                    recyclerView.layoutManager!!.smoothScrollToPosition(recyclerView,
                        RecyclerView.State(),
                        currentScrollNum)
                }
            }
        } else {
            binding.tips.text = translateNumToAnswerItem(result.answer)
            binding.nextQuestion.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    recyclerView.layoutManager!!.smoothScrollToPosition(recyclerView,
                        RecyclerView.State(),
                        currentScrollNum)
                }
            }
            binding.answerTextView.visibility = View.VISIBLE
            binding.tips.visibility = View.VISIBLE
            vSimple(binding.root.context, 200)
        }

    }

    fun getCurrentScrollNum(): Int {
        return currentScrollNum
    }

    fun setCurrentScrollNum(num: Int) {
        currentScrollNum = num
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    fun translateNumToAnswerItem(num: String): String {
        return when(num) {
            "1" -> {
                "正确答案：A"
            }
            "2" -> {
                "正确答案：B"
            }
            "3" -> {
                "正确答案：C"
            }
            else -> {
                "正确答案：D"
            }
        }
    }
}