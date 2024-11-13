package com.example.miok_quick_response_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

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


        // Observe the currentLanguage LiveData from SharedViewModel
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language)
        }

        // Populate EditText fields with current profile data
        profileViewModel.userName.observe(viewLifecycleOwner, { name ->
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

        // Populate Father's and Mother's Name fields
        profileViewModel.fatherName.observe(viewLifecycleOwner, Observer { fatherName ->
            binding.editParentName1.setText(fatherName)
        })
        profileViewModel.motherName.observe(viewLifecycleOwner, Observer { motherName ->
            binding.editParentName2.setText(motherName)
        })

        // Save button logic
        binding.saveButton.setOnClickListener {
            val newName = binding.editName.text.toString()
            val newEmail = binding.editEmail.text.toString()
            val newBirthday = binding.editBirthday.text.toString()
            val newAddress = binding.editAddress.text.toString()
            val newFatherName = binding.editParentName1.text.toString()  // Get father's name
            val newMotherName = binding.editParentName2.text.toString()  // Get mother's name


            profileViewModel.updateUserProfile(
                newName,
                newEmail,
                newBirthday,
                newAddress,
                newFatherName,
                newMotherName
            )
            Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
            // Update UI immediately after saving to reflect the current language
            sharedViewModel.currentLanguage.value?.let { language ->
                updateLanguageUI(language)
            }
            requireActivity().supportFragmentManager.popBackStack() // Go back to profile fragment
        }
    }

    private fun updateLanguageUI(language: String) {
        if (language == "Māori") {
            // Update TextViews to display Māori text
            binding.nameLabel.text = getString(R.string.name_label_mr)
            binding.emailLabel.text = getString(R.string.email_label_mr)
            binding.birthdayLabel.text = getString(R.string.birthday_label_mr)
            binding.addressLabel.text = getString(R.string.address_label_mr)
            binding.parentNameLabel1.text = getString(R.string.father_name_label_mr)
            binding.parentNameLabel2.text = getString(R.string.mother_name_label_mr)
            binding.saveButton.text = getString(R.string.save_button_mr)
        } else {
            // Default to English
            binding.nameLabel.text = getString(R.string.name_label)
            binding.emailLabel.text = getString(R.string.email_label)
            binding.birthdayLabel.text = getString(R.string.birthday_label)
            binding.addressLabel.text = getString(R.string.address_label)
            binding.parentNameLabel1.text = getString(R.string.father_name_label)
            binding.parentNameLabel2.text = getString(R.string.mother_name_label)
            binding.saveButton.text = getString(R.string.save_button)
        }
    }


    // Refresh UI when fragment is resumed (e.g., language changes while fragment is in the foreground)
    override fun onResume() {
        super.onResume()
        sharedViewModel.currentLanguage.value?.let { language ->
            updateLanguageUI(language)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
