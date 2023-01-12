package com.example.lr4pokemon

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RestAPI {
    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id: Long?
    ): Call<Pokemon?>?
}