package com.example.cardictionary.ui.gallery.topic

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardictionary.R
import com.example.cardictionary.data.adapter.RedoAdapter
import com.example.cardictionary.data.model2.JData2
import com.example.cardictionary.data.repository.Repository
import com.example.cardictionary.data.repository.Suit
import com.example.cardictionary.databinding.FragmentRedoBinding
import com.example.cardictionary.databinding.OnlineExamLayoutBinding
import kotlinx.coroutines.*

class RedoViewModel(app: Application) : AndroidViewModel(app) {
    private var isAutoDelete = false
    private lateinit var recyclerView: RecyclerView
    private lateinit var redoAdapter: RedoAdapter
    private val repository = Repository(app)
    private lateinit var wrongTopicList: List<Suit>
    var rightNum = MutableLiveData(0)
    var falseNum = MutableLiveData(0)
    private val myAnsList = arrayListOf<String>()
    var locatedNum = 0

    @SuppressLint("SetTextI18n")
    fun setDataIntoItemView(binding: OnlineExamLayoutBinding, data: JData2, position: Int) {
        binding.onlineExamQuestion.text = data.title
        if (data.titlePic != "") {
            Glide.with(binding.root.context)
                .load(data.titlePic)
                .into(binding.onlineExamPic)
        } else {
            binding.onlineExamPic.visibility = View.GONE
        }
        binding.choiceATextView.text = data.op1.substringAfter("、")
        binding.choiceBTextView.text = data.op2.substringAfter("、")
        when (data.titleType) {

            1 -> {
                binding.typeText.text = "单项选择题"
                binding.choiceCTextView.text = data.op3.substringAfter("、")
                binding.choiceDTextView.text = data.op4.substringAfter("、")
            }
            2 -> {
                binding.typeText.text = "判断题"
                binding.choiceATextView.text = data.op1.substringAfter("、")
                binding.choiceBTextView.text = data.op2.substringAfter("、")
            }
            3 -> {
                binding.typeText.text = "多项选择题"
                binding.choiceCTextView.text = data.op3.substringAfter("、")
                binding.choiceDTextView.text = data.op4.substringAfter("、")
                onClickMultiplyChoice(
                    binding.constraintLayoutA,
                    "A",
                    binding.sureButton
                )
                onClickMultiplyChoice(
                    binding.constraintLayoutB,
                    "B",
                    binding.sureButton
                )
                onClickMultiplyChoice(
                    binding.constraintLayoutC,
                    "C",
                    binding.sureButton
                )
                onClickMultiplyChoice(
                    binding.constraintLayoutD,
                    "D",
                    binding.sureButton
                )
                binding.sureButton.visibility = View.VISIBLE
                binding.sureButton.setOnClickListener {
                    if (binding.sureButton.tag == "0") {
                        binding.sureButton.tag = "1"
                        if (checkAnsForMultiply(position)) {
                            showRightAns(wrongTopicList[position].rightAns, binding)
                            rightNum.postValue(rightNum.value!! + 1)
                            if (isAutoDelete) {
                                val suit = getWrongTopicList()[locatedNum]
                                viewModelScope.launch(Dispatchers.IO) {
                                    repository.removeFromBackground(suit)
                                }
                            }
                            smoothScrollRecyclerView()
                        } else {
                            binding.RAns.text = wrongTopicList[position].rightAns
                            myAnsList.forEach { str ->
                                binding.YAns.text = binding.YAns.text.toString() + str
                            }
                            binding.answerConstraintLayout.visibility = View.VISIBLE
                            falseNum.postValue(falseNum.value!! + 1)
                        }
                    }
                }
            }
        }
        if (data.titleType != 3) {
            onClickSingleChoice(binding.constraintLayoutA, data, "A", position, binding)
            onClickSingleChoice(binding.constraintLayoutB, data, "B", position, binding)
            onClickSingleChoice(binding.constraintLayoutC, data, "C", position, binding)
            onClickSingleChoice(binding.constraintLayoutD, data, "D", position, binding)
        }
    }

    private fun checkAnsForMultiply(position: Int): Boolean {
        var flag = false
        if (myAnsList.size == wrongTopicList[position].rightAns.length) {
            myAnsList.forEachIndexed { index, _ ->
                flag = wrongTopicList[position].rightAns.contains(myAnsList[index])
            }
        }

        return flag
    }

    private fun checkAnsForSingle(position: Int): Boolean {
        return myAnsList[0] == wrongTopicList[position].rightAns
    }


    @SuppressLint("ClickableViewAccessibility")
    fun onClickMultiplyChoice(
        view: ConstraintLayout,
        choice: String,
        button: View,
    ) {
        var clickNum = 0
        view.setOnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN && button.tag == "0") {
                clickNum++
                if (clickNum % 2 != 0) {
                    addToAnsList(choice)
                    view.setBackgroundColor(Color.WHITE)
                } else {
                    removeToAnsList(choice)
                    view.setBackgroundColor(Color.parseColor("#F4F3F3"))
                }
            }
            true
        }
    }

    private fun onClickSingleChoice(
        view: ConstraintLayout,
        data: JData2,
        choice: String,
        position: Int,
        binding: OnlineExamLayoutBinding
    ) {
        view.setOnClickListener {
            if (binding.sureButton.tag == "0") {
                addToAnsList(choice)
                showRightAns(wrongTopicList[position].rightAns, binding)
                if (checkAnsForSingle(position)) {
                    rightNum.postValue(rightNum.value!! + 1)
                    if (isAutoDelete) {
                        val suit = getWrongTopicList()[locatedNum]
                        viewModelScope.launch(Dispatchers.IO) {
                            repository.removeFromBackground(suit)
                        }

                    }
                    smoothScrollRecyclerView()
                } else {
                    falseNum.postValue(falseNum.value!! + 1)
                    binding.RAns.text = wrongTopicList[position].rightAns
                    binding.YAns.text = myAnsList[0]
                    binding.answerConstraintLayout.visibility = View.VISIBLE
                }
                binding.sureButton.tag = "1"
            }
        }
    }

    private fun smoothScrollRecyclerView() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(600)
        }
        recyclerView.layoutManager!!.smoothScrollToPosition(
            recyclerView,
            RecyclerView.State(),
            locatedNum + 1
        )
    }

    private fun addToAnsList(str: String) {
        myAnsList.add(str)
    }

    private fun removeToAnsList(str: String) {
        myAnsList.remove(str)
    }

    fun setCurrentScrollNum(num: Int) {
        locatedNum = num
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showRightAns(str: String, binding: OnlineExamLayoutBinding) {
        str.forEach {
            when (it.toString()) {
                "A" -> {
                    binding.alphaA.setImageResource(R.drawable.isright)
                }
                "B" -> {
                    binding.alphaB.setImageResource(R.drawable.isright)
                }
                "C" -> {
                    binding.alphaC.setImageResource(R.drawable.isright)
                }
                "D" -> {
                    binding.alphaD.setImageResource(R.drawable.isright)
                }
            }
        }
    }

    fun removeAllInMyAnsList() {
        myAnsList.clear()
    }

    fun setWrongTopicList(list: List<Suit>) {
        wrongTopicList = list
    }

    fun getWrongTopicList(): List<Suit> {
        return wrongTopicList
    }

    fun observeBottomData(lifecycleOwner: LifecycleOwner, binding: FragmentRedoBinding) {
        rightNum.observe(lifecycleOwner) {
            binding.rightNumTextView.text = it.toString()
        }

        falseNum.observe(lifecycleOwner) {
            binding.falseNumTextView.text = it.toString()
        }
    }

    fun deleteCurrentQuestion(
        context: Context,
        suit: Suit,
        adapter: RedoAdapter,
        fragment: RedoFragment,
        binding: FragmentRedoBinding
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFromBackground(suit)
            wrongTopicList = repository.queryAllFromBackgroundNow()
            withContext(Dispatchers.Main) {
                adapter.setDataList(wrongTopicList, this@RedoViewModel)
                Toast.makeText(context, "题目已移除", Toast.LENGTH_SHORT).show()
                if (locatedNum >= 1) {
                    locatedNum--
                } else {
                    if (wrongTopicList.isEmpty()) {
                        fragment.findNavController().navigateUp()
                        locatedNum = 0
                    }
                }
                recyclerView.scrollToPosition(locatedNum)
                init(binding)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun init(binding: FragmentRedoBinding) {
        binding.totalNum.text = wrongTopicList.size.toString()
        binding.currentQ.text = (locatedNum + 1).toString()
    }

    fun setRedoAdapter(adapter: RedoAdapter) {
        this.redoAdapter = adapter
    }

    fun setIsAutoDelete(isAutoDelete: Boolean) {
        this.isAutoDelete = isAutoDelete
    }
}