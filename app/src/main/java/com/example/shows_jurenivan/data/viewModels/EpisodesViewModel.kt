package com.example.shows_jurenivan.data.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.example.shows_jurenivan.data.repositories.EpisodesRepository
import com.example.shows_jurenivan.data.repositories.ShowsRepository
import com.example.shows_jurenivan.data.dataStructures.Episode
import com.example.shows_jurenivan.data.dataStructures.Show

class EpisodesViewModel : ViewModel(), Observer<List<Episode>> {

    private val episodesLiveData = MutableLiveData<List<Episode>>()
    private var episodeList = listOf<Episode>()

    private var showId = -1
    lateinit var show: Show

    val liveData: LiveData<List<Episode>>
        get() = episodesLiveData

    init {
        episodesLiveData.value = episodeList
        EpisodesRepository.getLiveData().observeForever(this)
    }

    fun setShow(showId: Int) {
        this.showId = showId
        show = ShowsRepository.getShow(showId)
        EpisodesRepository.setEpisodes(showId)
    }

    fun addEpisode(episode: Episode) {
        EpisodesRepository.addEpisodeToShow(episode, showId)
    }

    override fun onChanged(episodes: List<Episode>?) {
        episodeList = episodes ?: listOf()
        episodesLiveData.value = episodeList
    }

}