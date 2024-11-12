package com.example.miok_quick_response_app


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.miok_quick_response_app.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    // Initialize ViewModel
    private val profileViewModel: ProfileViewModel by viewModels()

    // Use view binding
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize user profile with sample data
        profileViewModel.initUserProfile(
            imageUrl = "https://example.com/profile.jpg",
            name = "Lily Doe",
            email = "IamSafeAtHome@Gmail.com",
            birthday = "Sept 05, 2010",
            address = "A Block, Gate 3, Tristram Street, Hamilton",
            parents = listOf(
                Parent(name = "Lara Doe", relationship = "Mother"),
                Parent(name = "John Doe", relationship = "Father")
            )
        )

        // Observe LiveData to update UI
        profileViewModel.userName.observe(viewLifecycleOwner, Observer { name ->
            binding.userName.text = name
        })

        profileViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            binding.userEmail.text = email
        })

        profileViewModel.userBirthday.observe(viewLifecycleOwner, Observer { birthday ->
            binding.userBirthday.text = birthday
        })

        profileViewModel.userAddress.observe(viewLifecycleOwner, Observer { address ->
            binding.userAddress.text = address
        })

        profileViewModel.userParents.observe(viewLifecycleOwner, Observer { parents ->
            val parentInfo = parents.joinToString { "${it.name} (${it.relationship})" }
            binding.userContacts.text = parentInfo
        })


        profileViewModel.profileImageUrl.observe(viewLifecycleOwner, Observer { imageUrl ->
            // Load image using an image loading library (e.g., Glide)
            // Glide.with(this).load(imageUrl).into(binding.profileImage)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding
    }
}
