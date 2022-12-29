package com.example.social_media.presentation.register

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.w3c.dom.Text

class RegisterFragment : Fragment(), RegisterView {
    private val FB = "FACEBOOK"
    private lateinit var tvName: EditText
    private lateinit var tvEmail: EditText
    private lateinit var tvPassowrd: EditText
    private lateinit var registerBtn: Button
    private lateinit var loginBtn: TextView
    private lateinit var googleBtn: ImageView
    private lateinit var facebookBtn: ImageView
    private var registerPresenter = RegisterPresenter()
    private val mCallbackManager = CallbackManager.Factory.create()
    private val loginManager = LoginManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginManager.registerCallback(mCallbackManager,object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                Log.i(FB, "onCancel: CANCELLED")
            }
            override fun onError(error: FacebookException) {
                Log.i(FB, "onError: ${error}")
            }
            override fun onSuccess(result: LoginResult) {
                Log.i(FB, "onSuccess: ${result}")
                registerPresenter.signUpWithFacebook(result.accessToken)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvName = view.findViewById(R.id.nameText)
        tvEmail = view.findViewById(R.id.emailText)
        tvPassowrd = view.findViewById(R.id.passwordText)
        registerBtn = view.findViewById(R.id.registerBtn)
        loginBtn = view.findViewById(R.id.loginBtn)
        googleBtn = view.findViewById(R.id.imageViewGoogle)
        facebookBtn = view.findViewById(R.id.imageViewFacebook)


        registerPresenter.attachView(this)

        loginBtn.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateToLogin)
        })

        registerBtn.setOnClickListener {
            createUser()
        }

        //FACEBOOK
        facebookBtn.setOnClickListener{
            loginManager.logOut()
            loginManager.logInWithReadPermissions(this, mCallbackManager, listOf("public_profile","email"))
        }
        //FACEBOOK END
        //GOOGLE
        val gsc = requestGoogleSignIn()

        googleBtn.setOnClickListener{
            if (gsc != null) {
                loginWithGoogle(gsc)
            }
        }
        //GOOGLE END
    }

    override fun onDestroy() {
        super.onDestroy()
        registerPresenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                registerPresenter.signInWithGoogle(credential)
            }catch (e: ApiException){
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser() {
        val name = tvName.text.toString()
        val email: String = tvEmail.text.toString()
        val password: String = tvPassowrd.text.toString()

        if(TextUtils.isEmpty(name)){
            tvName.setError("Name cannot be empty")
            tvName.requestFocus()
        }else if(name.length <= 2){
            tvName.setError("Name must be longer than 2 characters")
            tvName.requestFocus()
        }
        else if (TextUtils.isEmpty(email)) {
            tvEmail.setError("Email cannot be empty")
            tvEmail.requestFocus()
        } else if (TextUtils.isEmpty(password)) {
            tvPassowrd.setError("Password cannot be empty")
            tvPassowrd.requestFocus()
        } else {
            registerPresenter.signUpUserWithFirebase(name, email, password)
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
    private fun loginWithGoogle(gsc: GoogleSignInClient){
        val intent = gsc.signInIntent
        startActivityForResult(intent,100)
    }
    //GOOGLE END

    override fun displaySuccess() {
        Toast.makeText(requireContext(), "Authentication successful!",Toast.LENGTH_SHORT).show()
        view?.let { Navigation.findNavController(it).navigate(R.id.navigateToLogin) }
    }

    override fun displayError() {
        Toast.makeText(requireContext(), "Authentication failed!",Toast.LENGTH_SHORT).show()
    }

}