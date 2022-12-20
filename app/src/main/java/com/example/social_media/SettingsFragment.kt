package com.example.social_media

import android.graphics.BitmapFactory
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
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text
import java.net.URL


class SettingsFragment : Fragment() {
    private var auth = FirebaseAuth.getInstance()
    private val loginManager = LoginManager.getInstance()
    private lateinit var profilePicture: ImageView
    private lateinit var userName: TextView
    private lateinit var email: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("SETTINGSFRAGMENT", "onViewCreated: ${auth.currentUser?.displayName}")
        Log.i("SETTINGSFRAGMENT", "onViewCreated: ${auth.currentUser?.email}")
        Log.i("SETTINGSFRAGMENT", "onViewCreated: ${auth.currentUser?.photoUrl.toString().dropLast(6)}")

        profilePicture = view.findViewById(R.id.profilePicture)
        userName = view.findViewById(R.id.userName)
        email = view.findViewById(R.id.userEmail)
        var userImage=auth.currentUser?.photoUrl.toString()
        if(userImage.contains("google")){
            userImage = userImage.dropLast(6)
        }else{
            userImage = userImage + "?height=500"
        }

        if(auth.currentUser != null){
            userName.text = auth.currentUser?.displayName
            email.text = auth.currentUser?.email
            activity?.let { Glide.with(it).load(userImage).into(profilePicture) }
        }else{
            Toast.makeText(activity, "HOW THE FUCK DID YOU EVEN GET HERE?", Toast.LENGTH_SHORT).show()
        }
    }
}