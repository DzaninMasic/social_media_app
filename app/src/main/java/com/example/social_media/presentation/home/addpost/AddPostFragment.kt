package com.example.social_media.presentation.home.addpost

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.social_media.R
import com.example.social_media.databinding.FragmentAddPostBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddPostFragment : Fragment(R.layout.fragment_add_post), AddPostView {
    private var imageUri: Uri? = null
    private lateinit var binding: FragmentAddPostBinding
    @Inject
    lateinit var addPostPresenter: AddPostPresenter
    private lateinit var pictureChooser: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let { data ->
                    imageUri = data
                    addPostPresenter.uploadPostPicture(imageUri!!)
                }
            }
        }
        pictureChooser = result
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddPostBinding.bind(view)

        addPostPresenter.attachView(this)

        binding.postButton.setOnClickListener{
            val post = binding.description.text.toString().replace(Regex("^[\\s\\n\\r]+|[\\s\\n\\r]+$"), "")
            addPostPresenter.addPost(post)
        }

        binding.addPostImageView.setOnClickListener{
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
        Toast.makeText(requireContext(),"Post added successfully!",Toast.LENGTH_SHORT).show()
    }

    override fun showFailedResponse() {
        Toast.makeText(requireContext(),"There was an error with the post.",Toast.LENGTH_SHORT).show()
    }

    override fun showChosenImage(uri: Uri) {
        activity?.let {
            Glide.with(it).load(uri).into(binding.addPostImageView)
        }
    }
}