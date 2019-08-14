package com.example.shows_jurenivan.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.adapters.CommentsAdapter
import com.example.shows_jurenivan.data.dataStructures.Comment
import com.example.shows_jurenivan.data.dataStructures.ResponseData
import com.example.shows_jurenivan.data.viewModels.CommentsViewModel
import kotlinx.android.synthetic.main.fragment_comments.*

class CommentsFragment : Fragment() {

    companion object {
        const val EPISODEID_KEY = "EpisodeID"
        fun newInstance(episodeId: String?) = CommentsFragment().apply {
            val args = Bundle()
            args.putString(EPISODEID_KEY, episodeId)
            arguments = args
        }
    }

    private lateinit var viewModel: CommentsViewModel
    private lateinit var adapter: CommentsAdapter
    private var episodeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.episodeId = arguments?.getString(EpisodeFragment.EPISODEID_KEY)
        viewModel = ViewModelProviders.of(this).get(CommentsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        episodeId?.let { viewModel.setEpisode(it) }


        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_light)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        adapter = CommentsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.commentsliveData.observe(this, Observer { comments ->
            if (comments != null) comments.data?.let { adapter.setData(it) }
            else adapter.setData(listOf())

            checkEmptiness(comments)
        })


        post.setOnClickListener {

            if (!commentInput.text.isNullOrBlank()) {
                episodeId?.let { it1 -> Comment(commentInput.text.toString(), it1, null, null) }
                    ?.let { it2 -> viewModel.postComment(it2) }
            }

        }

    }

    private fun checkEmptiness(comments: ResponseData<List<Comment>>?) {
        if (comments?.data.isNullOrEmpty()) {
            noComment.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noComment.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }


}