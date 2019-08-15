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

    private val loadingMutableLiveData = MutableLiveData<Boolean>()
    private val errorMutableLiveData = MutableLiveData<String>()

    val loadingLiveData: LiveData<Boolean>
        get() = loadingMutableLiveData

    val errorLiveData: LiveData<String>
        get() = errorMutableLiveData

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

                val filteredList = mutableListOf<Episode>()
                it.data?.forEach { ep -> if (checkParameters(ep)) filteredList.add(ep) }

                val sortedData = filteredList.sortedWith(compareBy({ ep -> ep.season }, { ep -> ep.episode }))
                episodesLiveData.value = ResponseData(sortedData, true)
            } else episodesLiveData.value = it
        }
        InternetRepository.getShowLiveData().observeForever {
            showLiveData.value = it
        }

        InternetRepository.getErrorBooleanLiveData().observeForever {
            if (it != null) {
                loadingMutableLiveData.value = it > 0
            }
        }

        InternetRepository.getErrorStringLiveData().observeForever {
            errorMutableLiveData.value = it
        }

    }

    private fun checkParameters(episode: Episode): Boolean {
        if (episode.title.isBlank()) return false
        if (episode.season.isBlank()) return false
        if (episode.episode.isBlank()) return false
        try {
            if (checkValues(Integer.parseInt(episode.episode), 99)) return false
            if (checkValues(Integer.parseInt(episode.season), 20)) return false
        } catch (e: Exception) {
            return false
        }
        return true
    }


    private fun checkValues(number: Int, upperLimit: Int): Boolean {
        if (number < 1 || number > upperLimit) return true
        return false
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
            if (flag && show != null) {
                flag = false
                when (show.likeStatus) {
                    null -> {
                        InternetRepository.likeShow(showId, getTokenFromSharedPref())
                        prepareShowAndData(show, 1, 1)
                    }
                    -1 -> {
                        InternetRepository.likeShow(showId, getTokenFromSharedPref())
                        InternetRepository.likeShow(showId, getTokenFromSharedPref())
                        prepareShowAndData(show, 1, 2)
                    }
                }
            }
        }
    }

    private fun prepareShowAndData(show: Show, likeStatus: Int, change: Int) {
        likeMutableStatusLiveData.value = likeStatus
        show.likeStatus = likeStatus
        show.likesCount = showLiveData.value?.data?.likesCount?.plus(change) ?: likeStatus
        show.description = showLiveData.value?.data?.description
        showLiveData.value = ResponseData(data = show, isSuccessful = true)
        ShowsDatabaseRepositoryRepository.updateShow(show)
    }

    fun disLikeShow(showId: String) {
        var flag = true
        ShowsDatabaseRepositoryRepository.getShow(showId).observeForever { show ->
            if (flag && show != null) {
                flag = false
                when (show.likeStatus) {
                    null -> {
                        InternetRepository.disLikeShow(showId, getTokenFromSharedPref())
                        prepareShowAndData(show, -1, -1)
                    }
                    1 -> {
                        InternetRepository.disLikeShow(showId, getTokenFromSharedPref())
                        InternetRepository.disLikeShow(showId, getTokenFromSharedPref())
                        prepareShowAndData(show, -1, -2)
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