package com.example.shows_jurenivan.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import com.example.shows_jurenivan.R
import kotlinx.android.synthetic.main.custom_image_view.view.*

class CustomImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var shouldStop: Boolean = false

    init {
        View.inflate(context, R.layout.custom_image_view, this)

        updateView()
    }

    private fun updateView() {
        rotator.setImageResource(R.drawable.ic_img_logo_mark)
    }

    fun startLoading() {
        this.shouldStop = false
        rotatorLayout.animate()
            .rotationBy(120f)
            .setDuration(1000)
            .setInterpolator(OvershootInterpolator(3f))
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    if (!shouldStop) startLoading()
                }
            })
            .start()
    }

    fun endLoading() {
        this.shouldStop = true
    }

}