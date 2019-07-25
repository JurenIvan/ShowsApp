package com.example.shows_jurenivan.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.adapters.ShowsAdapter
import com.example.shows_jurenivan.data.viewModels.ShowsViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {

    private lateinit var viewModel: ShowsViewModel
    private lateinit var adapter: ShowsAdapter

    private var twoPane: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }



        adapter = ShowsAdapter { position ->
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                var showFragment = ShowFragment()
                showFragment.setShow( position)
                if(twoPane)  {

                    replace(R.id.item_detail_container, showFragment)
                }

                else     replace(R.id.fragmentContainer, showFragment)
                addToBackStack("ShowDisplay")
                commit()
            }
        }

        recyclerViewShows.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewShows.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
        viewModel.liveData.observe(this, Observer { shows ->
            if (shows != null) {
                adapter.setData(shows)
            } else {
                adapter.setData(listOf())
            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)


    }


}