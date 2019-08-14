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
import com.example.shows_jurenivan.data.repositories.ShowsDatabaseRepositoryRepository
import com.example.shows_jurenivan.ui.activities.LoginActivity

class ShowViewModel : ViewModel() {

    private val episodesLiveData = MutableLiveData<ResponseData<List<Episode>>>()
    private val showLiveData = MutableLiveData<ResponseData<Show>>()
    private val likeMutableStatusLiveData = MutableLiveData<Int>()

    var episodeListData = ResponseData<List<Episode>>(isSuccessful = false)
    private var showData = ResponseData<Show>(isSuccessful = false)

    val episodesliveData: LiveData<ResponseData<List<Episode>>>
        get() = episodesLiveData

    val showliveData: LiveData<ResponseData<Show>>
        get() = showLiveData

    val likeStatusLiveData: LiveData<Int>
        get() = likeMutableStatusLiveData

    init {
        episodesLiveData.value = episodeListData
        showLiveData.value = showData


        InternetRepository.getEpisodesLiveData().observeForever {
            if (it != null && it.isSuccessful) {
                val sortedData = it.data?.sortedWith(compareBy({ it.season }, { it.episode }))
                episodesLiveData.value = ResponseData(sortedData, true)
            } else episodesLiveData.value = it
        }
        InternetRepository.getShowLiveData().observeForever {
            showLiveData.value = it
        }

    }

    fun setShow(showId: String) {
        InternetRepository.fetchShow(showId)
        InternetRepository.fetchEpisodes(showId)
        ShowsDatabaseRepositoryRepository.getShow(showId).observeForever {
            likeMutableStatusLiveData.value = it?.likeStatus
        }
    }

    fun postEpisode(episode: Episode, photoPath: String?) {
        episode.imageUrl = photoPath
        InternetRepository.postEpisode(episode, getTokenFromSharedPref())
    }

    fun likeShow(showId: String) {
        var flag = true
        ShowsDatabaseRepositoryRepository.getShow(showId).observeForever { show ->
            if (flag) {
                flag = false
                if (show?.likeStatus == null) {
                    InternetRepository.likeShow(showId, getTokenFromSharedPref())
                    if (show != null) {
                        show.likeStatus = 1
                        likeMutableStatusLiveData.value = 1
                        show.likesCount = showLiveData.value?.data?.likesCount?.plus(1) ?: 1
                        show.description = showLiveData.value?.data?.description
                        showLiveData.value = ResponseData(data = show, isSuccessful = true)
                        ShowsDatabaseRepositoryRepository.updateShow(show)
                    }
                } else if (show?.likeStatus == -1) {
                    InternetRepository.likeShow(showId, getTokenFromSharedPref())
                    InternetRepository.likeShow(showId, getTokenFromSharedPref())
                    if (show != null) {
                        show.likeStatus = 1
                        likeMutableStatusLiveData.value = 1
                        show.likesCount = showLiveData.value?.data?.likesCount?.plus(2) ?: 1
                        showLiveData.value = ResponseData(data = show, isSuccessful = true)
                        ShowsDatabaseRepositoryRepository.updateShow(show)
                    }
                }
            }
        }
    }

    fun disLikeShow(showId: String) {
        var flag = true
        ShowsDatabaseRepositoryRepository.getShow(showId).observeForever { show ->
            if (flag) {
                flag = false
                if (show?.likeStatus == null) {
                    likeMutableStatusLiveData.value = -1
                    InternetRepository.disLikeShow(showId, getTokenFromSharedPref())
                    if (show != null) {
                        show.likeStatus = -1
                        show.likesCount = showLiveData.value?.data?.likesCount?.minus(1) ?: -1
                        showLiveData.value = ResponseData(data = show, isSuccessful = true)
                        ShowsDatabaseRepositoryRepository.updateShow(show)
                    }
                } else
                    if (show?.likeStatus == 1) {
                        likeMutableStatusLiveData.value = -1
                        InternetRepository.disLikeShow(showId, getTokenFromSharedPref())
                        InternetRepository.disLikeShow(showId, getTokenFromSharedPref())
                        if (show != null) {
                            show.likeStatus = -1
                            show.likesCount = showLiveData.value?.data?.likesCount?.minus(2) ?: 1
                            showLiveData.value = ResponseData(data = show, isSuccessful = true)
                            ShowsDatabaseRepositoryRepository.updateShow(show)
                        }
                    }
            }
        }
    }

    private fun getTokenFromSharedPref() =
        MyShowsApp.instance.getSharedPreferences(
            LoginActivity.LOGIN, Context.MODE_PRIVATE
        ).getString(LoginActivity.TOKEN, "") ?: ""

}