package com.example.cardictionary.data.model3


import com.google.gson.annotations.SerializedName

data class Joke(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("msg")
    val msg: String
)