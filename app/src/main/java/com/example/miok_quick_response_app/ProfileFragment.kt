package com.example.miok_quick_response_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
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

        // Observe current language and update the UI
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            updateLanguageUI(language)
        }
        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        // Initialize user profile with sample data
        profileViewModel.initUserProfile(
            imageUrl = "https://example.com/profile.jpg",
            name = "Lily Doe",
            email = "IamSafeAtHome@Gmail.com",
            birthday = "Sept 05, 2010",
            address = "A Block, Gate 3, Tristram Street, Hamilton",
            fatherName = "John Doe",
            motherName = "Lara Doe"
        )

        // Set up the Edit Button to navigate to EditProfileFragment
        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        // Observe and update the profile details
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

        profileViewModel.fatherName.observe(viewLifecycleOwner, Observer { fatherName ->
            binding.userFatherName.text = fatherName
        })

        profileViewModel.motherName.observe(viewLifecycleOwner, Observer { motherName ->
            binding.userMotherName.text = motherName
        })

        profileViewModel.profileImageUrl.observe(viewLifecycleOwner, Observer { imageUrl ->
            // Optionally load profile image (e.g., using Glide)
            // Glide.with(this).load(imageUrl).into(binding.profileImage)
        })
    }

    private fun updateLanguageUI(language: String) {
        if (language == "Māori") {
            // Update the text views with Māori text

            binding.editProfileButton.text = getString(R.string.edit_profile_button_mr)
        } else {
            // Default to English
            binding.editProfileButton.text = getString(R.string.edit_profile_button)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up the binding to avoid memory leaks
    }
}
