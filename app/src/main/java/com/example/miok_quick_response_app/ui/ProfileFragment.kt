package com.example.miok_quick_response_app.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.ProfileViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.database.ProfileDatabase
import com.example.miok_quick_response_app.databinding.FragmentProfileBinding

// Fragment responsible for displaying and managing the user's profile information
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

        // Load the profile from the database when the fragment is created
        profileViewModel.loadProfile()

        // Observe current language and update the UI
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            updateLanguageUI(language)
        }
        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }


        // Set up the Edit Button to navigate to EditProfileFragment
        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        // Load existing profile data
        profileViewModel.imageUrl.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                binding.profileImage.setImageURI(Uri.parse(it))  // Load image from URI if available
            }
        }

        // Observe profile data from ViewModel and update UI
        profileViewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.userName.text = name
        }

        profileViewModel.userBirthday.observe(viewLifecycleOwner) { birthday ->
            binding.userBirthday.text = birthday
        }

        profileViewModel.userAddress.observe(viewLifecycleOwner) { address ->
            binding.userAddress.text = address
        }

        profileViewModel.fatherName.observe(viewLifecycleOwner) { fatherName ->
            binding.fatherName.text = fatherName
        }

        profileViewModel.motherName.observe(viewLifecycleOwner) { motherName ->
            binding.motherName.text = motherName
        }
    }

    // Function to update language text
    private fun updateLanguageUI(language: String) {
        val profileDatabase = ProfileDatabase(requireContext())

        // Update the button text regardless of stored profile
        binding.editProfileButton.text = if (language == "Māori") {
            getString(R.string.edit_profile_button_mr)
        } else {
            getString(R.string.edit_profile_button)
        }

        // Only change other UI elements if there is no stored profile
        if (!profileDatabase.hasProfile()) {
            if (language == "Māori") {
                // Update the text views with Māori text
                binding.userName.text = "Ingoa" // Name
                binding.userBirthday.text = "Rā Whānau"  // Birthday
                binding.userAddress.text = "Wāhitau"  // Address
                binding.motherName.text = "Ingoa o te Māmā"  // Mother's Name
                binding.fatherName.text = "Ingoa o te Pāpā"  // Father's Name
            } else {
                // Default to English
                binding.userName.text = "Name" // Name
                binding.userBirthday.text = "Birthday"
                binding.userAddress.text = "Address"
                binding.motherName.text = "Mother's Name"
                binding.fatherName.text = "Father's Name"
            }
        }
    }

    // Clears the binding reference to avoid memory leaks when the fragment's view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up the binding to avoid memory leaks
    }
}
