package com.example.shows_jurenivan.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.activities.ActivityLogin.Companion.USERNAME
import com.example.shows_jurenivan.ui.HomeActivity
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val username = intent.getStringExtra(USERNAME)
        welcome_user.text = "Welcome $username"

        postDelayed()
    }


    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun onRestart() {
        postDelayed()
        super.onRestart()
    }

    private fun postDelayed() {
        handler.postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }


}
