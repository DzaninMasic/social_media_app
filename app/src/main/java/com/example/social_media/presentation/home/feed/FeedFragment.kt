package com.example.social_media.presentation.home.feed

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.social_media.R
import com.example.social_media.domain.post.DomainPost
import com.example.social_media.data.network.NetworkPost
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

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            var page = 1
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItems = linearLayoutManager.itemCount
                val lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                if(totalItems-1==lastVisible){
                    feedPresenter.getData(page)
                    page++
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        feedPresenter.detachView()
    }

    override fun showData(items: MutableList<DomainPost>) {
        adapter.setData(items)
    }

    override fun displayError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun onLike(postId: String) {
        feedPresenter.likePost(postId)
    }

    override fun onComment(comment: String, postId: String?) {
        hideKeyboard()
        feedPresenter.commentOnPost(comment, postId)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDeletePost(post: NetworkPost) {
        feedPresenter.deletePost(post)
        feedPresenter.getData()
    }

    override fun displayDeleteSuccess(position: String) {
        Toast.makeText(requireContext(),"Post deleted!",Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteComment(commentPosition: String, postPosition: String?) {
        feedPresenter.deleteComment(commentPosition,postPosition)
    }
}