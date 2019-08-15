package com.example.shows_jurenivan.ui.fragments

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.adapters.EpisodeAdapter
import com.example.shows_jurenivan.data.RetrofitClient
import com.example.shows_jurenivan.data.dataStructures.Episode
import com.example.shows_jurenivan.data.dataStructures.ResponseData
import com.example.shows_jurenivan.data.viewModels.ShowViewModel
import com.example.shows_jurenivan.isNetworkAvailable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_show.*


class ShowFragment : Fragment() {

    companion object {
        const val SHOWID_KEY = "ShowID"

        fun newInstance(showId: String?) = ShowFragment().apply {
            val args = Bundle()
            args.putString(SHOWID_KEY, showId)
            arguments = args
        }
    }

    private lateinit var viewModel: ShowViewModel
    private lateinit var adapter: EpisodeAdapter
    private var episodeList: List<Episode> = listOf()
    private var progressDialog: ProgressDialog? = null

    private lateinit var showId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.showId = arguments?.getString(SHOWID_KEY) ?: ""
        viewModel = ViewModelProviders.of(this).get(ShowViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setShow(showId)

        setHasOptionsMenu(true)
        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_light)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        adapter = EpisodeAdapter { position: Int ->
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.item_detail_container, EpisodeFragment.newInstance(episodeList[position].episodeId))
                addToBackStack("EpisodeDisplay")
                commit()
            }
        }

        recyclerViewEpisodes.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewEpisodes.adapter = adapter

        viewModel.episodesliveData.observe(this, Observer { episodes ->
            if (episodes != null) episodes.data?.let {
                adapter.setData(it)
                episodeList = it
            }
            else adapter.setData(listOf())

            checkEmptiness(episodes)
        })

        viewModel.likeStatusLiveData.observe(this, Observer {
            updateLikeDislike(it)
        })

        viewModel.showliveData.observe(this, Observer {
            if (it != null) {
                episodeDescription.text = it.data?.description

                Picasso.get().load(RetrofitClient.BASE_URL + it.data?.imageURL)
                    .placeholder(R.drawable.rc8j4).error(android.R.drawable.stat_notify_error)
                    .into(imgPlaceholder)

                activity?.colappsingtoolbar?.title = it.data?.title

                likeStatusNumberCount.text = getNumberOfLikes(it.data?.likesCount)
            }
        })

        fab.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                addToBackStack("AddEpisodeFragment")
                replace(R.id.item_detail_container, AddEpisodeFragment.newInstance(showId))
                commit()
            }
        }

        likeImg.setOnClickListener {
            viewModel.likeShow(showId)
        }
        dislikeImg.setOnClickListener {
            viewModel.disLikeShow(showId)
        }

        viewModel.errorLiveData.observe(this, Observer { errors ->
            if (errors != null && errors.isNotBlank()) {
                Toast.makeText(context, errors, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.loadingLiveData.observe(this, Observer { loading ->
            if (loading == null || !loading) {
                progressDialog?.cancel()
            } else {
                if(progressDialog==null)
                progressDialog = ProgressDialog.show(context, "Shows", "Loading", true, true)
            }
        })

        context?.let {
            if (!isNetworkAvailable(context = it)) {
                AlertDialog.Builder(it)
                    .setTitle("Info")
                    .setMessage("Seems you have no internet connection. Functionality limited. :( ")
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
            }
        }
    }

    private fun getNumberOfLikes(numOfLikes: Int?): String {
        return numOfLikes?.toString() ?: "0"
    }

    private fun updateLikeDislike(it: Int?) {
        when (it) {
            null, 0 -> {
                likeImg.isActivated = false
                dislikeImg.isActivated = false
            }
            1 -> {
                likeImg.isActivated = true
                dislikeImg.isActivated = false
            }
            -1 -> {
                likeImg.isActivated = false
                dislikeImg.isActivated = true
            }
        }
    }

    private fun checkEmptiness(episodes: ResponseData<List<Episode>>?) {
        if (episodes?.data.isNullOrEmpty()) {
            noEntriesLayout.visibility = View.VISIBLE
            recyclerViewEpisodes.visibility = View.GONE
        } else {
            noEntriesLayout.visibility = View.GONE
            recyclerViewEpisodes.visibility = View.VISIBLE
        }
    }
}