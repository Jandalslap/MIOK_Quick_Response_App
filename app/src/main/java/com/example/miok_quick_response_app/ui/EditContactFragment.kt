package com.example.miok_quick_response_app.ui

import Contact
import Relationship
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.viewmodel.ContactViewModel
import com.example.miok_quick_response_app.databinding.FragmentEditContactBinding

// Fragment to edit contact information
class EditContactFragment : Fragment() {

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentEditContactBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactViewModel: ContactViewModel
    private var contactId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)

        val relationshipSpinner = view.findViewById<Spinner>(R.id.contact_relationship_spinner)
        val saveContactButton = view.findViewById<Button>(R.id.save_contact_button)
        val statusRadioGroup = view.findViewById<RadioGroup>(R.id.contact_status_radio_group)
        val statusYesButton = view.findViewById<RadioButton>(R.id.radio_yes)
        val statusNoButton = view.findViewById<RadioButton>(R.id.radio_no)
        val contactStatus = view.findViewById<TextView>(R.id.contact_status)
        val urgentContactLabel = view.findViewById<TextView>(R.id.urgent_contact_label)
        val contactStatusInfo = view.findViewById<TextView>(R.id.contact_status_info)
        val emergContact = view.findViewById<RadioButton>(R.id.urgent_contact_checkbox)

        // Set up the spinner with default English values
        updateLanguageUI(
            sharedViewModel.currentLanguage.value,
            relationshipSpinner,
            saveContactButton,
            contactStatus,
            urgentContactLabel,
            contactStatusInfo,
            statusYesButton,
            statusNoButton
        )
        // Observe the shared view model for language updates
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language, relationshipSpinner, saveContactButton, contactStatus, urgentContactLabel, contactStatusInfo, statusYesButton, statusNoButton)
        }

        // Set up the relationship spinner adapter
        setupRelationshipSpinner()

        // Retrieve the contactId passed from the arguments
        contactId = arguments?.getInt("contactId") ?: -1

        if (contactId != -1) {
            // Fetch and observe the contact details
            contactViewModel.getContactById(contactId).observe(viewLifecycleOwner) { contact ->
                contact?.let {
                    populateContactFields(it)
                }
            }
        }

        // Set up the Save button logic
        binding.saveContactButton.setOnClickListener {
            saveContact()
        }
    }

    // Set up the relationship spinner adapter
    private fun setupRelationshipSpinner() {
        val relationshipOptions = resources.getStringArray(R.array.relationship_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, relationshipOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.contactRelationshipSpinner.adapter = adapter
    }

    // Function to populate the edit contact form fields
    private fun populateContactFields(contact: Contact) {
        // Populate fields with existing contact data
        binding.contactNameInput.setText(contact.name)
        binding.contactPhoneNumber.setText(contact.phone_number)

        // Populate relationship spinner
        val relationshipOptions = resources.getStringArray(R.array.relationship_options)

        // Get the user-friendly name of the relationship from the enum
        val relationshipUserFriendlyName = mapFromRelationship(contact.relationship)

        // Find the index of the user-friendly name in the spinner options
        val selectedIndex = relationshipOptions.indexOf(relationshipUserFriendlyName)

        // Set the spinner selection if a match is found
        if (selectedIndex >= 0) {
            binding.contactRelationshipSpinner.setSelection(selectedIndex)
        } else {
            binding.contactRelationshipSpinner.setSelection(0) // Default to the first option
        }

        // Set status radio button based on the contact's status
        when (contact.status) {
            true -> binding.radioYes.isChecked = true
            false -> binding.radioNo.isChecked = true
        }

        // Set urgent contact checkbox
        binding.urgentContactCheckbox.isChecked = contact.emerg_contact
    }

    // Function to save contact form
    private fun saveContact() {
        val rawName = binding.contactNameInput.text.toString()
        val name = capitalizeEachWord(rawName)
        val phoneNumber = binding.contactPhoneNumber.text.toString()

        // Validate all fields
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate name for letters only
        if (!name.matches("^[A-Za-z\\s]+$".toRegex())) {
            Toast.makeText(requireContext(), "Name should only contain letters", Toast.LENGTH_SHORT).show()
            return
        }

        // Enforce name character limit (max 30 characters)
        if (name.length > 30) {
            Toast.makeText(requireContext(), "Name cannot be more than 30 letters", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate if phone number not empty
        if (phoneNumber.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a phone number", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate phone number for numbers only
        if (!phoneNumber.matches("^[0-9]+$".toRegex())) {
            Toast.makeText(requireContext(), "Phone number should only contain numbers", Toast.LENGTH_SHORT).show()
            return
        }

        // Enforce phone number digit limit (between 8 and 12 digits)
        if (phoneNumber.length !in 8..12) {
            Toast.makeText(requireContext(), "Phone number should be between 8 and 12 numbers", Toast.LENGTH_SHORT).show()
            return
        }


        // Get selected relationship from spinner
        val selectedRelationship = binding.contactRelationshipSpinner.selectedItem.toString()
        // Map the selected user-friendly name to the corresponding Relationship enum
        val relationship = mapToRelationship(selectedRelationship)
            ?: run {
                // If mapping fails, show an error message and return early
                Toast.makeText(requireContext(), "Invalid relationship selected", Toast.LENGTH_SHORT).show()
                return
            }

        // Get the status from radio group
        val isApproved = binding.contactStatusRadioGroup.checkedRadioButtonId == R.id.radio_yes

        // Get the urgent contact status
        val isUrgentContact = binding.urgentContactCheckbox.isChecked

        // Create a new or updated Contact object
        val updatedContact = Contact(
            id = contactId,
            name = name,
            phone_number = phoneNumber,
            relationship = relationship,
            status = isApproved,
            emerg_contact = isUrgentContact
        )

        // Save the updated contact through ViewModel
        contactViewModel.updateContact(updatedContact)

        Toast.makeText(requireContext(), "Contact updated successfully.", Toast.LENGTH_SHORT).show()
        // Navigate back to contact page
        findNavController().popBackStack()
    }

    // Maps user-friendly relationship names (both English and Māori) to the corresponding Relationship enum value
    private fun mapToRelationship(userFriendlyName: String): Relationship? {
        return when (userFriendlyName) {
            "Parent/Guardian" -> Relationship.PARENT_GUARDIAN
            "Caregiver" -> Relationship.CAREGIVER
            "Aunt/Uncle" -> Relationship.AUNT_UNCLE
            "Grandparent" -> Relationship.GRANDPARENT
            "Social Worker" -> Relationship.SOCIAL_WORKER
            "Iwi Social Worker" -> Relationship.IWI_SOCIAL_WORKER
            "Police" -> Relationship.POLICE
            "Other" -> Relationship.OTHER

            // Māori translations
            "Matua/Kaika" -> Relationship.PARENT_GUARDIAN
            "Kaiāwhina" -> Relationship.CAREGIVER
            "Whaea/Matua" -> Relationship.AUNT_UNCLE
            "Koroua/Koro" -> Relationship.GRANDPARENT
            "Kaimahi Tokanga" -> Relationship.SOCIAL_WORKER
            "Kaimahi Tokanga Iwi" -> Relationship.IWI_SOCIAL_WORKER
            "Pirihimana" -> Relationship.POLICE
            "Ētahi Atu" -> Relationship.OTHER

            else -> null // If the name doesn't match, return null
        }
    }

    // Function to update UI based on the language
    private fun updateLanguageUI(
        language: String?,
        relationshipSpinner: Spinner,
        saveContactButton: Button,
        contactStatus: TextView,
        urgentContactLabel: TextView,
        contactStatusInfo: TextView,
        statusYesButton: TextView,
        statusNoButton: TextView
    ) {
        // Update TextView text based on language
        if (language == "Māori") {

            saveContactButton.text = getString(R.string.save_contact_button_mr)

            // Set the spinner entries to Māori
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.relationship_options_mr,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                relationshipSpinner.adapter = adapter
            }

            // Set the text for the RadioButtons to Māori
            contactStatus.text = getString(R.string.contact_status_mr)
            statusYesButton.text = getString(R.string.status_yes_mr)
            statusNoButton.text = getString(R.string.status_no_mr)
            urgentContactLabel.text = getString(R.string.urgent_contact_label_mr)
            contactStatusInfo.text = getString(R.string.contact_status_info_mr)


        } else {
            // Default to English
            statusYesButton.text = getString(R.string.status_yes)
            statusNoButton.text = getString(R.string.status_no)
            saveContactButton.text = getString(R.string.save_contact_button)

            // Set the spinner entries to English
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.relationship_options,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                relationshipSpinner.adapter = adapter
            }

            // Set the text for the RadioButtons to English
            contactStatus.text = getString(R.string.contact_status)
            urgentContactLabel.text = getString(R.string.urgent_contact_label)
            contactStatusInfo.text = getString(R.string.contact_status_info)
        }
    }

    // Maps a Relationship enum value to its corresponding user-friendly string representation
    private fun mapFromRelationship(relationship: Relationship): String {
        return when (relationship) {
            Relationship.PARENT_GUARDIAN -> "Parent/Guardian"
            Relationship.CAREGIVER -> "Caregiver"
            Relationship.AUNT_UNCLE -> "Aunt/Uncle"
            Relationship.GRANDPARENT -> "Grandparent"
            Relationship.SOCIAL_WORKER -> "Social Worker"
            Relationship.IWI_SOCIAL_WORKER -> "Iwi Social Worker"
            Relationship.POLICE -> "Police"
            Relationship.OTHER -> "Other"
        }
    }

    // Capitalizes the first letter of each word in the input string, and ensures the rest of the letters are lowercase.
    fun capitalizeEachWord(input: String): String {
        return input.trim()
            .split("\\s+".toRegex()) // Split by whitespace
            .joinToString(" ") { word -> word.lowercase().replaceFirstChar { it.uppercase() } }
    }

    // Clears the binding reference to avoid memory leaks when the fragment's view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
