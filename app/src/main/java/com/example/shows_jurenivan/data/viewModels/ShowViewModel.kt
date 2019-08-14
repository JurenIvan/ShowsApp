package com.example.shows_jurenivan.data.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.shows_jurenivan.MyShowsApp
import com.example.shows_jurenivan.data.dataStructures.Episode
import com.example.shows_jurenivan.data.dataStructures.ResponseData
import com.example.shows_jurenivan.data.dataStructures.Show
import com.example.shows_jurenivan.data.repositories.InternetRepository
import com.example.shows_jurenivan.ui.activities.LoginActivity

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
                val sortedData = it.data?.sortedWith(compareBy({ it.season }, { it.episode }))
                episodesLiveData.value = ResponseData(sortedData, true)
            } else episodesLiveData.value = it
        }
        InternetRepository.getShowLiveData().observeForever { showLiveData.value = it }
    }

    fun setShow(showId: String) {
        InternetRepository.fetchShow(showId)
        InternetRepository.fetchEpisodes(showId)
    }

    fun postEpisode(episode: Episode, photoPath: String?) {
        episode.imageUrl = photoPath
        InternetRepository.postEpisode(episode, getTokenFromSharedPref())
    }

    fun likeShow(showId: String): Boolean {
        InternetRepository.likeShow(showId, getTokenFromSharedPref())
        return true
    }

    fun disLikeShow(showId: String): Boolean {
        InternetRepository.disLikeShow(showId, getTokenFromSharedPref())
        return true
    }

    private fun getTokenFromSharedPref() =
        MyShowsApp.instance.getSharedPreferences(
            LoginActivity.LOGIN, Context.MODE_PRIVATE
        ).getString(LoginActivity.TOKEN, "")?:""

}