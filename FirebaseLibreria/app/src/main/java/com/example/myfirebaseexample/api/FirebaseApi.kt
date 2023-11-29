package com.example.myfirebaseexample.api

import com.example.myfirebaseexample.api.response.PostResponse
import com.example.myfirebaseexample.api.response.BookResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FirebaseApi {
    @GET("Libros.json")
    fun getWeapons(): Call<MutableMap<String, BookResponse>>

    @GET("Libros/{id}.json")
    fun getWeapon(
        @Path("id") id: String
    ): Call<BookResponse>

    @POST("Libros.json")
    fun setWeapon(
        @Body() body: BookResponse
    ): Call<PostResponse>
}