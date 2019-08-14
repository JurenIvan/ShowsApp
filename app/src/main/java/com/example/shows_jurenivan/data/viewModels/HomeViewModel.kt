package com.example.shows_jurenivan.data.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.shows_jurenivan.data.dataStructures.ResponseData
import com.example.shows_jurenivan.data.dataStructures.Show
import com.example.shows_jurenivan.data.repositories.InternetRepository
import com.example.shows_jurenivan.data.repositories.ShowsDatabaseRepositoryRepository

class HomeViewModel : ViewModel() {

    private val showsLiveData = MutableLiveData<ResponseData<List<Show>>>()

    val liveData: LiveData<ResponseData<List<Show>>>
        get() = showsLiveData

    private var showsList: ResponseData<List<Show>>? = ResponseData(isSuccessful = false)

    init {
        showsLiveData.value = showsList
        InternetRepository.fetchShows()
        InternetRepository.getShowsLiveData().observeForever {
            showsLiveData.value = it
            it?.data?.forEach { show ->
                ShowsDatabaseRepositoryRepository.insertShow(show)
            }
        }
    }

}
