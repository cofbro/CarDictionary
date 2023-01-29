package com.example.cardictionary.data.model2


import com.google.gson.annotations.SerializedName

data class JData2(
    @SerializedName("id")
    val id: Int,
    @SerializedName("op1")
    val op1: String,
    @SerializedName("op2")
    val op2: String,
    @SerializedName("op3")
    val op3: String,
    @SerializedName("op4")
    val op4: String,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("titlePic")
    val titlePic: String,
    @SerializedName("titleType")
    val titleType: Int,
    @SerializedName("type")
    val type: Int
)