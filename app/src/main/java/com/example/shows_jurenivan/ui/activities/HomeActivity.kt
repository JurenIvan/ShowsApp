package com.example.shows_jurenivan.ui.activities

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.adapters.ShowsAdapter
import com.example.shows_jurenivan.data.dataStructures.Show
import com.example.shows_jurenivan.data.viewModels.HomeViewModel
import com.example.shows_jurenivan.isNetworkAvailable
import com.example.shows_jurenivan.ui.fragments.ShowFragment
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }

        private const val IS_GRID = "IsGrid"
    }

    private lateinit var viewModel: HomeViewModel
    private val adapterList = showsAdapterFactory(false)
    private val adapterGrid = showsAdapterFactory(true)

    private var shows: List<Show> = listOf()
    private var isGrid: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isGrid = savedInstanceState?.getBoolean(IS_GRID) ?: true

        setContentView(R.layout.activity_home)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.liveData.observe(this, Observer { newData ->
            if (newData != null) {
                if (newData.isSuccessful) {
                    newData.data?.let {
                        adapterList.setData(it)
                        adapterGrid.setData(it)
                        adapterList.notifyDataSetChanged()
                        adapterGrid.notifyDataSetChanged()
                        shows = it
                    }
                }
            }
        })

        logout.setOnClickListener { confirmLogout() }
        setAdapterAndLayoutManager()

        viewPicker.setOnClickListener {
            isGrid = !isGrid
            setAdapterAndLayoutManager()
        }

        viewModel.errorLiveData.observe(this, Observer { errors ->
            if (errors != null && errors.isNotBlank()) {
                Toast.makeText(this, errors, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.loadingLiveData.observe(this, Observer { loading ->
            if (loading == null || !loading) {
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.VISIBLE
            }
        })

        if (!isNetworkAvailable(context = this)) {
            AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage("Seems you have no internet connection. Functionality limited. :( ")
                .setPositiveButton("OK", null)
                .create()
                .show()
        }

    }

    private fun setAdapterAndLayoutManager() {
        if (isGrid) {
            recyclerViewShows.layoutManager = GridLayoutManager(this, 2)
            recyclerViewShows.adapter = adapterGrid
            viewPicker.setImageResource(R.drawable.ic_listview)
            return
        }
        recyclerViewShows.layoutManager = LinearLayoutManager(this)
        recyclerViewShows.adapter = adapterList
        viewPicker.setImageResource(R.drawable.ic_gridview_white)
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

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(IS_GRID, isGrid)
        super.onSaveInstanceState(outState)
    }
}

interface BackKeyInterface {
    fun backKeyPressed()
}