package com.example.shows_jurenivan.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.data.dataStructures.Comment
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentsAdapter :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private var comments = listOf<Comment>()

    fun setData(list: List<Comment>) {
        this.comments = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_comment,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) = holder.bind(comments[pos])

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(comment: Comment) {
            if (comment.text.isNullOrBlank())
                return

            with(itemView) {

                comment.userEmail?.let { selectRandomUserImage(it) }?.let { image.setImageResource(it) }
                userName.text = comment.userEmail?.split("@")?.get(0) ?: ""
                text.text = comment.text
                timeSincePost.text = getRandomTimeSincePost()
            }
        }

        private fun getRandomTimeSincePost(): CharSequence? = arrayListOf(
            "5 min", "10 h", "14 min", "8 min", "12 s",
            "6 h", "30 min", "12 min", "9 min", "7 min", "1d 5h", "12 h"
        )[(Math.random() * 12).toInt()]


        private fun selectRandomUserImage(userEmail: String): Int {
            return when (userEmail.hashCode() % 3) {
                0 -> R.drawable.ic_img_placeholder_user_1
                1 -> R.drawable.ic_img_placeholder_user_2
                else -> R.drawable.ic_img_placeholder_user_3
            }
        }

    }
}