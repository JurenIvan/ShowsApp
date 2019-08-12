package com.example.shows_jurenivan.data.dataStructures

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comment(

    @Json(name = "_id")
    var id: String,

    @Json(name = "episodeId")
    val episodeId: String,

    @Json(name = "text")
    val text: String,

    @Json(name = "userEmail")
    var userEmail: String

)