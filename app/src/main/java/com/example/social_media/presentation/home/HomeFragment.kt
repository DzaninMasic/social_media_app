package com.example.social_media.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.social_media.presentation.home.addpost.AddPostFragment
import com.example.social_media.presentation.home.feed.FeedFragment
import com.example.social_media.R
import com.example.social_media.presentation.home.settings.SettingsFragment
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fab: FloatingActionButton
    private var navHostFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = view.findViewById(R.id.bottomNavigationView)
        fab = view.findViewById(R.id.fab)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false
        navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView)

        bottomNavigationView.setOnItemSelectedListener {
            navigateToItem(it)
            return@setOnItemSelectedListener true
        }

        fab.setOnClickListener{
            val currentVisibleFragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment
            if(currentVisibleFragment !is AddPostFragment){
                Navigation.findNavController(requireView().findViewById(R.id.fragmentContainerView)).navigate(
                    R.id.navigateToAddPost
                )
            }
        }

        //WE DO THIS FOR BACK PRESS
        navHostFragment?.childFragmentManager?.addOnBackStackChangedListener {  //***********
            when (navHostFragment?.childFragmentManager?.primaryNavigationFragment) {
                is FeedFragment -> bottomNavigationView.selectedItemId = R.id.miFeed
                is SettingsFragment -> bottomNavigationView.selectedItemId = R.id.miSettings
            }
        }

    }

    private fun navigateToItem(selectedItem: MenuItem) {
        val currentVisibleFragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment
        when(selectedItem.itemId) {
            R.id.miFeed -> if (currentVisibleFragment !is FeedFragment) { Navigation.findNavController(requireView().findViewById(
                R.id.fragmentContainerView
            )).navigate(R.id.navigateToFeed)}
            R.id.miSettings -> if (currentVisibleFragment !is SettingsFragment) { Navigation.findNavController(requireView().findViewById(
                R.id.fragmentContainerView
            )).navigate(R.id.navigateToSettings)}
        }
    }

}