package com.example.lr4pokemon

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class RestViewModel : ViewModel() {
    private val repository: PokeGetter = PokeGetter()

    fun getFirstTodo(id:Int): LiveData<Pokemon> {
        return repository.getPokemon(id)
    }
}

