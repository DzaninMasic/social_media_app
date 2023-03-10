package com.example.social_media.presentation.home.addpost

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.social_media.R
import com.example.social_media.common.model.NetworkConnection
import com.example.social_media.databinding.FragmentAddPostBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddPostFragment : Fragment(R.layout.fragment_add_post), AddPostView {
    private lateinit var binding: FragmentAddPostBinding
    @Inject
    lateinit var addPostPresenter: AddPostPresenter
    private lateinit var pictureChooser: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            binding.addPostImageView.isClickable = true
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let { data ->
                    val imageUri = data
                    addPostPresenter.uploadPostPicture(imageUri)
                }
            }
        }
        pictureChooser = result
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddPostBinding.bind(view)

        addPostPresenter.attachView(this)

        binding.postButton.setOnClickListener{
            if(!NetworkConnection.isOnline(requireContext())){
                Snackbar.make(requireView(), "Not connected to internet. Once you reconnect the post will be uploaded!",Snackbar.LENGTH_SHORT).setAnchorView(binding.guideLine).show()
                binding.progressBar.isVisible = false
            }else{
                binding.progressBar.isVisible = true
            }
            binding.postButton.isClickable = false
            binding.addPostImageView.isClickable = false
            val post = binding.description.text.toString().replace(Regex("^[\\s\\n\\r]+|[\\s\\n\\r]+$"), "")
            binding.description.text.clear()
            binding.description.clearFocus()
            addPostPresenter.addPost(post)
        }

        binding.addPostImageView.setOnClickListener{
            binding.addPostImageView.isClickable = false
            choosePicture()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addPostPresenter.detachView()
    }

    private fun choosePicture(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pictureChooser.launch(intent)
    }

    override fun showSuccessfulResponse() {
        binding.addPostImageView.isClickable = true
        binding.postButton.isClickable = true
        binding.progressBar.isVisible = false
        Glide.with(requireView()).load(com.facebook.R.drawable.com_facebook_profile_picture_blank_portrait).into(binding.addPostImageView)
        Snackbar.make(requireView(),"Post added successfully!",Snackbar.LENGTH_SHORT).setAnchorView(binding.guideLine).show()
    }

    override fun showFailedResponse(string: String) {
        binding.addPostImageView.isClickable = true
        binding.postButton.isClickable = true
        binding.progressBar.isVisible = false
        Snackbar.make(requireView(),string,Snackbar.LENGTH_SHORT).setAnchorView(binding.guideLine).show()
    }

    override fun showChosenImage(uri: Uri) {
        activity?.let {
            Glide.with(it).load(uri).into(binding.addPostImageView)
        }
    }
}