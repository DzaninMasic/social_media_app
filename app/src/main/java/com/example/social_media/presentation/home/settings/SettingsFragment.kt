package com.example.social_media.presentation.home.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.social_media.R
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SettingsFragment : Fragment(), SettingsView {
    private lateinit var profilePicture: ImageView
    private lateinit var userName: TextView
    private lateinit var email: TextView
    private val settingsPresenter = SettingsPresenter()

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
        settingsPresenter.getUser()

        profilePicture.setOnClickListener{
            chooseImage()
        }
    }

    private fun chooseImage(){
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, 1)
    }

//    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
//        super.startActivityForResult(intent, requestCode)
//        if(requestCode==1)
//    }

    override fun onDestroy() {
        super.onDestroy()
        settingsPresenter.detachView()
    }

    override fun displayProfile(auth: FirebaseUser) {
        userName.text = auth.displayName
        email.text = auth.email
        var userImage=auth.photoUrl.toString()
        if(!userImage.equals("null")){
            if(userImage.contains("google")){
                userImage = userImage.dropLast(6)
                activity?.let { Glide.with(it).load(userImage).into(profilePicture)
                }
            }else if(userImage.contains("facebook")){
                userImage = userImage + "?height=500"
                activity?.let {
                    Glide.with(it).load(userImage).into(profilePicture)
                }
            }else{
                Toast.makeText(activity, "IMAGE ERROR!", Toast.LENGTH_SHORT).show()
            }
        }else{
            Log.i("USERIMAGE", "No user image!")
        }
    }

    override fun displayError() {
        Toast.makeText(activity, "Error getting user data!", Toast.LENGTH_SHORT).show()
    }
}