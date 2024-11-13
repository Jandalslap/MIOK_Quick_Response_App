package com.example.miok_quick_response_app

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.databinding.FragmentEditProfileBinding
import java.util.regex.Pattern

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

            // Perform validation
            if (newName.isBlank() || !isValidName(newName)) {
                Toast.makeText(requireContext(), "Please enter a valid name (letters only)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newEmail.isBlank() || !isValidEmail(newEmail)) {
                Toast.makeText(requireContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newBirthday.isBlank() || !isValidDate(newBirthday)) {
                Toast.makeText(requireContext(), "Please enter a valid birthday DD/MM/YYYY", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newAddress.isBlank()) {
                Toast.makeText(requireContext(), "Address cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newFatherName.isBlank()) {
                Toast.makeText(requireContext(), "Please enter the father's name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newMotherName.isBlank()) {
                Toast.makeText(requireContext(), "Please enter the mother's name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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
            binding.editName.hint = getString(R.string.name_label_mr)
            binding.editEmail.hint = getString(R.string.email_label_mr)
            binding.editBirthday.hint = getString(R.string.birthday_label_mr)
            binding.editAddress.hint = getString(R.string.address_label_mr)
            binding.editParentName1.hint = getString(R.string.father_name_label_mr)
            binding.editParentName2.hint = getString(R.string.mother_name_label_mr)
            binding.saveButton.hint = getString(R.string.save_button_mr)
        } else {
            // Default to English
            binding.nameLabel.text = getString(R.string.name_label)
            binding.emailLabel.text = getString(R.string.email_label)
            binding.birthdayLabel.text = getString(R.string.birthday_label)
            binding.addressLabel.text = getString(R.string.address_label)
            binding.parentNameLabel1.text = getString(R.string.father_name_label)
            binding.parentNameLabel2.text = getString(R.string.mother_name_label)
            binding.saveButton.text = getString(R.string.save_button)
            binding.editName.hint = getString(R.string.name_label)
            binding.editEmail.hint = getString(R.string.email_label)
            binding.editBirthday.hint = getString(R.string.birthday_label)
            binding.editAddress.hint = getString(R.string.address_label)
            binding.editParentName1.hint = getString(R.string.father_name_label)
            binding.editParentName2.hint = getString(R.string.mother_name_label)
            binding.saveButton.hint = getString(R.string.save_button)
        }
    }

    // Helper method to validate name (letters only)
    private fun isValidName(name: String): Boolean {
        return name.matches("^[A-Za-z\\s]+$".toRegex())
    }

    // Helper method to validate email format
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Helper method to validate date (basic format check for DD/MM/YYYY)
    private fun isValidDate(date: String): Boolean {
        val datePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$"
        return Pattern.matches(datePattern, date)
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
