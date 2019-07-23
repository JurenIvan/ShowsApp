package com.example.shows_jurenivan.data.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.example.shows_jurenivan.data.repositories.EpisodesRepository
import com.example.shows_jurenivan.data.repositories.ShowsRepository
import com.example.shows_jurenivan.data.dataStructures.Episode
import com.example.shows_jurenivan.data.dataStructures.Show

class EpisodesViewModel : ViewModel() {

    private val episodesLiveData = MutableLiveData<List<Episode>>()
    private val showLiveData = MutableLiveData<Show>()

    private var episodeList = listOf<Episode>()
    private var showId = 0
    private lateinit var show: Show

    var observerShows: Observer<List<Show>> = Observer {
         showLiveData.value = it?.get(showId)
    }

    var observerEpisodes: Observer<List<Episode>> = Observer {
        episodesLiveData.value = it?: listOf()
    }

    val episodesliveData: LiveData<List<Episode>>
        get() = episodesLiveData

    val showliveData: LiveData<Show>
        get() = showLiveData

    init {
        episodesLiveData.value = episodeList
        showLiveData.value = ShowsRepository.getShow(showId)

        ShowsRepository.getShows().observeForever(observerShows)
        EpisodesRepository.getLiveData().observeForever(observerEpisodes)
    }

    fun setShow(showId: Int) {
        this.showId = showId
        show = ShowsRepository.getShow(showId)
        showLiveData.value=show
        EpisodesRepository.setEpisodes(showId)
    }

    fun addEpisode(episode: Episode) {
        EpisodesRepository.addEpisodeToShow(episode, showId)
    }

}