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

    private val commentsLiveData = MutableLiveData<ResponseData<List<Comment>>>()
    private var commentsData = ResponseData<List<Comment>>(isSuccessful = false)

    val commentsliveData: LiveData<ResponseData<List<Comment>>>
        get() = commentsLiveData

    init {
        commentsLiveData.value = this.commentsData

        InternetRepository.getCommentsLiveData().observeForever {
            if (!(it?.data == null || !it.isSuccessful)) {
                commentsLiveData.value = ResponseData(data = it.data, isSuccessful = true)
            } else commentsLiveData.value = ResponseData(data = null, isSuccessful = false)
        }
    }

    fun setEpisode(episodeId: String) {
        InternetRepository.fetchComments(episodeId)
    }

    fun postComment(comment: Comment) {

        val sharedPreferences = MyShowsApp.instance.getSharedPreferences(LoginActivity.LOGIN, Context.MODE_PRIVATE)

        comment.userEmail = sharedPreferences.getString(LoginActivity.USERNAME,"")

        InternetRepository.postComment(comment, sharedPreferences.getString(LoginActivity.TOKEN, ""))
    }
}