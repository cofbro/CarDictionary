package com.example.cardictionary.data.repository

import androidx.room.*
import kotlinx.coroutines.flow.Flow



@Dao
interface SuitDao {
    @Insert
    fun addToBackground(suit: Suit)
    @Delete
    fun removeFromBackground(suit: Suit)
    @Update
    fun updateTheBackground(suit: Suit)
    @Query("select * from Suit")
    fun queryAllFromBackground(): Flow<List<Suit>>
    @Query("select * from Suit")
    fun queryAllFromBackgroundNow(): List<Suit>
}