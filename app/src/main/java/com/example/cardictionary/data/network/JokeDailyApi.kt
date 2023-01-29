package com.example.cardictionary.data.network

import com.example.cardictionary.data.model3.Joke
import retrofit2.Response
import retrofit2.http.GET

interface JokeDailyApi {
    @GET("random?app_id=bbjjb1jwpgleiilu&app_secret=b1lGYTJzUW0wYzNZMEFyUkh1RERxUT09")
    suspend fun getJokesApi(): Response<Joke>
}