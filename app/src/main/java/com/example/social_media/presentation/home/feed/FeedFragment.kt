package com.example.social_media.presentation.home.feed

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.social_media.R
import com.example.social_media.common.model.NetworkConnection
import com.example.social_media.domain.post.DomainPost
import com.example.social_media.data.network.NetworkPost
import com.example.social_media.databinding.FragmentFeedBinding
import com.example.social_media.extensions.hideKeyboard
import com.example.social_media.presentation.home.feed.adapters.TestAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment(R.layout.fragment_feed), FeedView {
    @Inject
    lateinit var feedPresenter: FeedPresenter
    private lateinit var adapter: TestAdapter
    private lateinit var binding: FragmentFeedBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFeedBinding.bind(view)
        with(binding){
            adapter = TestAdapter(requireContext(), this@FeedFragment)
            mRecyclerView.adapter = adapter
            mRecyclerView.itemAnimator = null
            mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            feedPresenter.attachView(this@FeedFragment)

            mRecyclerView.isVisible = false
            progressBar.isVisible = true

            feedPresenter.getData()

            binding.mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
    }

    override fun onDestroy() {
        super.onDestroy()
        feedPresenter.detachView()
    }

    override fun showData(items: MutableList<DomainPost>) {
        with(binding){
            showLoaded()
            adapter.setData(items)
        }
    }

    override fun showLoaded() = with(binding) {
        progressBar.isVisible = false
        mRecyclerView.isVisible = true
        binding.noNetLayout.isVisible = false
    }

    override fun displayError(error: String) {
        Snackbar.make(requireView(),error,Snackbar.LENGTH_SHORT).show()
    }

    override fun onLike(post: NetworkPost) {
        feedPresenter.likePost(post)
    }

    override fun onComment(comment: String, postId: String?) {
        hideKeyboard()
        feedPresenter.commentOnPost(comment, postId)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDeletePost(post: NetworkPost) {
        feedPresenter.deletePost(post)
    }

    override fun displayDeleteSuccess(position: String) {
        Snackbar.make(requireView(),"Post deleted!",Snackbar.LENGTH_SHORT).show()
    }

    override fun displayNoConnection() = with(binding){
        progressBar.isVisible = false
        mRecyclerView.isVisible = false
        noNetLayout.isVisible = true
    }

    override fun onDeleteComment(commentPosition: String, postPosition: String?) {
        feedPresenter.deleteComment(commentPosition,postPosition)
    }

    override fun onImageClick(imageUri: String) {
        val action = FeedFragmentDirections.navigateToPostImage(imageUri)
        if(findNavController().currentDestination?.id == R.id.postImageFragment){
            Log.i("DZANINMASIC", "BLOCKED")
        }else{
            this.findNavController().navigate(action)
        }
    }
}