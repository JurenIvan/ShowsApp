package com.example.shows_jurenivan.data.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.example.shows_jurenivan.data.repositories.ShowsRepository
import com.example.shows_jurenivan.data.dataStructures.Show

class ShowsViewModel : ViewModel(), Observer<List<Show>> {

    private val showsLiveData = MutableLiveData<List<Show>>()

    val liveData: LiveData<List<Show>>
        get() = showsLiveData

    private var showsList = listOf<Show>()

    init {
        showsLiveData.value = showsList
        ShowsRepository.getShows().observeForever(this)
    }

    override fun onChanged(shows: List<Show>?) {
        showsList = shows ?: listOf()
        showsLiveData.value = showsList
    }

    override fun onCleared() {
        ShowsRepository.getShows().removeObserver(this)
    }

}
