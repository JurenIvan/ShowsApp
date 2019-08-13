package com.example.shows_jurenivan.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
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
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            return intent
        }
    }

    private lateinit var viewModel: HomeViewModel
    private  val adapterList=showsAdapterFactory(false)
    private  val adapterGrid=showsAdapterFactory(true)

    private lateinit var sharedPref: SharedPreferences

    private var shows: List<Show> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        sharedPref = getSharedPreferences(HOME_GRID_LAYOUT, Context.MODE_PRIVATE)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        logout.setOnClickListener { confirmLogout() }

        viewModel.liveData.observe(this, Observer { newData ->
            if (newData != null) {
                if (newData.isSuccessful) {
                    newData.data?.let {
                        adapterList.setData(it)
                        adapterGrid.setData(it)
                        shows = it
                    }
                }
            }
        })

        recyclerViewShows.layoutManager = GridLayoutManager(this, 2)
        recyclerViewShows.adapter = adapterGrid


        viewPicker.setOnClickListener {
            if (sharedPref.getBoolean(HOME_GRID_LAYOUT, true)) {
                recyclerViewShows.layoutManager = GridLayoutManager(this, 2)
                sharedPref.edit().putBoolean(HOME_GRID_LAYOUT, false).apply()
                viewPicker.setImageResource(R.drawable.ic_listview)
                recyclerViewShows.adapter = adapterGrid
            } else {
                recyclerViewShows.layoutManager = LinearLayoutManager(this)
                sharedPref.edit().putBoolean(HOME_GRID_LAYOUT, true).apply()
                viewPicker.setImageResource(R.drawable.ic_gridview_white)
                recyclerViewShows.adapter = adapterList
            }
        }

    }

    private fun showsAdapterFactory(isGrid: Boolean): ShowsAdapter {
        return ShowsAdapter(isGrid) { position ->
            supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.item_detail_container, ShowFragment.newInstance(shows[position].id))
                addToBackStack("ShowDisplay")
                commit()
            }
        }
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Shows")
            .setMessage("Are you sure you want to logout?")
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Yes") { _, _ ->
                getSharedPreferences(LoginActivity.LOGIN, Context.MODE_PRIVATE).edit()
                    .putString(LoginActivity.USERNAME, "")
                    .putString(LoginActivity.TOKEN, "")
                    .putBoolean(LoginActivity.REMEMBER_ME, false)
                    .apply()
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finishAffinity()
            }
            .show()
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