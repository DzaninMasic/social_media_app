package com.example.social_media.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import com.example.social_media.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.i("BACKSTACKENTRIES", "onBackPressed: ${Navigation.findNavController(findViewById<View?>(
            R.id.fragmentContainerView
        )).backQueue.size}")
    }
}