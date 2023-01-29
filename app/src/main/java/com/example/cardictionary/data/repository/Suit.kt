package com.example.cardictionary.data.repository

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Suit(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    val question: String,
    val choiceA: String,
    val choiceB: String,
    val choiceC: String,
    val choiceD: String,
    val rightAns: String,
    val faultAns: String,
    val titlePic: String,
    val explain: String,
    val type: String
)
