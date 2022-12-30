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
    private var auth = FirebaseAuth.getInstance()
    private val loginManager = LoginManager.getInstance()
    private lateinit var btn: Button
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



        val stateListener = FirebaseAuth.AuthStateListener {
            val dzanin = FirebaseAuth.getInstance().currentUser
            val name = dzanin?.displayName
            Log.i("CURRENTUSER", "onViewCreated: ${name}")
            val email = dzanin?.email
            Log.i("CURRENTUSER", "onViewCreated: ${email}")
            val photoUrl = dzanin?.photoUrl
            Log.i("CURRENTUSER", "onViewCreated: ${photoUrl}")
        }
        auth.addAuthStateListener(stateListener)

        //WE DO THIS FOR BACK PRESS
        navHostFragment?.childFragmentManager?.addOnBackStackChangedListener {  //***********
            when (navHostFragment?.childFragmentManager?.primaryNavigationFragment) {
                is FeedFragment -> bottomNavigationView.selectedItemId = R.id.miFeed
                is SettingsFragment -> bottomNavigationView.selectedItemId = R.id.miSettings
            }
        }

        btn=view.findViewById(R.id.button)
        btn.setOnClickListener(View.OnClickListener {
            val googleAuth = requestGoogleSignIn()
            auth.signOut()
            loginManager.logOut()
            googleAuth?.signOut()?.addOnSuccessListener {
                Log.i("CURRENTUSER", "onViewCreated: SIGNED OUT FROM GUGEL")
            }
            Toast.makeText(activity,"LOGGED OUT",Toast.LENGTH_SHORT).show()
        })
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



    private fun requestGoogleSignIn(): GoogleSignInClient? {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        return activity?.let { GoogleSignIn.getClient(it, gso) }
    }
}








//        view.isFocusableInTouchMode = true
//        view.requestFocus()
//        view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
//            Log.i("BACKPRESS", "keyCode: $keyCode")
//            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
//                Log.i("BACKPRESS", "onKey Back listener is working!!!")
//                //fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                //activity?.supportFragmentManager?.popBackStack()
//                return true
//            }
//            false
//        })

/*btn=view.findViewById(R.id.button)
Log.i("CURRENTUSER", "onViewCreated: ${auth.currentUser?.displayName}")

btn.setOnClickListener(View.OnClickListener {
    val googleAuth = requestGoogleSignIn()
    auth.signOut()
    loginManager.logOut()
    googleAuth?.signOut()?.addOnSuccessListener {
        Log.i("CURRENTUSER", "onViewCreated: SIGNED OUT FROM GUGEL")
    }
    Toast.makeText(activity,"LOGGED OUT",Toast.LENGTH_SHORT).show()
})*/