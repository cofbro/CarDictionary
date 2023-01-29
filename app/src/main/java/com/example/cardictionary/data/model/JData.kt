package com.example.cardictionary.data.model


import com.google.gson.annotations.SerializedName


data class JData(
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("result")
    val result: List<Result>
)