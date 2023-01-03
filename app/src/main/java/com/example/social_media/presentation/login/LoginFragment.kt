package com.example.social_media.presentation.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.social_media.R
import com.facebook.*
import com.google.android.gms.auth.api.identity.BeginSignInRequest
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider


class LoginFragment : Fragment(), LoginView {
    val FB = "FACEBOOK"
    private lateinit var tvEmail: EditText
    private lateinit var tvPassowrd: EditText
    private lateinit var registerBtn: TextView
    private lateinit var loginBtn: Button
    private lateinit var googleBtn: ImageView
    private lateinit var facebookBtn: ImageView
    private var loginPresenter = LoginPresenter()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mCallbackManager = CallbackManager.Factory.create()
    private val loginManager = LoginManager.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //FACEBOOK
        loginManager.registerCallback(mCallbackManager,object : FacebookCallback<LoginResult>{
            override fun onCancel() {
                Log.i(FB, "onCancel: CANCELLED")
            }
            override fun onError(error: FacebookException) {
                Log.i(FB, "onError: ${error}")
            }
            override fun onSuccess(result: LoginResult) {
                Log.i(FB, "onSuccess: ${result}")
                loginPresenter.signInWithFacebook(result.accessToken)
            }
        })
        //FACEBOOK END
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvEmail = view.findViewById(R.id.emailText)
        tvPassowrd = view.findViewById(R.id.passwordText)
        registerBtn = view.findViewById(R.id.registerBtn)
        loginBtn = view.findViewById(R.id.loginBtn)
        googleBtn = view.findViewById(R.id.imageViewGoogle)
        facebookBtn = view.findViewById(R.id.imageViewFacebook)

        loginPresenter.attachView(this)

        if (auth.currentUser != null) {
            Navigation.findNavController(requireView()).navigate(R.id.navigateToHome)
        }
        //GOOGLE
        googleSignInClient = requestGoogleSignIn()

        googleBtn.setOnClickListener{
            if (googleSignInClient != null) {
                loginWithGoogle(googleSignInClient)
            }
        }
        //END GOOGLE

        //NORMAL AUTH
        loginBtn.setOnClickListener{
            loginUser()
        }
        registerBtn.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }
        //END NORMAL AUTH

        //FACEBOOK
        facebookBtn.setOnClickListener{
            loginManager.logOut()
            loginManager.logInWithReadPermissions(this, mCallbackManager, listOf("public_profile","email"))
        }
        //END FACEBOOK
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                loginPresenter.signInWithGoogle(credential, googleSignInClient)
            }catch (e: ApiException){
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //STANDARD LOGIN
    private fun loginUser(){
        val email: String = tvEmail.text.toString()
        val password: String = tvPassowrd.text.toString()

        if(TextUtils.isEmpty(email)){
            tvEmail.setError("Email cannot be empty")
            tvEmail.requestFocus()
        }
        else if(TextUtils.isEmpty(password)){
            tvPassowrd.setError("Password cannot be empty")
            tvPassowrd.requestFocus()
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

    private fun loginWithGoogle(gsc: GoogleSignInClient?){
        val intent = gsc?.signInIntent
        gsc?.signOut()
        startActivityForResult(intent,100)
    }
    //GOOGLE END

    override fun displaySuccess() {
        Toast.makeText(requireContext(), "Authentication successful!",Toast.LENGTH_SHORT).show()
        view?.let { Navigation.findNavController(it).navigate(R.id.navigateToHome) }
    }

    override fun displayError() {
        Toast.makeText(requireContext(), "Authentication failed!",Toast.LENGTH_SHORT).show()
    }
}