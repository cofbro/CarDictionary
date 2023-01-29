package com.example.cardictionary.data.model2


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("list")
    val list: List<JData2>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("totalPage")
    val totalPage: Int
)