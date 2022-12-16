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
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {
    private lateinit var tvEmail: EditText
    private lateinit var tvPassowrd: EditText
    private lateinit var registerBtn: Button
    private lateinit var loginBtn: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvEmail = view.findViewById(R.id.emailText)
        tvPassowrd = view.findViewById(R.id.passwordText)
        registerBtn = view.findViewById(R.id.registerBtn)
        loginBtn = view.findViewById(R.id.loginBtn)

        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateToLogin)
        })

        registerBtn.setOnClickListener{
            createUser()
        }
    }

    private fun createUser(){
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
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(it) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show()
                            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.navigateToLogin) }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

}