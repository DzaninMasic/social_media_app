package com.example.social_media

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
import androidx.navigation.fragment.NavHostFragment
import com.facebook.*
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import java.util.*


class LoginFragment : Fragment() {
    val FB = "FUCKING FACEBOOK"
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var tvEmail: EditText
    private lateinit var tvPassowrd: EditText
    private lateinit var registerBtn: TextView
    private lateinit var loginBtn: Button
    private lateinit var googleBtn: ImageView
    private lateinit var facebookBtn: ImageView
    //FOR FACEBOOK

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mCallbackManager = CallbackManager.Factory.create()
    private val loginManager = LoginManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginManager.registerCallback(mCallbackManager,object : FacebookCallback<LoginResult>{
            override fun onCancel() {
                Toast.makeText(activity,"CANCELLED",Toast.LENGTH_SHORT).show()
                Log.i(FB, "onCancel: CANCELLED")
            }

            override fun onError(error: FacebookException) {
                Log.i(FB, "onError: ${error}")
                Toast.makeText(activity,"ERROR",Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(result: LoginResult) {
                Log.i(FB, "onSuccess: ${result}")
                Toast.makeText(activity,"GOOD",Toast.LENGTH_SHORT).show()
                handleFacebookToken(result.accessToken)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Toast.makeText(activity,"LOGIN",Toast.LENGTH_SHORT).show()

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

        if (auth.currentUser != null) {
            Navigation.findNavController(requireView()).navigate(R.id.navigateToHome)
        }
        //GOOGLE
        val gsc = requestGoogleSignIn()

        googleBtn.setOnClickListener{
            if (gsc != null) {
                loginWithGoogle(gsc)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(FB, "onActivityResult: activity result")
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                Toast.makeText(activity,"Success",Toast.LENGTH_SHORT).show()
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnSuccessListener {
                    Log.i("DZANIN", "onActivityResult AFTER: ${auth.currentUser?.email}")
                }.addOnFailureListener {
                    Log.i("DZANIN", "onActivityResult AFTER: ${it.message}")
                }
                Navigation.findNavController(requireView()).navigate(R.id.navigateToHome)
            }catch (e: ApiException){
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //FACEBOOK SHIT
    private fun handleFacebookToken(token: AccessToken){
        val fbCredential = FacebookAuthProvider.getCredential(token.token)
        activity?.let {
            auth.signInWithCredential(fbCredential).addOnCompleteListener(it, object : OnCompleteListener<AuthResult>{
                override fun onComplete(task: Task<AuthResult>) {
                    if(task.isSuccessful){
                        Log.i(FB, "onComplete: fb sign in successful")
                        Toast.makeText(activity,"Facebook success",Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(requireView()).navigate(R.id.navigateToHome)
                    }
                    else{
                        Toast.makeText(activity,"Facebook fail",Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }
    }








    //FACEBOOK END

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
            activity?.let {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(it) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success")
                            Navigation.findNavController(requireView()).navigate(R.id.navigateToHome)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.exception)
                            Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun requestGoogleSignIn():GoogleSignInClient?{
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val signInClient = activity?.let { GoogleSignIn.getClient(it, gso) }
        return signInClient
    }

    private fun loginWithGoogle(gsc: GoogleSignInClient){
        val intent = gsc.signInIntent
        startActivityForResult(intent,100)
    }

}