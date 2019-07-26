package com.example.shows_jurenivan.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.adapters.ShowsAdapter
import com.example.shows_jurenivan.data.viewModels.ShowsViewModel
import com.example.shows_jurenivan.ui.fragments.BackKeyInterface
import com.example.shows_jurenivan.ui.fragments.ShowFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_show.*

class HomeActivity : AppCompatActivity() {

    private var twoPane: Boolean = false

    private lateinit var viewModel: ShowsViewModel
    private lateinit var adapter: ShowsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        adapter = ShowsAdapter { position ->
            supportFragmentManager?.beginTransaction()?.apply {
                var showFragment = ShowFragment()
                showFragment.setShow(position)

                replace(R.id.item_detail_container, showFragment)

                addToBackStack("ShowDisplay")
                commit()
            }
        }

        recyclerViewShows.layoutManager = LinearLayoutManager(this)
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

    override fun onBackPressed() {

        var e: Fragment? = supportFragmentManager.findFragmentById(R.id.item_detail_container)
        if (e is BackKeyInterface){
            e.backKeyPressed()
        }else

        super.onBackPressed()

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            supportFragmentManager.popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
