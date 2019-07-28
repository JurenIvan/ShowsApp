package com.example.shows_jurenivan.data.dataStructures

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class ResponseData<T>(
    @Json(name = "data")
    val data: T? = null,

    //we make field transient when we don't want to serialize it
    @Transient
    var isSuccessful: Boolean = true
)
