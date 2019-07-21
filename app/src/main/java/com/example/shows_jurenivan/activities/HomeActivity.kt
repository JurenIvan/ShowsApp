package com.example.shows_jurenivan.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.adapters.ShowsAdapter
import com.example.shows_jurenivan.shows
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerViewShows.layoutManager = LinearLayoutManager(this)
        recyclerViewShows.adapter = ShowsAdapter(shows, this)
    }
}
