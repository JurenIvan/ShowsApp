package com.example.shows_jurenivan.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.shows_jurenivan.R
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    companion object {
        const val EMAIL_KEY = "user"
        const val TOKEN = "token"

        fun newInstance(context: Context, email: String, token: String): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(EMAIL_KEY, email)
            intent.putExtra(TOKEN, token)
            return intent
        }
    }

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val username = intent.getStringExtra(EMAIL_KEY).split("@")[0]
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
            startActivity(
                HomeActivity.newInstance(
                    this,
                    intent.getStringExtra(EMAIL_KEY),
                    intent.getStringExtra(TOKEN)
                )
            )
            finish()
        }, 2000)
    }


}
