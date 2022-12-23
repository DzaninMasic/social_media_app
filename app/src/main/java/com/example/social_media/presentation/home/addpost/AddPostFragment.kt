package com.example.social_media.presentation.home.addpost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.social_media.R
import com.example.social_media.data.dao.DAOPost
import com.example.social_media.domain.post.Post

class AddPostFragment : Fragment(), AddPostView {
    private lateinit var editText: EditText
    private lateinit var btn: Button
    private val addPostPresenter = AddPostPresenter()

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

        addPostPresenter.attachView(this)

        btn.setOnClickListener{
            val post = editText.text.toString()
            addPostPresenter.addPost(post)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addPostPresenter.detachView()
    }

    override fun showSuccessfulResponse() {
        Toast.makeText(requireContext(),"Post added successfully!",Toast.LENGTH_SHORT).show()
    }

    override fun showFailedResponse() {
        Toast.makeText(requireContext(),"There was an error with the post.",Toast.LENGTH_SHORT).show()
    }
}