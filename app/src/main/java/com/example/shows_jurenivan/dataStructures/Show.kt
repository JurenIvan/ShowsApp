package com.example.shows_jurenivan.dataStructures

data class Show(
    val image:Int,
    val name:String,
    val airDate:String,
    val listOfEpisodes:MutableList<Episode>,
    val showDescription:String
)