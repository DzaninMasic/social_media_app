package com.example.social_media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.social_media.databinding.FragmentPostImageBinding

class PostImageFragment : Fragment(R.layout.fragment_post_image) {
    private lateinit var binding: FragmentPostImageBinding
    val args: PostImageFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPostImageBinding.bind(view)
        if(findNavController().currentDestination?.id == R.id.postImageFragment){
            Glide.with(requireContext()).load(args.imageUri).into(binding.postImage)
        }
    }
}