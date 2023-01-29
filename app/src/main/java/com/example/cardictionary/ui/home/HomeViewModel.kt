package com.example.cardictionary.ui.home


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.cardictionary.MainActivity
import com.example.cardictionary.R
import com.example.cardictionary.databinding.FragmentHomeBinding
import com.example.cardictionary.databinding.PreExamTipBinding
import com.example.cardictionary.tool.loadTopScoreRecord1
import com.example.cardictionary.tool.loadTopScoreRecord2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class HomeViewModel : ViewModel() {
    fun showTipDialog(context: Context, fragment: HomeFragment, activity: Activity) {
        val inflater = LayoutInflater.from(context)
        val binding = PreExamTipBinding.inflate(inflater)
        val dialog = AlertDialog.Builder(context).create()
        dialog.show()
        val lp = dialog.window!!.attributes
        lp.width = 900
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.apply {
            attributes = lp
            setContentView(binding.root)
        }
        binding.examOneBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToOnlineExamFragment(1)
            fragment.findNavController().navigate(action)
            dialog.setCancelable(false)
            setTimerTaskOnDialog(dialog, it, 5, activity)
        }
        binding.examTwoBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToOnlineExamFragment(2)
            fragment.findNavController().navigate(action)
            dialog.setCancelable(false)
            setTimerTaskOnDialog(dialog, it, 3, activity)
        }

    }


    private fun setTimerTaskOnDialog(
        dialog: AlertDialog,
        view: View,
        time: Int,
        activity: Activity
    ) {
        var t = time
        val textView = view as TextView
        textView.text = time.toString()
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (t <= 0) {
                    dialog.dismiss()
                    cancel()
                } else {
                    t--
                }
                activity.runOnUiThread {
                    textView.text = "$t"
                }
            }
        }, 1000, 1000)
    }

    fun loadTopScore(context: Context, binding: FragmentHomeBinding) {
        loadTopScoreRecord1(context) {
            binding.km1Highest.text = it
        }
        loadTopScoreRecord2(context) {
            binding.km4Highest.text = it
        }
    }
}