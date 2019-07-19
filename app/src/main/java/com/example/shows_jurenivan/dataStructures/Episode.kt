package com.example.shows_jurenivan.dataStructures

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Episode(
    val image:Uri?,
    val episodeNumber: Int,
    val seasonNumber: Int,
    var description:String,
    var title:String
) : Parcelable