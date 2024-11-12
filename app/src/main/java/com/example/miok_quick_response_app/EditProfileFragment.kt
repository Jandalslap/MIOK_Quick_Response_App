package com.example.miok_quick_response_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.miok_quick_response_app.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Populate EditText fields with current profile data
        profileViewModel.userName.observe(viewLifecycleOwner, Observer { name ->
            binding.editName.setText(name)
        })
        profileViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            binding.editEmail.setText(email)
        })
        profileViewModel.userBirthday.observe(viewLifecycleOwner, Observer { birthday ->
            binding.editBirthday.setText(birthday)
        })
        profileViewModel.userAddress.observe(viewLifecycleOwner, Observer { address ->
            binding.editAddress.setText(address)
        })

        // Save button logic
        binding.saveButton.setOnClickListener {
            val newName = binding.editName.text.toString()
            val newEmail = binding.editEmail.text.toString()
            val newBirthday = binding.editBirthday.text.toString()
            val newAddress = binding.editAddress.text.toString()

            profileViewModel.updateUserProfile(newName, newEmail, newBirthday, newAddress)
            Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack() // Go back to profile fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
