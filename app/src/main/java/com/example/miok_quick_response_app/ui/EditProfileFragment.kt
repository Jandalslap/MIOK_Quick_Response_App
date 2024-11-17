package com.example.miok_quick_response_app.ui

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.ProfileViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.databinding.FragmentEditProfileBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private val CAMERA_REQUEST_CODE = 1001
    private val GALLERY_REQUEST_CODE = 1002
    private val REQUEST_CODE_PERMISSIONS = 101

    private var imageUri: String? = null


    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Check permissions at the start
    private fun checkPermissions() {
        val permissions = mutableListOf<String>()

        // Check if camera permission is granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }

        // If permissions are not granted, request them
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), permissions.toTypedArray(), REQUEST_CODE_PERMISSIONS)
        } else {
            // Permissions are already granted, proceed with image picking
            openImagePicker()
        }
    }


    // Handle the permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, proceed with image picking
                openImagePicker()
            } else {
                // Permissions denied, show a message to the user
                Toast.makeText(requireContext(), "Permissions required to access camera and storage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Open camera or gallery for selecting profile image
    private fun openImagePicker() {
        // Implement logic to open camera or gallery (Intent-based logic)
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    // Handle the result from picking an image (from camera or gallery)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    // Get the image URI from the camera capture
                    val photoUri = data?.data  // Assuming you capture it from the camera as well
                    photoUri?.let {
                        // Save the URI to be used later for saving the profile
                        imageUri = it.toString()

                        // Update the ImageView to show the selected image
                        binding.profileImage.setImageURI(it)
                    }
                }
                GALLERY_REQUEST_CODE -> {
                    // Get the image URI from the gallery
                    val selectedImageUri = data?.data
                    selectedImageUri?.let {
                        // Save the URI to be used later for saving the profile
                        imageUri = it.toString()

                        // Update the ImageView to show the selected image
                        binding.profileImage.setImageURI(it)
                    }
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up a click listener for the profile image picker button
        binding.profileImagepick.setOnClickListener {
            // Show a dialog to choose between camera and gallery
            showImagePickerDialog()
        }


        // Observe the currentLanguage LiveData from SharedViewModel
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language)
        }

        // Load existing profile data
        profileViewModel.imageUrl.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                binding.profileImage.setImageURI(Uri.parse(it)) // Load image from URI if available
                imageUri = it // Initialize imageUri variable with the current value
            }
        }

        // Populate EditText fields with current profile data
        profileViewModel.userName.observe(viewLifecycleOwner, { name ->
            binding.editName.setText(name)
        })
        profileViewModel.userEmail.observe(viewLifecycleOwner, Observer { email ->
            binding.editEmail.setText(email)
        })

        profileViewModel.userBirthday.observe(viewLifecycleOwner, Observer { birthday ->
            // Pre-fill the birthday EditText
            binding.editBirthday.setText(birthday)

            // Parse the birthday to set the DatePicker default date if available
            if (!birthday.isNullOrEmpty()) {
                val date = dateFormatter.parse(birthday)
                if (date != null) {
                    calendar.time = date // Set the calendar to the parsed birthday
                }
            }
        })

        // Set up the DatePicker for the birthday field
        binding.editBirthday.setOnClickListener {
            // Clear focus to prevent the soft keyboard from opening
            binding.editBirthday.clearFocus()

            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    // Set the chosen date to the EditText
                    calendar.set(year, month, dayOfMonth)
                    binding.editBirthday.setText(dateFormatter.format(calendar.time))
                },
                initialYear,
                initialMonth,
                initialDay
            )

            datePickerDialog.show()
        }

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
            Log.d("EditProfile", "Image URI: $imageUri")
            val rawName = binding.editName.text.toString()
            val newName = capitalizeEachWord(rawName)
            val newEmail = binding.editEmail.text.toString()
            val newBirthday = binding.editBirthday.text.toString()
            val rawAddress = binding.editAddress.text.toString()
            val newAddress = capitalizeEachWord(rawAddress)
            val rawFatherName = binding.editParentName1.text.toString()  // Get father's name
            val newFatherName = capitalizeEachWord(rawFatherName)
            val rawMotherName = binding.editParentName2.text.toString()  // Get mother's name
            val newMotherName = capitalizeEachWord(rawMotherName)

            // Perform validation

            // Validate name: check if blank first, then validate format
            if (newName.isBlank()) {
                Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!newName.matches("^[A-Za-z\\s]+$".toRegex())) {
                Toast.makeText(requireContext(), "Name should only have letters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Enforce 30-character limit for the name
            if (newName.length > 30) {
                Toast.makeText(requireContext(), "Name cannot be more than 30 letters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate email: optional, validate length first, then format
            if (newEmail.isNotBlank()) {
                if (newEmail.length > 30) {
                    Toast.makeText(requireContext(), "Email address cannot be more than 30 characters", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (!isValidEmail(newEmail)) {
                    Toast.makeText(requireContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Validate birthday: check if blank first, then validate format
            if (newBirthday.isBlank()) {
                Toast.makeText(requireContext(), "Birthday cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Regex to check if the date matches the format DD/MM/YYYY
            val datePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$"
            if (!newBirthday.matches(datePattern.toRegex())) {
                Toast.makeText(requireContext(), "Please enter a valid birthday (DD/MM/YYYY)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate address: check if blank first, then enforce length
            if (newAddress.isBlank()) {
                Toast.makeText(requireContext(), "Address cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newAddress.length > 50) {
                Toast.makeText(requireContext(), "Address cannot be more than 50 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate father's name: check if blank first, then validate format
            if (newFatherName.isBlank()) {
                Toast.makeText(requireContext(), "Please enter the father's name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate Father's name: Letters only
            if (!newFatherName.matches("^[A-Za-z\\s]+$".toRegex())) {
                Toast.makeText(requireContext(), "Father's name should only contain letters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Enforce Father's name character limit (max 20 characters)
            if (newFatherName.length > 20) {
                Toast.makeText(requireContext(), "Father's name cannot be more than 20 letters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate mother's name: check if blank first, then validate format
            if (newMotherName.isBlank()) {
                Toast.makeText(requireContext(), "Please enter the mother's name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate Mother's name: Letters only
            if (!newMotherName.matches("^[A-Za-z\\s]+$".toRegex())) {
                Toast.makeText(requireContext(), "Mother's name should only contain letters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Enforce Mother's name character limit (max 20 characters)
            if (newMotherName.length > 20) {
                Toast.makeText(requireContext(), "Mother's name cannot be more than 20 letters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            profileViewModel.updateUserProfile(
                newName,
                newEmail,
                newBirthday,
                newAddress,
                newFatherName,
                newMotherName,
                imageUri
            )
            Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
            // Update UI immediately after saving to reflect the current language
            sharedViewModel.currentLanguage.value?.let { language ->
                updateLanguageUI(language)
            }
            findNavController().popBackStack()
        }
    }

    private fun showImagePickerDialog() {
        // Remove "Take Photo" option and just keep "Choose from Gallery"
        val options = arrayOf("Choose from Gallery", "Cancel")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Change Profile Picture")
        builder.setItems(options) { _, which ->
            when (which) {
                // 0 -> openCamera() // Open camera was here...
                0 -> openGallery() // Open gallery to choose an image
                1 -> return@setItems // Do nothing if canceled
            }
        }
        builder.show()
    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), "Camera not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }


    private fun updateLanguageUI(language: String) {
        if (language == "Māori") {
            // Update TextViews to display Māori text
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

    // Utility function to capitalize each word
    fun capitalizeEachWord(input: String): String {
        return input.trim()
            .split("\\s+".toRegex()) // Split by whitespace
            .joinToString(" ") { word -> word.lowercase().replaceFirstChar { it.uppercase() } }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
