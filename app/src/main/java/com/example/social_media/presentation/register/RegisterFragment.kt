package com.example.social_media.presentation.register

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.social_media.R
import com.example.social_media.common.model.NetworkConnection
import com.example.social_media.databinding.FragmentRegisterBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register), RegisterView {
    private val FB = "FACEBOOK"
    private lateinit var binding: FragmentRegisterBinding
    @Inject
    lateinit var registerPresenter: RegisterPresenter
    private val mCallbackManager = CallbackManager.Factory.create()
    private val loginManager = LoginManager.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginManager.registerCallback(mCallbackManager,object : FacebookCallback<LoginResult> {
            override fun onCancel() {}
            override fun onError(error: FacebookException) {
                Log.i(FB, "onError: ${error}")
            }
            override fun onSuccess(result: LoginResult) {
                Log.i(FB, "onSuccess: ${result}")
                registerPresenter.signUpWithFacebook(result.accessToken)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        with(binding){

            registerPresenter.attachView(this@RegisterFragment)

            loginBtn.setOnClickListener{
                @RequiresApi(Build.VERSION_CODES.M)
                if(!NetworkConnection.isOnline(requireContext())){
                    Snackbar.make(view,"No internet connection.",Snackbar.LENGTH_SHORT).show()
                }
                else Navigation.findNavController(view).navigate(R.id.navigateToLogin)
            }

            registerBtn.setOnClickListener {
                disableInteractions()
                createUser()
            }

            //FACEBOOK
            imageViewFacebook.setOnClickListener{
                disableInteractions()
                loginManager.logOut()
                loginManager.logInWithReadPermissions(this@RegisterFragment, mCallbackManager, listOf("public_profile","email"))
            }
            //FACEBOOK END
            //GOOGLE
            val googleSignIn = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                googleSignInClient?.signOut()
                if (it.resultCode == Activity.RESULT_OK) {
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        account?.let {
                            val googleAuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                            registerPresenter.signInWithGoogle(googleAuthCredential, googleSignInClient)
                        }
                    } catch (e: ApiException) {
                        Snackbar.make(view,"Error: ${e.statusCode}",Snackbar.LENGTH_SHORT).show()
                    }
                }else{
                    enableInteractions()
                }
            }

            googleSignInClient = requestGoogleSignIn()
            imageViewGoogle.setOnClickListener {
                if (googleSignInClient != null) {
                    disableInteractions()
                    googleSignIn.launch(googleSignInClient?.signInIntent)
                }
            }
            //GOOGLE END
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        registerPresenter.detachView()
    }

    private fun createUser() {
        with(binding){
            val name = nameText.text.toString()
            val email: String = emailText.text.toString()
            val password: String = passwordText.text.toString()

            if(TextUtils.isEmpty(name)){
                enableInteractions()
                nameText.setError("Name cannot be empty")
                nameText.requestFocus()
            }else if(name.length <= 2 || name.length > 20){
                enableInteractions()
                nameText.setError("Name must be longer than 2 characters and less than 20")
                nameText.requestFocus()
            }
            else if (TextUtils.isEmpty(email)) {
                enableInteractions()
                emailText.setError("Email cannot be empty")
                emailText.requestFocus()
            } else if (TextUtils.isEmpty(password)) {
                enableInteractions()
                passwordText.setError("Password cannot be empty")
                passwordText.requestFocus()
            } else {
                registerPresenter.signUpUserWithFirebase(name, email, password)
            }
        }
    }

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
        enableInteractions()
        Snackbar.make(requireView(),"User created successfully!",Snackbar.LENGTH_SHORT).show()
        view?.let { Navigation.findNavController(it).navigate(R.id.navigateToLogin) }
    }

    override fun displayError() {
        enableInteractions()
        Snackbar.make(requireView(),"Error creating user!",Snackbar.LENGTH_SHORT).show()
    }

    private fun enableInteractions(){
        with(binding){
            imageViewFacebook.isClickable = true
            imageViewGoogle.isClickable = true
            registerBtn.isClickable = true
            loginBtn.isClickable = true
            progressBar.isVisible = false
        }
    }

    private fun disableInteractions(){
        with(binding){
            imageViewFacebook.isClickable = false
            imageViewGoogle.isClickable = false
            registerBtn.isClickable = false
            loginBtn.isClickable = false
            progressBar.isVisible = true
        }
    }
}