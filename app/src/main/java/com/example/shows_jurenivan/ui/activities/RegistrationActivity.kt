package com.example.shows_jurenivan.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.MenuItem
import android.widget.Toast
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.data.dataStructures.User
import com.example.shows_jurenivan.data.viewModels.RegisterViewModel
import com.example.shows_jurenivan.ui.activities.LoginActivity.Companion.checkAllPasswordConditions
import com.example.shows_jurenivan.ui.activities.LoginActivity.Companion.checkAllUsernameConditions
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity() {

    companion object {
        private const val EMAIL_KEY = "Email"
        private const val REMEMBER_ME_CHECK = "remember"
        const val LOGIN = "LOGINSHAREDPREF"

        fun newInstance(context: Context, email: String?, rememberMe: Boolean): Intent {
            val intent = Intent(context, RegistrationActivity::class.java)
            intent.putExtra(EMAIL_KEY, email)
            intent.putExtra(REMEMBER_ME_CHECK, rememberMe)
            return intent
        }
    }

    private lateinit var sharedPref: SharedPreferences
    private lateinit var viewModel: RegisterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        sharedPref = getSharedPreferences(LOGIN, Context.MODE_PRIVATE)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.getStringExtra(EMAIL_KEY) != null) {
            eMailTextInput.setText(intent.getStringExtra(EMAIL_KEY).trim())
        }

        registerButton.setOnClickListener {
            if (password1.text.toString().equals(password2.text.toString()).not()) {
                passwordTextLayout2.error = "Paswords do not match!"
                return@setOnClickListener
            } else if (Patterns.EMAIL_ADDRESS.matcher(eMailTextInput.text).matches().not()) {
                eMailTextInputLayout.error = "Invalid email"
            } else {
                val user = User()
                user.email = eMailTextInput.text.toString()
                user.password = password1.text.toString()

                viewModel.registerUser(user)
                Toast.makeText(this, "Connecting Servers", Toast.LENGTH_SHORT).show()
            }

            viewModel.registerLiveData.observe(this, Observer { user ->
                if (user?.isSuccessful == true) {
                    val userToLogIn = user.data!!
                    userToLogIn.password = password1.text.toString().trim()
                    viewModel.loginUser(userToLogIn)
                    viewModel.registerLiveData.removeObservers(this)
                }
            })

            viewModel.tokenLiveData.observe(this, Observer { token ->
                if (token?.isSuccessful == true) {
                    val userEmail = viewModel.registerLiveData.value?.data?.email
                    if (userEmail.isNullOrBlank().not()) {

                        sharedPref.edit()
                            .putString(LoginActivity.USERNAME, userEmail)
                            .putString(LoginActivity.TOKEN, token.data?.token)
                            .putBoolean(LoginActivity.REMEMBER_ME, intent.getBooleanExtra(REMEMBER_ME_CHECK, false))
                            .apply()

                        startActivity(userEmail?.let { _ -> WelcomeActivity.newInstance(this, userEmail) })
                        finishAffinity()
                    }
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }
            })
        }

        password1.addTextChangedListener(textWatcher)
        password2.addTextChangedListener(textWatcher)

    }


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            registerButton.isEnabled =
                checkAllPasswordConditions(password1)
                        && checkAllUsernameConditions(eMailTextInput)
                        && checkAllPasswordConditions(password2)
            passwordTextLayout2.error = ""
            eMailTextInputLayout.error = ""
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}