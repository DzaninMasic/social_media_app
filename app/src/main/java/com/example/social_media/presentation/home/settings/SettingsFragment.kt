package com.example.social_media.presentation.home.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.social_media.R
import com.example.social_media.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings), SettingsView {
    private lateinit var binding: FragmentSettingsBinding
    @Inject
    lateinit var settingsPresenter: SettingsPresenter
    private var imageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        settingsPresenter.attachView(this)

        settingsPresenter.getUser()

        binding.profilePicture.setOnClickListener{
            chooseImage()
        }
        binding.logOutButton.setOnClickListener{
            settingsPresenter.signOut()
            requireParentFragment().requireParentFragment().findNavController().navigate(R.id.navigateToRegister)
        }
    }

    private fun chooseImage(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1 && data!=null && data.data != null){
            imageUri = data.data
            imageUri?.let { settingsPresenter.changeProfilePicture(it) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsPresenter.detachView()
    }

    override fun displayProfile(auth: FirebaseUser) {
        binding.userName.text = auth.displayName
        binding.userEmail.text = auth.email
        var userImage=auth.photoUrl.toString()
        if(!userImage.equals("null")){
            if(userImage.contains("google")){
                userImage = userImage.dropLast(6)
                activity?.let { Glide.with(it).load(userImage).circleCrop().into(binding.profilePicture)
                }
            }else if(userImage.contains("facebook")){
                userImage = userImage + "?height=500"
                activity?.let {
                    Glide.with(it).load(userImage).circleCrop().into(binding.profilePicture)
                }
            }else{
                activity?.let {
                    Glide.with(it).load(userImage).circleCrop().into(binding.profilePicture)
                }
            }
        }else{
            Log.i("USERIMAGE", "No user image!")
        }
    }

    override fun displayError() {
        Toast.makeText(activity, "Error getting user data!", Toast.LENGTH_SHORT).show()
    }

    override fun displaySuccessfulImageUpload(uri: Uri) {
        Toast.makeText(activity, "Image uploaded!", Toast.LENGTH_SHORT).show()
        activity?.let {
            Glide.with(it).load(uri).circleCrop().into(binding.profilePicture)
        }
    }

    override fun displayFailedImageUpload() {
        Toast.makeText(activity, "Error uploading image!", Toast.LENGTH_SHORT).show()
    }
}