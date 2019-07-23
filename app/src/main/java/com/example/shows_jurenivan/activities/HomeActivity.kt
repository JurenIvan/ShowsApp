package com.example.shows_jurenivan.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.adapters.ShowsAdapter
import com.example.shows_jurenivan.data.viewModels.ShowsViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: ShowsViewModel
    private lateinit var adapter: ShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        adapter = ShowsAdapter(this)
        recyclerViewShows.layoutManager = LinearLayoutManager(this)
        recyclerViewShows.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
        viewModel.liveData.observe(this, Observer { shows ->
            if (shows != null) {
                adapter.setData(shows)
            }else{
                adapter.setData(listOf())
            }
        })

    }
}
