package com.example.shows_jurenivan.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.data.dataStructures.Show
import kotlinx.android.synthetic.main.item_show.view.*


class ShowsAdapter(val clickAction: ( Int) -> Unit = {}) :
    RecyclerView.Adapter<ShowsAdapter.ViewHolder>() {

    private var shows = listOf<Show>()

    fun setData(shows: List<Show>) {
        this.shows = shows
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_show,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = shows.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position:Int) {
            var show=shows[position]
            with(itemView) {
                itemShowListTitle.text = show.name
                itemShowListYears.text = show.airDate
                imageViewPicture.setImageResource(show.image)
                showListID.setOnClickListener { clickAction(position) }
            }
        }
    }
}