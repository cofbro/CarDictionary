package com.example.cardictionary.data.network


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Constants {
    private const val BASE_URL = "http://v.juhe.cn/jztk/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val carDictionaryApi: CarDictionaryApi = retrofit.create(CarDictionaryApi::class.java)

    private val retrofit2 = Retrofit.Builder()
        .baseUrl("https://www.mxnzp.com/api/driver_exam/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val carDictionaryApi2: CarDictionaryApi2 = retrofit2.create(CarDictionaryApi2::class.java)

    private val retrofit3 = Retrofit.Builder()
        .baseUrl(" https://www.mxnzp.com/api/jokes/list/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val jokeDailyApi = retrofit3.create(JokeDailyApi::class.java)
}