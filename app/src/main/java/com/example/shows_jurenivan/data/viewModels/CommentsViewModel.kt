package com.example.shows_jurenivan.data.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.shows_jurenivan.MyShowsApp
import com.example.shows_jurenivan.data.dataStructures.Comment
import com.example.shows_jurenivan.data.dataStructures.ResponseData
import com.example.shows_jurenivan.data.repositories.InternetRepository
import com.example.shows_jurenivan.ui.activities.LoginActivity

class CommentsViewModel : ViewModel() {

    private val commentsMutableLiveData = MutableLiveData<ResponseData<List<Comment>>>()
    private var commentsData = ResponseData<List<Comment>>(isSuccessful = false)


    private val loadingMutableLiveData = MutableLiveData<Boolean>()
    private val errorMutableLiveData = MutableLiveData<String>()

    val loadingLiveData: LiveData<Boolean>
        get() = loadingMutableLiveData

    val errorLiveData: LiveData<String>
        get() = errorMutableLiveData

    val commentsliveData: LiveData<ResponseData<List<Comment>>>
        get() = commentsMutableLiveData

    init {
        commentsMutableLiveData.value = this.commentsData

        InternetRepository.getCommentsLiveData().observeForever {
            if (!(it?.data == null || !it.isSuccessful)) {
                commentsMutableLiveData.value = ResponseData(data = it.data, isSuccessful = true)
            } else commentsMutableLiveData.value = ResponseData(data = null, isSuccessful = false)
        }

        InternetRepository.getErrorBooleanLiveData().observeForever {
            if (it != null) {
                loadingMutableLiveData.value = it>0
            }
        }

        InternetRepository.getErrorStringLiveData().observeForever {
            errorMutableLiveData.value = it
        }
    }

    fun setEpisode(episodeId: String) {
        InternetRepository.fetchComments(episodeId)
    }

    fun postComment(comment: Comment) {
        val sharedPreferences = MyShowsApp.instance.getSharedPreferences(LoginActivity.LOGIN, Context.MODE_PRIVATE)
        InternetRepository.postComment(comment, sharedPreferences.getString(LoginActivity.TOKEN, ""))
    }
}