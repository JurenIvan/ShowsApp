package com.example.shows_jurenivan.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.ui.fragments.BackKeyInterface
import com.example.shows_jurenivan.ui.fragments.HomeFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_show.*

class ShowsContainerActivity : AppCompatActivity() {

    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_container)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, HomeFragment())
            commit()
        }

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""


        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }





    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("sada", "land")
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("sas", "vert")
        }
    }


    override fun onBackPressed() {

        var e = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount-1)
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

