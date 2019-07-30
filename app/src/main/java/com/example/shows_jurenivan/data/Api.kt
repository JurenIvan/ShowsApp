package com.example.shows_jurenivan.data

import com.example.shows_jurenivan.data.dataStructures.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface Api {
    @POST("api/users")
    fun registerUser(@Body user: User): Call<ResponseData<User>>

    @POST("/api/users/sessions")
    fun loginUser(@Body user: User): Call<ResponseData<Token>>

    @GET("api/shows/")
    fun getShows(): Call<ResponseData<List<Show>>>

    @GET("api/shows/{showId}")
    fun getShow(@Path("showId") showID: String): Call<ResponseData<Show>>

    @GET("/api/shows/{showId}/episodes")
    fun getEpisodes(@Path("showId") showId: String): Call<ResponseData<List<Episode>>>

}