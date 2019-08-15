package com.example.shows_jurenivan.ui.fragments

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shows_jurenivan.R
import com.example.shows_jurenivan.data.RetrofitClient
import com.example.shows_jurenivan.data.viewModels.EpisodeViewModel
import com.example.shows_jurenivan.isNetworkAvailable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_episode.*
import kotlinx.android.synthetic.main.fragment_show.episodeDescription
import kotlinx.android.synthetic.main.fragment_show.imgPlaceholder


class EpisodeFragment : Fragment() {

    companion object {
        const val EPISODEID_KEY = "EpisodeID"
        fun newInstance(episodeId: String?) = EpisodeFragment().apply {
            val args = Bundle()
            args.putString(EPISODEID_KEY, episodeId)
            arguments = args
        }
    }

    private lateinit var viewModel: EpisodeViewModel
    private var episodeId: String? = null
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.episodeId = arguments?.getString(EPISODEID_KEY)
        viewModel = ViewModelProviders.of(this).get(EpisodeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        episodeId?.let { viewModel.setEpisode(it) }
        setHasOptionsMenu(true)

        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_light)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        viewModel.episodeliveData.observe(this, Observer {
            if (it != null && it.isSuccessful) {
                episodeDescription.text = it.data?.description
                seasonAndEpisode.text =
                    String.format(
                        "S%02d Ep%d",
                        Integer.parseInt(it.data?.season ?: "0"),
                        Integer.parseInt(it.data?.episode ?: "0")
                    )
                episodeTitle.text = it.data?.title

                Picasso.get().load(RetrofitClient.BASE_URL + it.data?.imageUrl)
                    .placeholder(R.drawable.rc8j4).error(android.R.drawable.stat_notify_error)
                    .into(imgPlaceholder)
            }
        })

        commentsSection.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.item_detail_container, CommentsFragment.newInstance(episodeId))
                addToBackStack("CommentsDisplay")
                commit()
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
}