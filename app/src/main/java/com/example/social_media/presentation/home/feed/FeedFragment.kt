package com.example.social_media.presentation.home.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.social_media.R
import com.example.social_media.domain.post.Post
import com.example.social_media.extensions.hideKeyboard
import com.example.social_media.presentation.home.feed.adapters.FeedFragmentAdapter

class FeedFragment : Fragment(), FeedView {
    private var recyclerView: RecyclerView? = null
    private val feedPresenter = FeedPresenter()
    private lateinit var adapter: FeedFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.mRecyclerView)
        adapter = FeedFragmentAdapter(requireContext(), this)
        recyclerView?.adapter = adapter
        recyclerView?.itemAnimator = null
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        feedPresenter.attachView(this)
        feedPresenter.getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        feedPresenter.detachView()
    }

    override fun showData(items: List<Post>) {
        adapter.setData(items)
    }

    override fun displayError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun onLike(position: Int) {
        feedPresenter.likePost(position)
    }

    override fun onComment(position: Int, comment: String) {
        hideKeyboard()
        feedPresenter.commentOnPost(position, comment)
    }
}