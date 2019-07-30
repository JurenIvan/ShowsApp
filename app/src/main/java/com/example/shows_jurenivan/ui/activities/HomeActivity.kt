package com.example.shows_jurenivan.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.adapters.ShowsAdapter
import com.example.shows_jurenivan.data.dataStructures.Show
import com.example.shows_jurenivan.data.viewModels.HomeViewModel
import com.example.shows_jurenivan.ui.fragments.ShowFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_show.*

class HomeActivity : AppCompatActivity() {

    companion object {
        const val EMAIL_KEY = "user"
        const val TOKEN = "token"

        fun newInstance(context: Context, email: String, token: String): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(EMAIL_KEY, email)
            intent.putExtra(TOKEN, token)
            return intent
        }
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ShowsAdapter

    private var shows: List<Show> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.liveData.observe(this, Observer { newData ->
            if (newData != null) {
                if (newData.isSuccessful) {
                    newData.data?.let {
                        adapter.setData(it)
                        shows = it
                    }
                }
            }
        })

        adapter = ShowsAdapter { position ->
            supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.item_detail_container, ShowFragment.newInstance(shows[position].id))
                addToBackStack("ShowDisplay")
                commit()
            }
        }
        recyclerViewShows.layoutManager = LinearLayoutManager(this)
        recyclerViewShows.adapter = adapter
    }

    override fun onBackPressed() {
        val e: Fragment? = supportFragmentManager.findFragmentById(R.id.item_detail_container)

        if (e is BackKeyInterface) e.backKeyPressed()
        else super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            supportFragmentManager.popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}

interface BackKeyInterface {
    fun backKeyPressed()
}