package com.example.shows_jurenivan.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.adapters.ShowsAdapter
import com.example.shows_jurenivan.data.dataStructures.Show
import com.example.shows_jurenivan.data.viewModels.HomeViewModel
import com.example.shows_jurenivan.ui.fragments.ShowFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_show.*

class HomeActivity : AppCompatActivity() {

    companion object {
        const val HOME_GRID_LAYOUT = "home"
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
    private lateinit var sharedPref: SharedPreferences

    private var shows: List<Show> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        sharedPref = getSharedPreferences(HOME_GRID_LAYOUT, Context.MODE_PRIVATE)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        logout.setOnClickListener {
            sharedPref.edit().clear().apply()
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finishAffinity()
        }

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

        adapter = ShowsAdapter(true) { position ->
            supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.item_detail_container, ShowFragment.newInstance(shows[position].id))
                addToBackStack("ShowDisplay")
                commit()
            }
        }
        recyclerViewShows.layoutManager = GridLayoutManager(this, 2)
        recyclerViewShows.adapter = adapter


        viewPicker.setOnClickListener {
            if (sharedPref.getBoolean(HOME_GRID_LAYOUT, true)) {
                recyclerViewShows.layoutManager = GridLayoutManager(this, 2)
                sharedPref.edit().putBoolean(HOME_GRID_LAYOUT, false).apply()
                viewPicker.setImageResource(R.drawable.ic_listview)

                adapter = ShowsAdapter(true) { position ->
                    supportFragmentManager?.beginTransaction()?.apply {
                        replace(R.id.item_detail_container, ShowFragment.newInstance(shows[position].id))
                        addToBackStack("ShowDisplay")
                        commit()
                    }
                }

            } else {
                recyclerViewShows.layoutManager = LinearLayoutManager(this)
                sharedPref.edit().putBoolean(HOME_GRID_LAYOUT, true).apply()
                viewPicker.setImageResource(R.drawable.ic_gridview_white)

                adapter = ShowsAdapter(false) { position ->
                    supportFragmentManager?.beginTransaction()?.apply {
                        replace(R.id.item_detail_container, ShowFragment.newInstance(shows[position].id))
                        addToBackStack("ShowDisplay")
                        commit()
                    }
                }

            }
        }

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