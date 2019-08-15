package com.example.shows_jurenivan.ui.fragments

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.adapters.CommentsAdapter
import com.example.shows_jurenivan.data.dataStructures.Comment
import com.example.shows_jurenivan.data.dataStructures.ResponseData
import com.example.shows_jurenivan.data.viewModels.CommentsViewModel
import com.example.shows_jurenivan.isNetworkAvailable
import kotlinx.android.synthetic.main.fragment_comments.*

class CommentsFragment : Fragment() {

    companion object {
        private const val EPISODEID_KEY = "EpisodeID"
        fun newInstance(episodeId: String?) = CommentsFragment().apply {
            val args = Bundle()
            args.putString(EPISODEID_KEY, episodeId)
            arguments = args
        }
    }

    private lateinit var viewModel: CommentsViewModel
    private lateinit var adapter: CommentsAdapter
    private var episodeId: String? = null
    private var progressDialog: ProgressDialog? = null


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
            if (comments != null)
                comments.data?.let { adapter.setData(it) }
            else {
                adapter.setData(listOf())
            }
            adapter.notifyDataSetChanged()
            checkEmptiness(comments)
        })


        post.setOnClickListener {
            if (!commentInput.text.isNullOrBlank()) {
                episodeId?.let { it1 -> Comment(commentInput.text.toString(), it1, null, null) }
                    ?.let { it2 -> viewModel.postComment(it2) }
            }
        }

        viewModel.errorLiveData.observe(this, Observer { errors ->
            if (errors != null && errors.isNotBlank()) {
                Toast.makeText(context, errors, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.loadingLiveData.observe(this, Observer { loading ->
            if (loading == null || !loading) {
                progressDialog?.cancel()
            } else {
                if (progressDialog == null)
                    progressDialog = ProgressDialog.show(context, "Shows", "Loading", true, true)
            }
        })

        context?.let {
            if (!isNetworkAvailable(context = it)) {
                AlertDialog.Builder(it)
                    .setTitle("Info")
                    .setMessage("Seems you have no internet connection. Functionality limited. :( ")
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
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