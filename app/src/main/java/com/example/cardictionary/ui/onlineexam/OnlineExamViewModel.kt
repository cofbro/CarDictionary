package com.example.cardictionary.ui.onlineexam

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardictionary.R
import com.example.cardictionary.data.model2.JData2
import com.example.cardictionary.data.network.Constants
import com.example.cardictionary.data.repository.Repository
import com.example.cardictionary.data.repository.Suit
import com.example.cardictionary.databinding.FaileExamTipBinding
import com.example.cardictionary.databinding.OnlineExamLayoutBinding
import com.example.cardictionary.databinding.SubmitPageTipBinding
import com.example.cardictionary.tool.*
import kotlinx.coroutines.*

class OnlineExamViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var recyclerView: RecyclerView
    var type = 0
    private val repository = Repository(application)
    var rightNum = MutableLiveData(0)
    var falseNum = MutableLiveData(0)
    var currentPage = 1
    val explainList = arrayListOf<String>()
    var ansList = arrayListOf<String>()
    private val myAnsList = arrayListOf<String>()

    @SuppressLint("SetTextI18n")
    fun setCountDownTimer(timeStr: String, view: TextView, fragment: OnlineExamFragment) {
        var str = timeStr
        viewModelScope.launch(Dispatchers.IO) {
            while (view.text != "00:00") {
                delay(1000)
                var secondHalf = str.substringAfter(":").toInt()
                var frontHalf = str.substringBefore(":").toInt()
                if (secondHalf - 1 >= 0) {
                    secondHalf -= 1
                } else {
                    if (frontHalf - 1 >= 0) {
                        frontHalf -= 1
                        secondHalf = 59
                    }
                }
                withContext(Dispatchers.Main) {
                    var frontHalfStr = frontHalf.toString()
                    var secondHalfStr = secondHalf.toString()
                    if (frontHalf < 10) {
                        frontHalfStr = "0$frontHalf"
                    }
                    if (secondHalf < 10) {
                        secondHalfStr = "0$secondHalf"
                    }
                    str = "$frontHalfStr:$secondHalfStr"
                    view.text = str
                }
            }
            Toast.makeText(view.context, "您已超时，已自动帮您提交试卷", Toast.LENGTH_LONG).show()
            showTipDialog2(view.context, fragment)
        }
    }

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
                            showRightAns(ansList[position], binding)
                            rightNum.postValue(rightNum.value!! + 1)
                            smoothScrollRecyclerView()
                        } else {
                            binding.RAns.text = ansList[position]
                            myAnsList.forEach { str ->
                                binding.YAns.text = binding.YAns.text.toString() + str
                            }
                            binding.answerConstraintLayout.visibility = View.VISIBLE
                            falseNum.postValue(falseNum.value!! + 1)
                            addToBackground(data, binding, position)
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
        if (myAnsList.size == ansList[position].length) {
            myAnsList.forEachIndexed { index, _ ->
                flag = ansList[position].contains(myAnsList[index])
            }
        }

        return flag
    }

    private fun checkAnsForSingle(position: Int): Boolean {
        return myAnsList[0] == ansList[position]
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
                showRightAns(ansList[position], binding)
                if (checkAnsForSingle(position)) {
                    rightNum.postValue(rightNum.value!! + 1)
                    smoothScrollRecyclerView()
                } else {
                    falseNum.postValue(falseNum.value!! + 1)
                    binding.RAns.text = ansList[position]
                    binding.YAns.text = myAnsList[0]
                    binding.answerConstraintLayout.visibility = View.VISIBLE
                    addToBackground(data, binding, position)
                }
                binding.sureButton.tag = "1"
            }
        }
    }

    private fun addToBackground(jData2: JData2, binding: OnlineExamLayoutBinding, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToBackground(makeSuit(jData2, binding, position))
        }
    }

    private fun smoothScrollRecyclerView() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(600)
        }
        recyclerView.layoutManager!!.smoothScrollToPosition(
            recyclerView,
            RecyclerView.State(),
            currentPage
        )
    }

    private fun addToAnsList(str: String) {
        myAnsList.add(str)
    }

    private fun removeToAnsList(str: String) {
        myAnsList.remove(str)
    }

    suspend fun getCarDictionaryTopic2Ans(id: String): AnsPair {
        val ans = viewModelScope.async(Dispatchers.IO) {
            Constants.carDictionaryApi2.getCarDictionaryTopic2Ans(id)

        }
        return AnsPair(ans.await().body()!!.data[0].answer, ans.await().body()!!.data[0].explain)
    }

    fun setCurrentScrollNum(num: Int) {
        currentPage = num
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

    fun showTipDialog(context: Context, fragment: OnlineExamFragment) {
        val inflater = LayoutInflater.from(context)
        val binding = FaileExamTipBinding.inflate(inflater)
        val dialog = AlertDialog.Builder(context).create()
        dialog.show()
        dialog.setCancelable(false)
        val lp = dialog.window!!.attributes
        lp.width = 900
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.apply {
            attributes = lp
            setContentView(binding.root)
        }
        bindDataWithDialog(binding, dialog, fragment)
    }

    @SuppressLint("SetTextI18n")
    fun showTipDialog2(context: Context, fragment: OnlineExamFragment) {
        val inflater = LayoutInflater.from(context)
        val binding = SubmitPageTipBinding.inflate(inflater)
        if (rightNum.value!! < 90) {
            binding.titleTextView.text = "别灰心，再学习一会吧！"
        }
        binding.gradeTextView.text = "本次成绩：${rightNum.value}分"
        val dialog = AlertDialog.Builder(context).create()
        dialog.show()
        dialog.setCancelable(false)
        val lp = dialog.window!!.attributes
        lp.width = 900
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.apply {
            attributes = lp
            setContentView(binding.root)
        }
        binding.sureBtn.setOnClickListener {
            dialog.dismiss()

            if (type == 1) {
                Log.d("chy", "type is 1")
                saveTopScore1(context, rightNum.value!!.toString())
            } else {
                Log.d("chy", "type is 2")
                saveTopScore2(context, rightNum.value!!.toString())
            }
            fragment.findNavController().navigateUp()
        }

    }

    private fun bindDataWithDialog(
        binding: FaileExamTipBinding,
        dialog: AlertDialog,
        fragment: OnlineExamFragment
    ) {
        val wrongNum = falseNum.value
        val rightNum = rightNum.value
        val undo = 100 - wrongNum!! - rightNum!!
        binding.wrongTextView.text = wrongNum.toString()
        binding.undoTextView.text = undo.toString()
        binding.scoreTextView.text = rightNum.toString()
        binding.keepDoing.setOnClickListener {
            dialog.dismiss()
        }
        binding.submitNow.setOnClickListener {
            dialog.dismiss()
            submitAns(it.context, fragment)
        }
        blinkTextAnimator(binding.undoTextView)
    }

    private fun submitAns(context: Context, fragment: OnlineExamFragment) {
        showTipDialog2(context, fragment)
    }

    private fun makeSuit(jData2: JData2, binding: OnlineExamLayoutBinding, position: Int): Suit {
        return Suit(
            jData2.id,
            jData2.title,
            jData2.op1,
            jData2.op2,
            jData2.op3,
            jData2.op4,
            binding.RAns.text.toString(),
            binding.YAns.text.toString(),
            jData2.titlePic,
            explainList[position],
            jData2.titleType.toString()
        )
    }

    private fun saveTopScore1(context: Context, score: String) {
        loadTopScoreRecord1(context) {
            if (it != "无") {
                if (it.toInt() < rightNum.value!!) {
                    saveTopScoreRecord1(context, score)
                }
            } else {
                saveTopScoreRecord1(context, score)
            }
        }
    }

    private fun saveTopScore2(context: Context, score: String) {
        loadTopScoreRecord2(context) {
            if (it != "无") {
                if (it.toInt() < rightNum.value!!) {
                    saveTopScoreRecord2(context, score)
                }
            } else {
                saveTopScoreRecord2(context, score)
            }
        }
    }

    class AnsPair(
        val ans: String,
        val explain: String
    )
}