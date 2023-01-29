package com.example.cardictionary.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.cardictionary.data.model3.Data
import com.example.cardictionary.data.network.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameQuestionViewModel: ViewModel() {
    fun setJokeContent(alreadyLoad: (List<Data>) -> Unit = {}) {
        Log.d("chy","调用setJokeContent")
        viewModelScope.launch(Dispatchers.IO) {
            val data = Constants.jokeDailyApi.getJokesApi().body()?.data
            withContext(Dispatchers.Main) {
                if (data != null) {
                    alreadyLoad(data)
                }
            }
        }
    }
}