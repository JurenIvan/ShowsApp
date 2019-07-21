package com.example.shows_jurenivan.data.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.example.shows_jurenivan.MyShowsApp
import com.example.shows_jurenivan.data.dataStructures.Episode
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

object EpisodesRepository {

    private const val FILENAME = "episodes"
    private val episodesLiveData = MutableLiveData<List<Episode>>()

    private var episodesList: MutableList<Episode> = mutableListOf()

    init {
        episodesLiveData.value = episodesList
    }

    fun getLiveData(): LiveData<List<Episode>> =
        episodesLiveData

    fun setEpisodes(showId: Int) {
        episodesList = readEpisodes(showId)
        episodesLiveData.value = episodesList
    }

    fun addEpisodeToShow(episode: Episode, showId: Int) {
        episodesList = readEpisodes(showId)
        episodesList.add(episode)
        episodesList.sortWith(compareBy({ it.seasonNumber }, { it.episodeNumber }))
        episodesLiveData.value = episodesList

        ObjectOutputStream(MyShowsApp.instance.openFileOutput("$FILENAME$showId", Context.MODE_PRIVATE)).use {
            it.writeObject(episodesList as List<Serializable>)
        }
    }

    private fun readEpisodes(showId: Int): MutableList<Episode> {
        return try {
            ObjectInputStream(MyShowsApp.instance.openFileInput("$FILENAME$showId")).use {
                it.readObject() as MutableList<Episode>
            }
        } catch (e: Exception) {
            mutableListOf()
        }


    }

}