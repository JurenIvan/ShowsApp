package com.example.shows_jurenivan.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.activities.ShowActivity
import com.example.shows_jurenivan.data.dataStructures.Episode

class EpisodeAdapter(private val activityHome: ShowActivity) :
    RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

    private var episodes = listOf<Episode>()

    fun setData(list: List<Episode>) {
        this.episodes = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_episode,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = episodes.size

    override fun onBindViewHolder(holder: ViewHolder, episodeId: Int) {
        val entry: Episode = episodes[episodeId]
        holder.episodeTitle.text = entry.title
        holder.seasonAndEpisode.text = String.format("S%02d E%02d", entry.seasonNumber, entry.episodeNumber)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val seasonAndEpisode = itemView.findViewById(R.id.seasonAndEpisode) as TextView
        val episodeTitle = itemView.findViewById(R.id.episodeTitle) as TextView
    }


}