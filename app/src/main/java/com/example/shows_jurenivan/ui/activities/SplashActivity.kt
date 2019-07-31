package com.example.shows_jurenivan.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.*
import com.example.shows_jurenivan.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        doBounceAnimation()
    }

    private fun doBounceAnimation() {
        logo.animate()
            .translationY(0f)
            .setInterpolator(BounceInterpolator())
            .setDuration(1100)
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
            .setDuration(600)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
            })
            .start()
    }
}
