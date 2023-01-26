package com.example.social_media.presentation.register

import android.app.Activity
import android.os.Build
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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

        registerPresenter.attachView(this)

        binding.loginBtn.setOnClickListener{
            @RequiresApi(Build.VERSION_CODES.M)
            if(!NetworkConnection.isOnline(requireContext())){
                Toast.makeText(requireContext(), "No internet connection.",Toast.LENGTH_SHORT).show()
            }
            else Navigation.findNavController(view).navigate(R.id.navigateToLogin)
        }

        binding.registerBtn.setOnClickListener {
            createUser()
        }

        //FACEBOOK
        binding.imageViewFacebook.setOnClickListener{
            loginManager.logOut()
            loginManager.logInWithReadPermissions(this, mCallbackManager, listOf("public_profile","email"))
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
                    Toast.makeText(requireContext(), "Error: ${e.statusCode}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        googleSignInClient = requestGoogleSignIn()
        binding.imageViewGoogle.setOnClickListener {
            if (googleSignInClient != null) {
                googleSignIn.launch(googleSignInClient?.signInIntent)
            }
        }
        //GOOGLE END
    }

    override fun onDestroy() {
        super.onDestroy()
        registerPresenter.detachView()
    }

    private fun createUser() {
        val name = binding.nameText.text.toString()
        val email: String = binding.emailText.text.toString()
        val password: String = binding.passwordText.text.toString()

        if(TextUtils.isEmpty(name)){
            binding.nameText.setError("Name cannot be empty")
            binding.nameText.requestFocus()
        }else if(name.length <= 2){
            binding.nameText.setError("Name must be longer than 2 characters")
            binding.nameText.requestFocus()
        }
        else if (TextUtils.isEmpty(email)) {
            binding.emailText.setError("Email cannot be empty")
            binding.emailText.requestFocus()
        } else if (TextUtils.isEmpty(password)) {
            binding.passwordText.setError("Password cannot be empty")
            binding.passwordText.requestFocus()
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
    //GOOGLE END

    override fun displaySuccess() {
        Toast.makeText(requireContext(), "Authentication successful!",Toast.LENGTH_SHORT).show()
        view?.let { Navigation.findNavController(it).navigate(R.id.navigateToLogin) }
    }

    override fun displayError() {
        Toast.makeText(requireContext(), "Authentication failed!",Toast.LENGTH_SHORT).show()
    }

}