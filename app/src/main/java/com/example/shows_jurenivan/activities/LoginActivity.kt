package com.example.shows_jurenivan.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import com.example.shows_jurenivan.R
import kotlinx.android.synthetic.main.activity_login.*
import android.content.Context
import android.content.SharedPreferences


class ActivityLogin : AppCompatActivity() {

    companion object {
        const val USERNAME = "username"
        const val MIN_EMAIL_LEN = 1
        const val MIN_PWD_LEN = 8
        const val LOGIN = "LOGINSHAREDPREF"
    }

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPref = getSharedPreferences(LOGIN, Context.MODE_PRIVATE)
        val userName = sharedPref.getString(USERNAME, "")
        if (!userName.isNullOrBlank()) {
            startWelcomeScreen(userName)
        }


        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnLogIn.isEnabled =
                    checkAllPasswordConditions(passwordTextEditor) && checkAllUsernameConditions(tvUsername)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        }

        passwordTextEditor.addTextChangedListener(textWatcher)
        tvUsername.addTextChangedListener(textWatcher)

        btnLogIn.setOnClickListener {
            if (Patterns.EMAIL_ADDRESS.matcher(tvUsername.text).matches()) {
                startWelcomeScreen(tvUsername.text.toString())
            } else {
                userNameLayout.error = getString(R.string.email_required)
            }
        }
    }

    private fun startWelcomeScreen(userName: String) {
        if (checkBox.isChecked)
            sharedPref.edit().putString("username", tvUsername.text.toString().trim()).apply()

        val intent = Intent(baseContext, WelcomeActivity::class.java)
        intent.putExtra(USERNAME, userName)
        startActivity(intent)
        finish()
    }


    private fun checkAllPasswordConditions(etPassword: EditText?): Boolean {
        return etPassword?.text?.length?.let { len -> len >= MIN_PWD_LEN } ?: false
    }

    private fun checkAllUsernameConditions(etUsername: EditText?): Boolean {
        return etUsername?.text?.length?.let { len -> len >= MIN_EMAIL_LEN } ?: false
    }

}
