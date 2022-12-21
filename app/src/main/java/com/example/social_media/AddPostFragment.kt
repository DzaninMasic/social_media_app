package com.example.social_media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.social_media.dao.DAOPost
import com.example.social_media.data_class.Post

class AddPostFragment : Fragment() {
    private lateinit var editText: EditText
    private lateinit var btn: Button
    private val dao = DAOPost()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editText = view.findViewById(R.id.description)
        btn = view.findViewById(R.id.postButton)

        btn.setOnClickListener{
            val post = Post(editText.text.toString())
            Toast.makeText(activity,"${post}",Toast.LENGTH_SHORT).show()
            dao.add(post).addOnSuccessListener{
                Toast.makeText(activity, "Post added!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}