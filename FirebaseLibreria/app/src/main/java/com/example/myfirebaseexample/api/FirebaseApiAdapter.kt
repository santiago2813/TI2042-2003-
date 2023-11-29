package com.example.myfirebaseexample.api

import com.example.myfirebaseexample.api.response.BookResponse
import com.example.myfirebaseexample.api.response.PostResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

class FirebaseApiAdapter {
    private var URL_BASE = "https://firabaseprueba-5ac57-default-rtdb.firebaseio.com/"
    private val firebaseApi = Retrofit.Builder()
        .baseUrl(URL_BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getWeapons(): MutableMap<String, BookResponse>? {
        val call = firebaseApi.create(FirebaseApi::class.java).getWeapons().execute()
        val weapons = call.body()
        return weapons
    }

    fun getWeapon(id: String): BookResponse? {
        val call = firebaseApi.create(FirebaseApi::class.java).getWeapon(id).execute()
        val weapon = call.body()
        return weapon
    }

    fun setWeapon(weapon: BookResponse): PostResponse? {
        val call = firebaseApi.create(FirebaseApi::class.java).setWeapon(weapon).execute()
        val results = call.body()
        return results
    }
}