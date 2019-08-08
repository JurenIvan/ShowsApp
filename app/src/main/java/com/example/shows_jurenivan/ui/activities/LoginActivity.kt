package com.example.shows_jurenivan.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.data.dataStructures.User
import com.example.shows_jurenivan.data.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    companion object {
        const val USERNAME = "username"
        const val TOKEN = "token"
        const val MIN_EMAIL_LEN = 1
        const val MIN_PWD_LEN = 8
        const val LOGIN = "LOGINSHAREDPREF"

        fun checkAllPasswordConditions(etPassword: EditText?): Boolean {
            return etPassword?.text?.length?.let { len -> len >= MIN_PWD_LEN } ?: false
        }

        fun checkAllUsernameConditions(etUsername: EditText?): Boolean {
            return etUsername?.text?.length?.let { len -> len >= MIN_EMAIL_LEN } ?: false
        }
    }

    private lateinit var sharedPref: SharedPreferences
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        sharedPref = getSharedPreferences(LOGIN, Context.MODE_PRIVATE)

        val token = sharedPref.getString(TOKEN, "")
        val userName = sharedPref.getString(USERNAME, "")

        if (userName.isNullOrBlank().not() && token.isNullOrBlank().not()) {
            startActivity(HomeActivity.newInstance(this, userName, token))
            finishAffinity()
        }

        passwordTextEditor.addTextChangedListener(textWatcher)
        tvUsername.addTextChangedListener(textWatcher)

        btnLogIn.setOnClickListener {
            if (!Patterns.EMAIL_ADDRESS.matcher(tvUsername.text).matches()) {
                userNameLayout.error = getString(R.string.email_required)
                return@setOnClickListener
            }

            val user = User()
            user.email = tvUsername.text.toString()
            user.password = passwordTextEditor.text.toString()

            viewModel.loginUser(user)
            Toast.makeText(this, "Connecting Servers", Toast.LENGTH_SHORT).show()
            android_logo.startLoading()
        }

        viewModel.liveData.observe(this, Observer { user ->
            if (user?.isSuccessful == true) {
                startWelcomeScreen(tvUsername.text.toString(), user.data.toString())
                finish()
            }else{
                android_logo.endLoading()
            }
        })


        createAccount.setOnClickListener {
            startActivity(
                RegistrationActivity.newInstance(
                    this,
                    tvUsername.text.toString(),
                    rememberMeCheckBox.isChecked
                )
            )
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            btnLogIn.isEnabled =
                checkAllPasswordConditions(passwordTextEditor) && checkAllUsernameConditions(tvUsername)

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    private fun startWelcomeScreen(userName: String, token: String) {
        if (rememberMeCheckBox.isChecked)
            sharedPref.edit().putString(USERNAME, tvUsername.text.toString().trim()).apply()
        sharedPref.edit().putString(TOKEN, tvUsername.text.toString().trim()).apply()

        startActivity(WelcomeActivity.newInstance(this, userName, token))
        finish()
    }


    fun checkAllPasswordConditions(etPassword: EditText?): Boolean {
        return etPassword?.text?.length?.let { len -> len >= MIN_PWD_LEN } ?: false
    }

    fun checkAllUsernameConditions(etUsername: EditText?): Boolean {
        return etUsername?.text?.length?.let { len -> len >= MIN_EMAIL_LEN } ?: false
    }

}
