package com.example.social_media.presentation.home.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.social_media.R
import com.example.social_media.databinding.FragmentSettingsBinding
import com.example.social_media.extensions.loadImage
import com.example.social_media.extensions.showSuccesfulImageUpload
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings), SettingsView {
    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var settingsPresenter: SettingsPresenter
    private var imageUri: Uri? = null
    private lateinit var chooseImageLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val data = activityResult.data
                if (data != null) {
                    with(binding) {
                        profilePicture.isClickable = false
                        logOutButton.isClickable = false
                        progressBar.isVisible = true
                        imageUri = data.data
                        imageUri?.let { settingsPresenter.changeProfilePicture(it) }
                    }
                }
            } else {
                binding.profilePicture.isClickable = true
            }
        }
        chooseImageLauncher = result
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        with(binding) {
            settingsPresenter.attachView(this@SettingsFragment)
            progressBar.isVisible = true
            profilePicture.isClickable = true
            settingsPresenter.getUser()

            profilePicture.setOnClickListener {
                profilePicture.isClickable = false
                chooseImage()
            }
            logOutButton.setOnClickListener {
                settingsPresenter.signOut()
                requireParentFragment().requireParentFragment().findNavController()
                    .navigate(R.id.navigateToRegister)
            }
        }
    }

    private fun chooseImage() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        chooseImageLauncher.launch(gallery)
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsPresenter.detachView()
    }

    override fun displayProfile(auth: FirebaseUser) {
        with(binding) {
            userName.text = auth.displayName
            userEmail.text = auth.email
            var userImage = auth.photoUrl.toString()
            if (!userImage.equals("null")) {
                if (userImage.contains("google")) {
                    userImage = userImage.dropLast(USER_IMAGE_DROP_TRESHOLD)
                    activity?.let {
                        profilePicture.loadImage(userImage)
                    }
                } else if (userImage.contains("facebook")) {
                    userImage = userImage + "?height=500"
                    activity?.let {
                        profilePicture.loadImage(userImage)
                    }
                } else {
                    activity?.let {
                        profilePicture.loadImage(userImage)
                    }
                }
            } else {
                Log.i("USERIMAGE", "No user image!")
            }
            progressBar.isVisible = false
        }
    }

    override fun displayError() {
        Snackbar.make(requireView(), "Error getting user data!", Snackbar.LENGTH_SHORT).show()
    }

    override fun displaySuccessfulImageUpload(uri: Uri) {
        with(binding){
            showSuccesfulImageUpload()
            logOutButton.isClickable = true
            profilePicture.isClickable = true
            progressBar.isVisible = false
            activity?.let {
                profilePicture.loadImage(uri)
            }
        }
    }

    override fun displayFailedImageUpload() {
        Snackbar.make(requireView(), "Error uploading image!", Snackbar.LENGTH_SHORT).show()
    }

    companion object{
        const val USER_IMAGE_DROP_TRESHOLD = 6
    }
}