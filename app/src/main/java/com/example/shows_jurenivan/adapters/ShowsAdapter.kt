package com.example.shows_jurenivan.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.activities.HomeActivity
import com.example.shows_jurenivan.activities.ShowActivity
import com.example.shows_jurenivan.dataStructures.Show


class ShowsAdapter(private val shows: List<Show>, private val activityHome: HomeActivity) :
    RecyclerView.Adapter<ShowsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_show,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = shows.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry: Show = shows[position]
        holder.textViewTitle.text = entry.name
        holder.textViewYears.text = entry.airDate
        holder.imageSrc.setImageResource(entry.image)
        holder.whole.setOnClickListener {
            activityHome.startActivity(ShowActivity.newInstance(activityHome,position))
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle = itemView.findViewById(R.id.itemShowListTitle) as TextView
        val textViewYears = itemView.findViewById(R.id.itemShowListYears) as TextView
        val imageSrc = itemView.findViewById(R.id.imageViewPicture) as ImageView
        val whole = itemView.findViewById(R.id.showListID) as LinearLayout

    }

}