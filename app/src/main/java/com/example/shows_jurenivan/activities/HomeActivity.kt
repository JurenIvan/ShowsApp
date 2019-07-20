package com.example.shows_jurenivan.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.Adapters.ShowsAdapter
import com.example.shows_jurenivan.shows
import kotlinx.android.synthetic.main.activity_shows.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        recyclerViewShows.layoutManager = LinearLayoutManager(this)
        recyclerViewShows.adapter = ShowsAdapter(shows, this)
    }
}
