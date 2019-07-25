package com.example.shows_jurenivan.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.adapters.EpisodeAdapter
import com.example.shows_jurenivan.data.dataStructures.Episode
import com.example.shows_jurenivan.data.dataStructures.Show
import com.example.shows_jurenivan.data.viewModels.EpisodesViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_show.*
import kotlinx.android.synthetic.main.show_details.*
import kotlinx.android.synthetic.main.show_details.colappsingtoolbar
import kotlinx.android.synthetic.main.show_details.fab
import kotlinx.android.synthetic.main.show_details.noEntriesLayout
import kotlinx.android.synthetic.main.show_details.recyclerViewEpisodes
import kotlinx.android.synthetic.main.show_details.showDescription


class ShowFragment : Fragment() {


    private lateinit var viewModel: EpisodesViewModel
    private lateinit var adapter: EpisodeAdapter

    private var showId = 0
    private lateinit var show: Show


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)
    }

    private var twoPane: Boolean = false

    fun setShow(showId: Int) {
        this.showId = showId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (item_detail_container != null) {
            twoPane = true
        }

        viewModel = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)
        viewModel.setShow(showId)


        setHasOptionsMenu(true)


            val toolbar = view.findViewById(R.id.toolbar) as Toolbar
            toolbar.setNavigationIcon(R.drawable.ic_back_arrow_light)
            toolbar.setNavigationOnClickListener {
                activity?.onBackPressed()
            }


        adapter = EpisodeAdapter()
        recyclerViewEpisodes.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewEpisodes.adapter = adapter

        viewModel.episodesliveData.observe(this, Observer { episodes ->
            if (episodes != null) {
                adapter.setData(episodes)
            } else {
                adapter.setData(listOf())
            }
            checkEmptiness(episodes)
        })

        viewModel.showliveData.observe(this, Observer {
            if (it != null) {
                this.show = it
                showDescription.text = show.showDescription

                imgPlaceholder.setImageResource(show.image)
                activity?.colappsingtoolbar?.title = show.name

                activity?.colappsingtoolbarImage?.setImageResource(show.image)

            }
        })

        fab.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                var addEpisodeFragment = AddEpisodeFragment()
                addEpisodeFragment.setShow(showId)
                addToBackStack("ShowDetails")

                if (item_detail_container != null) {
                    twoPane = true
                }

                if(twoPane) replace(R.id.item_detail_container, addEpisodeFragment)
                else replace(R.id.fragmentContainer, addEpisodeFragment)

                commit()
            }
        }



    }


    private fun checkEmptiness(episodes: List<Episode>?) {
        if (episodes.isNullOrEmpty()) {
            noEntriesLayout.visibility = View.VISIBLE
            recyclerViewEpisodes.visibility = View.GONE
        } else {
            noEntriesLayout.visibility = View.GONE
            recyclerViewEpisodes.visibility = View.VISIBLE
        }
    }


}

