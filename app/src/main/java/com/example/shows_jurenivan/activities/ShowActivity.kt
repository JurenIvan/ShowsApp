package com.example.shows_jurenivan.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.example.shows_jurenivan.adapters.EpisodeAdapter
import com.example.shows_jurenivan.data.dataStructures.Show
import com.example.shows_jurenivan.data.viewModels.EpisodesViewModel
import kotlinx.android.synthetic.main.show_details.*
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.data.dataStructures.Episode


class ShowActivity : AppCompatActivity() {

    companion object {
        const val RESULT_SHOW = "Show"
        fun newInstance(context: Context, position: Int): Intent {
            val intent = Intent(context, ShowActivity::class.java)
            intent.putExtra(RESULT_SHOW, position)
            return intent
        }
    }

    private lateinit var viewModel: EpisodesViewModel
    private lateinit var adapter: EpisodeAdapter
    private lateinit var show: Show

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_details)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        adapter = EpisodeAdapter(this)
        recyclerViewEpisodes.layoutManager = LinearLayoutManager(this)
        recyclerViewEpisodes.adapter = adapter

        val position = intent.getIntExtra(RESULT_SHOW, 0)

        viewModel = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)
        viewModel.setShow(position)

        viewModel.liveData.observe(this, Observer { episodes ->
            adapter.setData(episodes!!)
            checkEmptiness(episodes)
        })
        show = viewModel.show

        showDescription.text = show.showDescription
        colappsingtoolbar.setBackgroundResource(show.image)
        colappsingtoolbar.title = show.name

        viewModel = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)


        fab.setOnClickListener {
            val intent = Intent(this, AddEpisodeActivity::class.java)
            intent.putExtra(RESULT_SHOW_NUM, position)
            startActivity(intent)
            checkEmptiness(viewModel.liveData.value)
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


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
