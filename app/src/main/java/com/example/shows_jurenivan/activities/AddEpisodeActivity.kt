package com.example.shows_jurenivan.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import com.example.shows_jurenivan.R
import kotlinx.android.synthetic.main.activity_add_episode.*

const val RESULT_TITLE = "resultTitle"
const val RESULT_DESC = "resultDescription"
const val RESULT_EPISODE_NUM = "orderNum"
const val RESULT_SHOW_NUM = "position"

class AddEpisodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnSave.isEnabled =
                    checkAllTextBoxConditions(episodeTitle) && checkAllTextBoxConditions(episodeDescription)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        }

        episodeTitle.addTextChangedListener(textWatcher)
        episodeDescription.addTextChangedListener(textWatcher)

        btnSave.setOnClickListener {

            val resultIntent = Intent()
            resultIntent.putExtra(RESULT_TITLE, episodeTitle.text.toString())
            resultIntent.putExtra(RESULT_DESC, episodeDescription.text.toString())
            resultIntent.putExtra(RESULT_EPISODE_NUM, intent.getIntExtra(RESULT_EPISODE_NUM, 0))
            resultIntent.putExtra(RESULT_SHOW_NUM, intent.getIntExtra(RESULT_SHOW_NUM, 0))

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun checkAllTextBoxConditions(textBox: TextInputEditText?): Boolean {
        return textBox?.text?.length?.let { len -> len >= 1 } ?: false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
