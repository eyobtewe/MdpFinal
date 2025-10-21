package com.mdp.mdpfinal.data.remote.api

import com.mdp.mdpfinal.data.remote.dto.JokeDto
import retrofit2.http.GET

interface JokeApiService {
    @GET("random_joke")
    suspend fun getRandomJoke(): JokeDto
}
    