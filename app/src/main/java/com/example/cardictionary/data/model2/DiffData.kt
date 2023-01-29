package com.example.cardictionary.data.model2


import com.google.gson.annotations.SerializedName

data class DiffData(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("msg")
    val msg: String
)