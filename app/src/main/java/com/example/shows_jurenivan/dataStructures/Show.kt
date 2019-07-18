package com.example.shows_jurenivan.dataStructures

import android.os.Parcelable
import android.support.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Show(
    @DrawableRes val image:Int,
    val name:String,
    val airDate:String,
    val listOfEpisodes:MutableList<Episode>,
    val showDescription:String
) : Parcelable