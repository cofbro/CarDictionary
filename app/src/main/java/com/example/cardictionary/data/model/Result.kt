package com.example.cardictionary.data.model


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("explains")
    val explains: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("item1")
    val item1: String,
    @SerializedName("item2")
    val item2: String,
    @SerializedName("item3")
    val item3: String,
    @SerializedName("item4")
    val item4: String,
    @SerializedName("question")
    val question: String,
    @SerializedName("url")
    val url: String
)