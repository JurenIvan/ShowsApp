package com.example.shows_jurenivan.data

import com.example.shows_jurenivan.data.dataStructures.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


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


    @POST("/api/episodes")
    fun postEpisode(@Header("Authorization") token: String, @Body episode: Episode): Call<Void>

    @GET("/api/episodes/{episodeId}")
    fun getEpisode(@Path("episodeId") episodeId: String): Call<ResponseData<Episode>>


    @GET("/api/episodes/{episodeId}/comments")
    fun getComments(@Path("episodeId") episodeId: String): Call<ResponseData<List<Comment>>>

    @POST("/api/comments")
    fun postComment(@Header("Authorization") token: String, @Body comment: Comment): Call<Void>

    @POST("/api/media")
    @Multipart
    fun uploadMedia(@Header("Authorization") token: String, @Part("file\"; filename=\"image.jpg\"") request: RequestBody): Call<ResponseData<MediaResponse>>


    @POST("/api/shows/{showId}/like")
    fun likeShow(@Header("Authorization") token: String, @Path("showId") showId: String): Call<Void>

    @POST("/api/shows/{showId}/dislike")
    fun dislikeShow(@Header("Authorization") token: String, @Path("showId") showId: String): Call<Void>


}