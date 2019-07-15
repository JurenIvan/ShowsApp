package com.example.shows_jurenivan.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.activities.ActivityLogin.Companion.USERNAME
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


        val username = intent.getStringExtra(USERNAME)
        welcome_user.text = "Welcome $username"


        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)

    }
}
