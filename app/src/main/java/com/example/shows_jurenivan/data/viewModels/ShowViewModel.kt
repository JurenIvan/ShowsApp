package com.example.shows_jurenivan.data.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.shows_jurenivan.data.dataStructures.Episode
import com.example.shows_jurenivan.data.dataStructures.ResponseData
import com.example.shows_jurenivan.data.dataStructures.Show
import com.example.shows_jurenivan.data.repositories.EpisodesRepository
import com.example.shows_jurenivan.data.repositories.InternetRepository

class ShowViewModel : ViewModel() {

    private val episodesLiveData = MutableLiveData<ResponseData<List<Episode>>>()
    private val showLiveData = MutableLiveData<ResponseData<Show>>()

    var episodeListData = ResponseData<List<Episode>>(isSuccessful = false)
    private var showData = ResponseData<Show>(isSuccessful = false)

    val episodesliveData: LiveData<ResponseData<List<Episode>>>
        get() = episodesLiveData

    val showliveData: LiveData<ResponseData<Show>>
        get() = showLiveData

    init {
        episodesLiveData.value = episodeListData
        showLiveData.value = showData

        InternetRepository.getEpisodesLiveData().observeForever {
            if (it != null && it.isSuccessful) {
                var sortedData = it.data?.sortedWith(compareBy({ it.season }, { it.episode }))
                episodesLiveData.value = ResponseData(sortedData, true)
            } else episodesLiveData.value = it
        }
        InternetRepository.getShowLiveData().observeForever { showLiveData.value = it }
    }

    fun setShow(showId: String) {
        InternetRepository.fetchShow(showId)
        InternetRepository.fetchEpisodes(showId)
    }

    fun addEpisode(episode: Episode) {
        showData.data?.id?.let { EpisodesRepository.addEpisodeToShow(episode, it) }
    }

}