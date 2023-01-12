package com.example.lr4pokemon

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokeGetter {
    fun getPokemon(id:Int): LiveData<Pokemon> {
        val BASE_URL = "https://pokeapi.co/api/v2/"
        var pokemon = MutableLiveData<Pokemon>()
        pokemon.value= Pokemon(132,"ditto",40,3,)
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(RestAPI::class.java)
        val call : Call<Pokemon?>? = service.getPokemon(id.toLong())
            call?.enqueue(object : Callback<Pokemon?> {
                override fun onResponse(call: Call<Pokemon?>, response: Response<Pokemon?>) {
                    if (response.isSuccessful) {
                        pokemon.value = response.body()
                        Log.d("Response", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<Pokemon?>, t: Throwable) {
                    Log.d("fail", t.message.toString());
                }

            })
        return pokemon
    }
}