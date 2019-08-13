package com.example.shows_jurenivan.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.data.RetrofitClient
import com.example.shows_jurenivan.data.dataStructures.Show
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_show_list.view.*


class ShowsAdapter(private val gridActive:Boolean,val clickAction: (Int) -> Unit = {}) :
    RecyclerView.Adapter<ShowsAdapter.ViewHolder>() {

    private var shows = listOf<Show>()

    fun setData(shows: List<Show>) {
        this.shows = shows
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        if (gridActive) {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_show_grid,
                    parent,
                    false
                )
            )
        }
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_show_list,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int = shows.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            var show = shows[position]
            with(itemView) {
                itemShowListTitle.text = show.title
                if(itemLikeCount!=null){
                    itemLikeCount.text=show.likesCount.toString()
                }

                Picasso.get().load(RetrofitClient.BASE_URL + show.imageURL)
                    .placeholder(R.drawable.ic_img_placeholder_episodes).error(R.drawable.ic_img_placeholder_episodes)
                    .into(imageViewPicture)

                showListID.setOnClickListener { clickAction(position) }
            }
        }
    }
}