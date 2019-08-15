package com.example.shows_jurenivan.data.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.shows_jurenivan.data.dataStructures.ResponseData
import com.example.shows_jurenivan.data.dataStructures.Token
import com.example.shows_jurenivan.data.dataStructures.User
import com.example.shows_jurenivan.data.repositories.InternetRepository

class LoginViewModel : ViewModel() {

    private val tokenLiveData = MutableLiveData<ResponseData<Token>>()

    private val loadingMutableLiveData = MutableLiveData<Boolean>()
    private val errorMutableLiveData = MutableLiveData<String>()

    val loadingLiveData: LiveData<Boolean>
        get() = loadingMutableLiveData

    val errorLiveData: LiveData<String>
        get() = errorMutableLiveData

    val liveData: LiveData<ResponseData<Token>>
        get() = tokenLiveData

    private var token: ResponseData<Token>? = ResponseData(isSuccessful = false)

    init {
        tokenLiveData.value = token
        InternetRepository.getTokenLiveData().observeForever {
            tokenLiveData.value = it
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

    fun loginUser(user: User) {
        InternetRepository.doLogin(user)
    }


}