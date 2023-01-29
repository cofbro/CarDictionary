package com.example.cardictionary.data.model3


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("content")
    val content: String,
    @SerializedName("updateTime")
    val updateTime: String
)