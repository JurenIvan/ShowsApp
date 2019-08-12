package com.example.shows_jurenivan.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.adapters.EpisodeAdapter
import com.example.shows_jurenivan.data.RetrofitClient
import com.example.shows_jurenivan.data.dataStructures.Episode
import com.example.shows_jurenivan.data.dataStructures.ResponseData
import com.example.shows_jurenivan.data.viewModels.EpisodeViewModel
import com.example.shows_jurenivan.data.viewModels.ShowViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_show.*
import kotlinx.android.synthetic.main.fragment_show.colappsingtoolbar
import kotlinx.android.synthetic.main.fragment_show.fab
import kotlinx.android.synthetic.main.fragment_show.noEntriesLayout
import kotlinx.android.synthetic.main.fragment_show.recyclerViewEpisodes
import kotlinx.android.synthetic.main.fragment_show.showDescription


class EpisodeFragment : Fragment() {

    companion object {
        const val EPISODEID_KEY = "EpisodeID"

        fun newInstance(episodeId: String?) = ShowFragment().apply {
            val args = Bundle()
            args.putString(EPISODEID_KEY, episodeId)
            arguments = args
        }
    }

    private lateinit var viewModel: EpisodeViewModel
    private var episodeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.episodeId = arguments?.getString(EPISODEID_KEY)
        viewModel = ViewModelProviders.of(this).get(EpisodeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        episodeId?.let { viewModel.setEpisode(it) }
        setHasOptionsMenu(true)

        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_light)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        viewModel.episodeliveData.observe(this, Observer {
            if (it != null) {
                .text = it.data?.description

                Picasso.get().load(RetrofitClient.BASE_URL + it.data?.imageURL)
                    .placeholder(R.drawable.ic_img_placeholder_episodes).error(android.R.drawable.stat_notify_error)
                    .into(imgPlaceholder)

                activity?.colappsingtoolbar?.title = it.data?.title
            }
        })

        fab.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                addToBackStack("AddEpisodeFragment")
                if (showId != null) {
                    replace(R.id.item_detail_container, AddEpisodeFragment.newInstance(showId!!))
                }
                commit()
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