package com.example.social_media

import android.R.attr
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {
    private var auth = FirebaseAuth.getInstance()
    private val loginManager = LoginManager.getInstance()
    private lateinit var btn: Button
    private lateinit var bottomNavigationView: BottomNavigationView
    private var navHostFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Toast.makeText(activity,"Home",Toast.LENGTH_SHORT).show()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = view.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false
        navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView)

        bottomNavigationView.setOnItemSelectedListener {
            navigateToItem(it)
            return@setOnItemSelectedListener true
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
            R.id.miFeed -> if (currentVisibleFragment !is FeedFragment) { Navigation.findNavController(requireView().findViewById(R.id.fragmentContainerView)).navigate(R.id.navigateToFeed)}
            R.id.miSettings -> if (currentVisibleFragment !is SettingsFragment) { Navigation.findNavController(requireView().findViewById(R.id.fragmentContainerView)).navigate(R.id.navigateToSettings)}
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