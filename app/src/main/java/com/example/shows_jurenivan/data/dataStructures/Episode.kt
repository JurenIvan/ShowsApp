package com.example.shows_jurenivan.data.dataStructures

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Episode(
    val image: String?,
    val episodeNumber: Int,
    val seasonNumber: Int,
    var description: String,
    var title: String
) : Parcelable, Serializable