package com.example.social_media

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /*override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if(user == null){
            this.finish()
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }*/

    override fun onBackPressed() {
        super.onBackPressed()
        Log.i("BACKSTACKENTRIES", "onBackPressed: ${Navigation.findNavController(findViewById<View?>(R.id.fragmentContainerView)).backQueue.size}")
    }
}