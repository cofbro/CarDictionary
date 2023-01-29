package com.example.cardictionary.data.network

import com.example.cardictionary.data.ansmodel.Ans
import com.example.cardictionary.data.model2.DiffData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CarDictionaryApi2 {
    @GET("question/list?rank=1&app_id=bbjjb1jwpgleiilu&app_secret=b1lGYTJzUW0wYzNZMEFyUkh1RERxUT09")
    suspend fun getCarDictionaryTopic2(
        @Query("page") page: String,
        @Query("type") type: String
    ): Response<DiffData>

    @GET("answer/list?app_id=bbjjb1jwpgleiilu&app_secret=b1lGYTJzUW0wYzNZMEFyUkh1RERxUT09")
    suspend fun getCarDictionaryTopic2Ans(@Query("ids") ids: String): Response<Ans>
}