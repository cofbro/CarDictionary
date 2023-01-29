package com.example.cardictionary.data.ansmodel


import com.google.gson.annotations.SerializedName

data class Ans(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("msg")
    val msg: String
)