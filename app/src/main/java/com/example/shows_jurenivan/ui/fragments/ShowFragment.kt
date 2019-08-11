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
import com.example.shows_jurenivan.data.viewModels.ShowViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_show.*
import kotlinx.android.synthetic.main.show_details.colappsingtoolbar
import kotlinx.android.synthetic.main.show_details.fab
import kotlinx.android.synthetic.main.show_details.noEntriesLayout
import kotlinx.android.synthetic.main.show_details.recyclerViewEpisodes
import kotlinx.android.synthetic.main.show_details.showDescription


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

    private var showId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.showId = arguments?.getString(SHOWID_KEY)
        viewModel = ViewModelProviders.of(this).get(ShowViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showId?.let { viewModel.setShow(it) }

        setHasOptionsMenu(true)

        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_light)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        adapter = EpisodeAdapter()
        recyclerViewEpisodes.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewEpisodes.adapter = adapter

        viewModel.episodesliveData.observe(this, Observer { episodes ->
            if (episodes != null) episodes.data?.let { adapter.setData(it) }
            else adapter.setData(listOf())

            checkEmptiness(episodes)
        })

        viewModel.showliveData.observe(this, Observer {
            if (it != null) {
                showDescription.text = it.data?.description

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