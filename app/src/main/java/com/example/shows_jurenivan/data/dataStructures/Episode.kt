package com.example.shows_jurenivan.data.dataStructures

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Episode(
    @Json(name = "_id")
    val episodeId: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "season")
    var season: String,

    @Json(name = "episodeNumber")
    var episode: String,

    @Json(name = "imageUrl")
    var imageUrl: String?
)