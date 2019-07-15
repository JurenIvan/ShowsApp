package com.example.shows_jurenivan.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.dataStructures.Episode
import com.example.shows_jurenivan.dataStructures.Show
import kotlinx.android.synthetic.main.activity_episodes.*

const val REQUEST_ID_ADD_EPISODE = 1
const val RESULT_SHOW = "Show"

class ShowActivity : AppCompatActivity() {

    private var lastEpNumber = 0
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var show: Show

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episodes)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val position = intent.getIntExtra(RESULT_SHOW, 0)
        show = shows[position]
        showDescription.text = show.showDescription

        supportActionBar?.title = show.name

        val stringList = show.listOfEpisodes.map { e -> "${e.episodeNumber} ${e.title}" }
        adapter = ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, stringList)
        listOfItems.adapter = adapter

        checkEmptiness()

        fab.setOnClickListener {
            lastEpNumber = show.listOfEpisodes.size
            val intent = Intent(this, AddEpisodeActivity::class.java)
            intent.putExtra(RESULT_EPISODE_NUM, show.listOfEpisodes.size)
            intent.putExtra(RESULT_SHOW_NUM, position)
            startActivityForResult(intent, REQUEST_ID_ADD_EPISODE)
            checkEmptiness()
        }

    }

    private fun checkEmptiness() {
        if (show.listOfEpisodes.isEmpty()) {
            noEntriesLayout.visibility = View.VISIBLE
        } else {
            noEntriesLayout.visibility = View.GONE
        }
        listOfItems.invalidateViews()
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val title = data!!.getStringExtra(RESULT_TITLE)
                val description = data.getStringExtra(RESULT_DESC)
                val episodeNum = data.getIntExtra(RESULT_EPISODE_NUM, 0)
                val showNum = data.getIntExtra(RESULT_SHOW_NUM, 0)
                val episode = Episode(episodeNum + 1, title, description)

                shows[showNum].listOfEpisodes.add(episode)
                adapter.add("${episode.episodeNumber} ${episode.title}")
                checkEmptiness()
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }

    }//onActivityResult

}
