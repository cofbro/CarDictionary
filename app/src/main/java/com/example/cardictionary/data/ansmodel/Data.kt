package com.example.cardictionary.data.ansmodel


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("explain")
    val explain: String,
    @SerializedName("id")
    val id: Int
)