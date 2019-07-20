package com.example.shows_jurenivan.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.example.shows_jurenivan.adapters.EpisodeAdapter
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.dataStructures.Episode
import com.example.shows_jurenivan.dataStructures.Show
import com.example.shows_jurenivan.shows
import kotlinx.android.synthetic.main.activity_show.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


const val REQUEST_ID_ADD_EPISODE = 1

class ShowActivity : AppCompatActivity() {

    companion object{
        const val BASE_FILE_NAME="EpisodesOf"
        const val RESULT_SHOW = "Show"
        fun newInstance(context: Context, position: Int): Intent {
            val intent = Intent(context, ShowActivity::class.java)
            intent.putExtra(RESULT_SHOW, position)
            return intent
        }
    }

    private lateinit var show: Show
    private lateinit var list: ArrayList<Episode>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        recyclerViewEpisodes.layoutManager = LinearLayoutManager(this)

        setSupportActionBar(toolbar)

        val position = intent.getIntExtra(RESULT_SHOW, 0)
        show = shows[position]
        showDescription.text = show.showDescription
        showPicture.setImageResource(show.image)
        showName.text=show.name
        supportActionBar?.title = ""

        list= readArrayListFromStorage(baseContext, "$BASE_FILE_NAME$position.txt")
        recyclerViewEpisodes.adapter = EpisodeAdapter(list, this)
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
                val position=data.getIntExtra(RESULT_SHOW_NUM,-1)

                list=readArrayListFromStorage(baseContext, "$BASE_FILE_NAME$position.txt")
                list.add(episode)

                list.sortWith(compareBy({it.seasonNumber},{it.episodeNumber}))
                checkEmptiness()
                saveArrayListToStorage(baseContext,"$BASE_FILE_NAME$position.txt",list)
                recyclerViewEpisodes.adapter = EpisodeAdapter(list, this)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    private fun checkEmptiness() {
        if (list.isEmpty()) {
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

   private fun readArrayListFromStorage(mContext: Context, filename: String): ArrayList<Episode> {
        return try {
            val fis = mContext.openFileInput("$filename.dat")
            val ois = ObjectInputStream(fis)
            val obj = ois.readObject() as ArrayList<Episode>
            fis.close()
            obj
        } catch (e: Exception) {
            e.printStackTrace()
            ArrayList()
        }
    }

    private fun saveArrayListToStorage(mContext: Context, filename: String, list: ArrayList<Episode>) = try {

        val fos = mContext.openFileOutput("$filename.dat", Context.MODE_PRIVATE)
        val oos = ObjectOutputStream(fos)
        oos.writeObject(list)
        fos.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}
