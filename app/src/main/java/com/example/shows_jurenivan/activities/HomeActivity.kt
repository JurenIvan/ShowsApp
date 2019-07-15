package com.example.shows_jurenivan.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.ShowsAdapter
import com.example.shows_jurenivan.dataStructures.Show
import kotlinx.android.synthetic.main.activity_shows.*

val shows = listOf(
    Show(R.drawable.p220, "Big bag theory", "(2007-2019)", ArrayList(), "Nice show"),
    Show(R.drawable.p220_2, "Sherlock", "(2010-)", ArrayList(), "Nice show"),
    Show(R.drawable.p220_3, "the office", "(2012-2014)", ArrayList(), "Nice show"),
    Show(R.drawable.p220_4, "Big bag theory", "(2007-2019)", ArrayList(), "Nice show"),
    Show(R.drawable.p220_5, "Sherlock", "(2010-)", ArrayList(), "Nice show"),
    Show(R.drawable.p220, "the office", "(2012-2014)", ArrayList(), "Nice show"),
    Show(R.drawable.p220_2, "Big bag theory", "(2007-2019)", ArrayList(), "Nice show"),
    Show(R.drawable.p220_3, "Sherlock", "(2010-)", ArrayList(), "Nice show"),
    Show(R.drawable.p220_4, "the office", "(2012-2014)", ArrayList(), "Nice show")
)

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        recyclerViewShows.layoutManager = LinearLayoutManager(this)
        recyclerViewShows.adapter = ShowsAdapter(shows, this)
    }

    fun showClicked(position: Int) {

        val intent = Intent(baseContext, ShowActivity::class.java)
        intent.putExtra(RESULT_SHOW, position)
        startActivity(intent)
    }


}
