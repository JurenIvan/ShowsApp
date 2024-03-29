package com.example.shows_jurenivan.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.data.dataStructures.Episode
import kotlinx.android.synthetic.main.item_episode.view.*

class EpisodeAdapter(val clickAction: (Int) -> Unit = {}) :
    RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

    private var episodes = listOf<Episode>()

    fun setData(list: List<Episode>) {
        this.episodes = list?: listOf()
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            with(itemView) {
                seasonAndEpisode.text = getSeasonAndEpisodeFormatedText(position)
                episodeTitle.text = episodes[position].title
                episodeListId.setOnClickListener { clickAction(position) }
            }
        }

        private fun getSeasonAndEpisodeFormatedText(position: Int): String {
            return String.format(
                "S%02d E%02d",
                Integer.parseInt(episodes[position].season),
                Integer.parseInt(episodes[position].episode)
            )
        }
    }
}