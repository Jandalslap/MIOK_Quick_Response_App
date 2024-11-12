package com.example.miok_quick_response_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null // Binding object for accessing views
    private val binding get() = _binding!! // Safe access to the binding object

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding
    }

}
