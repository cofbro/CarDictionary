package com.example.cardictionary.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

class Repository(context: Context) {
    private var suitDao: SuitDao

    init {
        suitDao = SuitDatabase.getInstance(context).suitDao()
    }

    fun addToBackground(suit: Suit) {
        suitDao.addToBackground(suit)
    }

    fun removeFromBackground(suit: Suit) {
        suitDao.removeFromBackground(suit)
    }

    fun updateTheBackground(suit: Suit){
        suitDao.updateTheBackground(suit)
    }

    fun queryAllFromBackground(): Flow<List<Suit>> {
        return suitDao.queryAllFromBackground()
    }

    fun queryAllFromBackgroundNow(): List<Suit> {
        return suitDao.queryAllFromBackgroundNow()
    }
}