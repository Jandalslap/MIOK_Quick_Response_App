package com.example.miok_quick_response_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.databinding.FragmentDisclaimerBinding

// Fragment that displays a disclaimer to the user
class DisclaimerFragment : Fragment() {

    private var _binding: FragmentDisclaimerBinding? = null
    private val binding get() = _binding!!

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisclaimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the currentLanguage LiveData from SharedViewModel
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language)
        }

        binding.acceptButton.setOnClickListener {
            findNavController().navigate(R.id.action_disclaimerFragment_to_homeFragment)
        }

        binding.declineButton.setOnClickListener {
            requireActivity().finish() // Close the app
        }
    }

    // Function to update UI based on the language
    private fun updateLanguageUI(language: String) {
        // Update TextView text based on language
        if (language == "Māori") {
            binding.disclaimerText.text = getString(R.string.disclaimer_text_mr)
            binding.acceptButton.text = getString(R.string.acceptButton_mr)
            binding.declineButton.text = getString(R.string.declineButton_mr)
            binding.disclaimerTitle.text = getString(R.string.disclaimer_title_mr)

        } else {
            // Default to English
            binding.disclaimerText.text = getString(R.string.disclaimer_text)
            binding.acceptButton.text = getString(R.string.acceptButton)
            binding.declineButton.text = getString(R.string.declineButton)
            binding.disclaimerTitle.text = getString(R.string.disclaimer_title)
        }
    }

    // Cleans up the binding reference when the view is destroyed to prevent memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
