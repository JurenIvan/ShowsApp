package com.example.shows_jurenivan.data.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.shows_jurenivan.data.dataStructures.Episode
import com.example.shows_jurenivan.data.dataStructures.ResponseData
import com.example.shows_jurenivan.data.repositories.InternetRepository


class EpisodeViewModel : ViewModel() {

    private val episodeLiveData = MutableLiveData<ResponseData<Episode>>()
    private var episodeData = ResponseData<Episode>(isSuccessful = false)

    val episodeliveData: LiveData<ResponseData<Episode>>
        get() = episodeLiveData

    init {
        episodeLiveData.value = this.episodeData

        InternetRepository.getEpisodeLiveData().observeForever {
            if (!(it == null || it.data == null || !it.isSuccessful)) {
                episodeLiveData.value = ResponseData(data = it.data, isSuccessful = true)
            } else episodeLiveData.value = ResponseData(data = null, isSuccessful = false)
        }
    }

    fun setEpisode(episodeId: String) {
        InternetRepository.fetchEpisode(episodeId)
    }

}