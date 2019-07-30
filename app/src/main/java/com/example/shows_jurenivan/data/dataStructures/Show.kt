package com.example.shows_jurenivan.data.dataStructures

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Show(

    @Json(name = "_id")
    val id: String?,

    @Json(name = "title")
    val title: String?,


    @Json(name = "imageUrl")
    val imageURL: String?,


    @Json(name = "description")
    val description: String?


)