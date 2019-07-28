package com.example.shows_jurenivan.data.dataStructures

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
class User {

    @Json(name = "email")
    var email: String? = null

    @Json(name = "password")
    var password: String? = null

    @Json(name = "_id")
    var id: String? = null


}