package com.example.shows_jurenivan.data.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.shows_jurenivan.data.dataStructures.ResponseData
import com.example.shows_jurenivan.data.dataStructures.Token
import com.example.shows_jurenivan.data.dataStructures.User
import com.example.shows_jurenivan.data.repositories.InternetRepository

class RegisterViewModel : ViewModel() {

    private val registerMutableLiveData = MutableLiveData<ResponseData<User>>()
    private val tokenMutableLiveData = MutableLiveData<ResponseData<Token>>()

    val registerLiveData: LiveData<ResponseData<User>>
        get() = registerMutableLiveData
    val tokenLiveData: LiveData<ResponseData<Token>>
        get() = tokenMutableLiveData


    private var user: ResponseData<User>? = ResponseData(isSuccessful = false)
    private var token: ResponseData<Token>? = ResponseData(isSuccessful = false)

    init {
        registerMutableLiveData.value = user
        tokenMutableLiveData.value = token

        InternetRepository.getUserLiveData().observeForever {
            registerMutableLiveData.value = it
        }
        InternetRepository.getTokenLiveData().observeForever {
            tokenMutableLiveData.value = it
        }
    }

    fun registerUser(user: User) {
        InternetRepository.doRegistration(user)
    }

    fun loginUser(user: User) {
        InternetRepository.doLogin(user)
    }

}