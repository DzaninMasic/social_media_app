package com.example.social_media.presentation.home.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.social_media.R
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class SettingsFragment : Fragment(), SettingsView {
    private lateinit var profilePicture: ImageView
    private lateinit var userName: TextView
    private lateinit var email: TextView
    private lateinit var signOutBtn: Button
    private val settingsPresenter = SettingsPresenter()
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsPresenter.attachView(this)

        profilePicture = view.findViewById(R.id.profilePicture)
        userName = view.findViewById(R.id.userName)
        email = view.findViewById(R.id.userEmail)
        signOutBtn = view.findViewById(R.id.logOutButton)
        settingsPresenter.getUser()

        profilePicture.setOnClickListener{
            chooseImage()
        }
        signOutBtn.setOnClickListener{
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
        userName.text = auth.displayName
        email.text = auth.email
//        Glide.with(this).load("").circleCrop().into(profilePicture).
        var userImage=auth.photoUrl.toString()
        Log.i("PROFILEPICTURE", "displayProfile: $userImage")
        if(!userImage.equals("null")){
            if(userImage.contains("google")){
                userImage = userImage.dropLast(6)
                activity?.let { Glide.with(it).load(userImage).circleCrop().into(profilePicture)
                }
            }else if(userImage.contains("facebook")){
                userImage = userImage + "?height=500"
                activity?.let {
                    Glide.with(it).load(userImage).circleCrop().into(profilePicture)
                }
            }else{
                activity?.let {
                    Glide.with(it).load(userImage).circleCrop().into(profilePicture)
                }
                //Toast.makeText(activity, "IMAGE ERROR!", Toast.LENGTH_SHORT).show()
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
            Glide.with(it).load(uri).circleCrop().into(profilePicture)
        }
    }

    override fun displayFailedImageUpload() {
        Toast.makeText(activity, "Error uploading image!", Toast.LENGTH_SHORT).show()
    }
}