package com.example.shows_jurenivan.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.*
import com.example.shows_jurenivan.R
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    companion object {
        private const val ANIMATION_LEN_FIRST_PART = 1100L
        private const val ANIMATION_LEN_SECOND_PART = 600L
    }

    private var handler=Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    private fun startAnimation() {
        handler.postDelayed({
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        }, ANIMATION_LEN_FIRST_PART + ANIMATION_LEN_SECOND_PART)

        logo.animate()
            .translationY(0f)
            .setInterpolator(BounceInterpolator())
            .setDuration(ANIMATION_LEN_FIRST_PART)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    doScalingAnimation()
                }
            })
            .start()
    }

    private fun doScalingAnimation() {
        showsText.visibility = View.VISIBLE
        showsText.animate()
            .scaleX(8f)
            .scaleY(8f)
            .setInterpolator(OvershootInterpolator(8f))
            .setDuration(ANIMATION_LEN_SECOND_PART)
            .start()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        startAnimation()
    }

}
