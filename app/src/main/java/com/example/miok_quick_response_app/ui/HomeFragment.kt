package com.example.miok_quick_response_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null // Binding object for accessing views
    private val binding get() = _binding!! // Safe access to the binding object

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the currentLanguage LiveData from SharedViewModel
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language)
        }


        // Set up click listeners for buttons
        binding.buttonQuiz.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_quizFragment)
        }

        binding.buttonContact.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_contactFragment)
        }

        binding.buttonMessage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_messageFragment)
        }
        binding.buttonProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    // Function to update UI based on the language
    private fun updateLanguageUI(language: String) {
        // Update TextView text based on language
        if (language == "Māori") {
            binding.homeTextView.text = getString(R.string.homeTextView_mr)
            binding.buttonQuiz.text = getString(R.string.button_quiz_mr)
            binding.buttonContact.text = getString(R.string.button_contact_mr)
            binding.buttonMessage.text = getString(R.string.button_message_mr)
            binding.buttonProfile.text = getString(R.string.button_profile_mr)
        } else {
            // Default to English
            binding.homeTextView.text = getString(R.string.homeTextView)
            binding.buttonQuiz.text = getString(R.string.button_quiz)
            binding.buttonContact.text = getString(R.string.button_contact)
            binding.buttonMessage.text = getString(R.string.button_message)
            binding.buttonProfile.text = getString(R.string.button_profile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding
    }

}
