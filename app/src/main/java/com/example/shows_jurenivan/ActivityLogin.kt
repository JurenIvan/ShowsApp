package com.example.shows_jurenivan

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login.*
import android.content.Intent


const val USERNAME = "username"
const val MIN_EMAIL_LEN=1
const val MIN_PWD_LEN=8


class ActivityLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (checkAllPasswordConditions(passwordTextEditor) && checkAllUsernameConditions(tvUsername)) {
                    btnLogIn.isEnabled = true
                    btnLogIn.background = getDrawable(R.drawable.roundedButtonEnabled)
                } else {
                    btnLogIn.isEnabled = false
                    btnLogIn.background = getDrawable(R.drawable.roundedButtonDisabled)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        }

        passwordTextEditor.addTextChangedListener(textWatcher)
        tvUsername.addTextChangedListener(textWatcher)

        btnLogIn.setOnClickListener {
            if (Patterns.EMAIL_ADDRESS.matcher(tvUsername.text).matches()) {
                val intent = Intent(baseContext, ActivityWelcome::class.java)
                intent.putExtra(USERNAME, tvUsername.text.toString())
                startActivity(intent)
                finish()
            } else {
                userNameLayout.error = getString(R.string.email_required)
            }
        }
    }

    private fun checkAllPasswordConditions(etPassword: EditText?): Boolean {
        return etPassword?.text?.length?.let { len -> len >= MIN_PWD_LEN } ?: false
    }

    private fun checkAllUsernameConditions(etUsername: EditText?): Boolean {
        return etUsername?.text?.length?.let { len -> len >= MIN_EMAIL_LEN } ?: false
    }

}
