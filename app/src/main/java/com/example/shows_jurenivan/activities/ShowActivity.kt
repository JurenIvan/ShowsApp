package com.example.shows_jurenivan.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.example.shows_jurenivan.EpisodeAdapter
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.dataStructures.Episode
import com.example.shows_jurenivan.dataStructures.Show
import kotlinx.android.synthetic.main.activity_episodes.*

const val REQUEST_ID_ADD_EPISODE = 1

class ShowActivity : AppCompatActivity() {

    companion object{
        const val RESULT_SHOW = "Show"

        fun newInstance(context: Context, position: Int): Intent {
            val intent = Intent(context, ShowActivity::class.java)
            intent.putExtra(RESULT_SHOW, position)
            return intent
        }
    }

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var show: Show

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episodes)

        recyclerViewEpisodes.layoutManager = LinearLayoutManager(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val position = intent.getIntExtra(RESULT_SHOW, 0)
        show = shows[position]
        showDescription.text = show.showDescription
        showPicture.setImageResource(show.image)
        showName.text=show.name
        supportActionBar?.title = ""

        recyclerViewEpisodes.adapter = EpisodeAdapter(show.listOfEpisodes,this)

        checkEmptiness()

        fab.setOnClickListener {
            val intent = Intent(this, AddEpisodeActivity::class.java)
            intent.putExtra(RESULT_SHOW_NUM, position)
            startActivityForResult(intent, REQUEST_ID_ADD_EPISODE)
            checkEmptiness()
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val episode = data!!.getParcelableExtra<Episode>(RESULT)
                val position=data!!.getIntExtra(RESULT_SHOW_NUM,-1)

                shows[position].listOfEpisodes.add(episode)

                shows[position].listOfEpisodes.sortWith(compareBy({it.seasonNumber},{it.episodeNumber}))
                recyclerViewEpisodes.adapter!!.notifyItemInserted(shows[position].listOfEpisodes.size-1)
                checkEmptiness()
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }

    }//onActivityResult

    private fun checkEmptiness() {
        if (show.listOfEpisodes.isEmpty()) {
            noEntriesLayout.visibility = View.VISIBLE
        } else {
            noEntriesLayout.visibility = View.GONE
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
