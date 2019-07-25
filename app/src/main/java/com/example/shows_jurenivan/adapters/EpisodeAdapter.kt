package com.example.shows_jurenivan.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.data.dataStructures.Episode
import kotlinx.android.synthetic.main.item_episode.view.*

class EpisodeAdapter :
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

    override fun onBindViewHolder(holder: ViewHolder, episodeId: Int) = holder.bind(episodes[episodeId])

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(episode: Episode) {
            with(itemView) {
                seasonAndEpisode.text = String.format("S%02d E%02d", episode.seasonNumber, episode.episodeNumber)
                episodeTitle.text = episode.title
            }
        }
    }
}