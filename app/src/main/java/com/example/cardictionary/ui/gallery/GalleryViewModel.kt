package com.example.cardictionary.ui.gallery

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardictionary.data.adapter.GalleryWrongTitleAdapter
import com.example.cardictionary.data.repository.Repository
import com.example.cardictionary.data.repository.Suit
import com.example.cardictionary.databinding.FaileExamTipBinding
import com.example.cardictionary.databinding.FragmentGalleryBinding
import com.example.cardictionary.databinding.SureCleanTipBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel(val app: Application) : AndroidViewModel(app) {
    private val repository = Repository(app)
    val wrongTopicList = MutableLiveData<List<Suit>>()
    fun queryAllFromBackground() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.queryAllFromBackground().collect {
                wrongTopicList.postValue(it)
            }
        }
    }

    fun getWrongTopicList(): List<Suit> {
        return wrongTopicList.value!!
    }

    private fun cleanAll() {
        viewModelScope.launch(Dispatchers.IO) {
            wrongTopicList.value!!.forEach {
                repository.removeFromBackground(it)
            }
        }
    }

    fun showTipDialog(context: Context) {
        val inflater = LayoutInflater.from(context)
        val binding = SureCleanTipBinding.inflate(inflater)
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
        bindDataWithDialog(binding, dialog)
    }

    private fun bindDataWithDialog(binding: SureCleanTipBinding, dialog: AlertDialog) {
        binding.cancle.setOnClickListener {
            dialog.dismiss()
        }

        binding.sure.setOnClickListener {
            cleanAll()
            dialog.dismiss()
        }
    }
}