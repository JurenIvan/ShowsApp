package com.example.shows_jurenivan.data.dataStructures

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaResponse(

    @Json(name = "path")
    var path: String,

    @Json(name = "type")
    var type: String,

    @Json(name = "_id")
    var _id: String
)