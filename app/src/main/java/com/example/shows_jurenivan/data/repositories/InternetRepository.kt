package com.example.shows_jurenivan.data.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.shows_jurenivan.data.Api
import com.example.shows_jurenivan.data.RetrofitClient
import com.example.shows_jurenivan.data.dataStructures.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


object InternetRepository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)

    private val showsLiveData = MutableLiveData<ResponseData<List<Show>>>()
    private val showLiveData = MutableLiveData<ResponseData<Show>>()
    private val episodesLiveData = MutableLiveData<ResponseData<List<Episode>>>()
    private val userLiveData = MutableLiveData<ResponseData<User>>()
    private val tokenLiveData = MutableLiveData<ResponseData<Token>>()
    private val episodeLiveData = MutableLiveData<ResponseData<Episode>>()
    private val commentsLiveData = MutableLiveData<ResponseData<List<Comment>>>()


    fun getShowsLiveData(): LiveData<ResponseData<List<Show>>> = showsLiveData
    fun getShowLiveData(): LiveData<ResponseData<Show>> = showLiveData
    fun getEpisodesLiveData(): LiveData<ResponseData<List<Episode>>> = episodesLiveData
    fun getUserLiveData(): LiveData<ResponseData<User>> = userLiveData
    fun getTokenLiveData(): LiveData<ResponseData<Token>> = tokenLiveData
    fun getEpisodeLiveData(): LiveData<ResponseData<Episode>> = episodeLiveData
    fun getCommentsLiveData(): LiveData<ResponseData<List<Comment>>> = commentsLiveData


    fun fetchShows() {
        apiService?.getShows()?.enqueue(object : Callback<ResponseData<List<Show>>> {
            override fun onFailure(call: Call<ResponseData<List<Show>>>, t: Throwable) {
                showsLiveData.value = ResponseData(isSuccessful = false)
            }

            override fun onResponse(
                call: Call<ResponseData<List<Show>>>,
                response: Response<ResponseData<List<Show>>>
            ) {
                with(response) {
                    if (isSuccessful && body() != null)
                        showsLiveData.value = ResponseData(isSuccessful = true, data = body()?.data)
                    else
                        showsLiveData.value = ResponseData(isSuccessful = false)
                }
            }
        })
    }

    fun fetchShow(showId: String) {
        apiService?.getShow(showId)?.enqueue(object : Callback<ResponseData<Show>> {

            override fun onFailure(call: Call<ResponseData<Show>>, t: Throwable) {
                showLiveData.value = ResponseData(isSuccessful = false)
            }

            override fun onResponse(call: Call<ResponseData<Show>>, response: Response<ResponseData<Show>>) {
                with(response) {
                    if (isSuccessful && body() != null)
                        showLiveData.value = ResponseData(isSuccessful = true, data = body()?.data)
                    else
                        showLiveData.value = ResponseData(isSuccessful = false)
                }
            }
        })
    }

    fun fetchEpisodes(showId: String) {
        apiService?.getEpisodes(showId)?.enqueue(object : Callback<ResponseData<List<Episode>>> {

            override fun onFailure(call: Call<ResponseData<List<Episode>>>, t: Throwable) {
                episodesLiveData.value = ResponseData(isSuccessful = false)
            }

            override fun onResponse(
                call: Call<ResponseData<List<Episode>>>,
                response: Response<ResponseData<List<Episode>>>
            ) {
                with(response) {
                    if (isSuccessful && body() != null)
                        episodesLiveData.value = ResponseData(isSuccessful = true, data = body()?.data)
                    else
                        episodesLiveData.value = ResponseData(isSuccessful = false)
                }
            }
        })
    }

    fun fetchEpisode(episodeId: String) {
        apiService?.getEpisode(episodeId)?.enqueue(object : Callback<ResponseData<Episode>> {

            override fun onFailure(call: Call<ResponseData<Episode>>, t: Throwable) {
                episodeLiveData.value = ResponseData(isSuccessful = false)
            }

            override fun onResponse(call: Call<ResponseData<Episode>>, response: Response<ResponseData<Episode>>) {
                with(response) {
                    if (isSuccessful && body() != null)
                        episodeLiveData.value = ResponseData(isSuccessful = true, data = body()?.data)
                    else
                        episodeLiveData.value = ResponseData(isSuccessful = false)

                }
            }
        })
    }

    fun fetchComments(episodeId: String) {
        apiService?.getComments(episodeId)?.enqueue(object : Callback<ResponseData<List<Comment>>> {
            override fun onFailure(call: Call<ResponseData<List<Comment>>>, t: Throwable) {
                commentsLiveData.value = ResponseData(isSuccessful = false)
            }

            override fun onResponse(
                call: Call<ResponseData<List<Comment>>>,
                response: Response<ResponseData<List<Comment>>>
            ) {
                with(response) {
                    if (isSuccessful && body() != null)
                        commentsLiveData.value = ResponseData(isSuccessful = true, data = body()?.data)
                    else
                        commentsLiveData.value = ResponseData(isSuccessful = false)
                }
            }
        })
    }

    fun postComment(comment: Comment, token: String) {
        apiService?.postComment(token, comment)?.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {}
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}
        })
    }

    fun postEpisode(episode: Episode, token: String) {

        if (episode.imageUrl.isNullOrBlank().not()) {

            apiService?.uploadMedia(token, RequestBody.create(MediaType.parse("image/jpg"), File(episode.imageUrl)))
                ?.enqueue(object : Callback<ResponseData<MediaResponse>> {

                    override fun onFailure(call: Call<ResponseData<MediaResponse>>, t: Throwable) {
                        return

                    }

                    override fun onResponse(
                        call: Call<ResponseData<MediaResponse>>,
                        response: Response<ResponseData<MediaResponse>>
                    ) {
                        episode.imageUrl = response.body()?.data?.path
                        episode.mediaId = response.body()?.data?._id

                        uploadEpisode(token, episode)
                    }
                })
        } else {
            uploadEpisode(token, episode)
        }


    }

    private fun uploadEpisode(token: String, episode: Episode) {
        apiService?.postEpisode(token, episode)?.enqueue(object : Callback<Void> {

            override fun onFailure(call: Call<Void>, t: Throwable) {}
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}

        })
    }

    fun likeShow(showId: String, token: String) {
        apiService?.likeShow(token, showId)?.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {}
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}
        })
    }

    fun disLikeShow(showId: String, token: String) {
        apiService?.disLikeShow(token, showId)?.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {}
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}
        })
    }


    fun doLogin(user: User) {
        if (user.email == null) return

        apiService?.loginUser(user)?.enqueue(object : retrofit2.Callback<ResponseData<Token>> {

            override fun onFailure(call: Call<ResponseData<Token>>, t: Throwable) {
                tokenLiveData.value = ResponseData(isSuccessful = false)
            }

            override fun onResponse(call: Call<ResponseData<Token>>, response: Response<ResponseData<Token>>) {
                with(response) {
                    if (code() == 200) {
                        tokenLiveData.value = ResponseData(isSuccessful = true, data = body()?.data)
                    } else {
                        tokenLiveData.value = ResponseData(isSuccessful = false)
                    }
                }
            }
        })
    }

    fun doRegistration(user: User) {
        if (user.email == null) return

        apiService?.registerUser(user)?.enqueue(object : retrofit2.Callback<ResponseData<User>> {

            override fun onFailure(call: Call<ResponseData<User>>, t: Throwable) {
                userLiveData.value = ResponseData(isSuccessful = false)
            }

            override fun onResponse(call: Call<ResponseData<User>>, response: Response<ResponseData<User>>) {
                with(response) {
                    if (code() == 201)
                        userLiveData.value = ResponseData(isSuccessful = true, data = body()?.data)
                    else
                        userLiveData.value = ResponseData(isSuccessful = false)
                }
            }
        })
    }


}