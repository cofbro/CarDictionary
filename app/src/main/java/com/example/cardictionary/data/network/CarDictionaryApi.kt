package com.example.cardictionary.data.network

import com.example.cardictionary.data.model.JData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CarDictionaryApi {
    @GET("query?model=c1&key=d0419d8f0277a3a73309fad054dea886")
    suspend fun getCarDictionaryTopic(@Query("subject") subject: String ): Response<JData>

}