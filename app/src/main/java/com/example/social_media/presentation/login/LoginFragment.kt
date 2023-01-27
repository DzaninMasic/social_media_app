package com.example.social_media.presentation.login

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.example.social_media.R
import com.example.social_media.databinding.FragmentLoginBinding
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login), LoginView {
    private lateinit var binding: FragmentLoginBinding
    @Inject
    lateinit var loginPresenter: LoginPresenter
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mCallbackManager = CallbackManager.Factory.create()
    private val loginManager = LoginManager.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //FACEBOOK
        loginManager.registerCallback(mCallbackManager,object : FacebookCallback<LoginResult>{
            override fun onCancel() {}
            override fun onError(error: FacebookException) {}
            override fun onSuccess(result: LoginResult) {
                loginPresenter.signInWithFacebook(result.accessToken)
            }
        })
        //FACEBOOK END
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        loginPresenter.attachView(this)

        if (auth.currentUser != null) {
            Navigation.findNavController(requireView()).navigate(R.id.navigateToHome)
        }
        //GOOGLE
        val googleSignIn = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            googleSignInClient?.signOut()
            if (it.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    account?.let {
                        val googleAuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                        loginPresenter.signInWithGoogle(googleAuthCredential, googleSignInClient)
                    }
                } catch (e: ApiException) {
                    Snackbar.make(requireView(),"Error: ${e.statusCode}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        googleSignInClient = requestGoogleSignIn()
        binding.imageViewGoogle.setOnClickListener {
            if (googleSignInClient != null) {
                googleSignIn.launch(googleSignInClient?.signInIntent)
            }
        }
        //END GOOGLE

        //NORMAL AUTH
        binding.loginBtn.setOnClickListener{
            loginUser()
        }
        binding.registerBtn.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }
        //END NORMAL AUTH

        //FACEBOOK
        binding.imageViewFacebook.setOnClickListener{
            loginManager.logOut()
            loginManager.logInWithReadPermissions(this, mCallbackManager, listOf("public_profile","email"))
        }
        //END FACEBOOK
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter.detachView()
    }

    //STANDARD LOGIN
    private fun loginUser(){
        val email: String = binding.emailText.text.toString()
        val password: String = binding.passwordText.text.toString()

        if(TextUtils.isEmpty(email)){
            binding.emailText.setError("Email cannot be empty")
            binding.emailText.requestFocus()
        }
        else if(TextUtils.isEmpty(password)){
            binding.passwordText.setError("Password cannot be empty")
            binding.passwordText.requestFocus()
        }
        else{
            loginPresenter.signInWithFirebase(email, password)
        }
    }
    //STANDARD LOGIN END

    //GOOGLE
    private fun requestGoogleSignIn(): GoogleSignInClient? {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        return activity?.let { GoogleSignIn.getClient(it, gso) }
    }
    //GOOGLE END

    override fun displaySuccess() {
        Snackbar.make(requireView(),"Authentication successful!",Snackbar.LENGTH_SHORT).show()
        view?.let { Navigation.findNavController(it).navigate(R.id.navigateToHome) }
    }

    override fun displayError() {
        Snackbar.make(requireView(),"Authentication failed!",Snackbar.LENGTH_SHORT).show()
    }
}