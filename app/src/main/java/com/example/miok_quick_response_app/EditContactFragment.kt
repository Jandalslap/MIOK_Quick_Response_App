package com.example.miok_quick_response_app

import Contact
import Relationship
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.miok_quick_response_app.ViewModel.ContactViewModel
import com.example.miok_quick_response_app.databinding.FragmentEditContactBinding

class EditContactFragment : Fragment() {

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


    private fun populateContactFields(contact: Contact) {
        // Populate fields with existing contact data
        binding.contactNameInput.setText(contact.name)
        binding.contactPhoneNumber.setText(contact.phone_number)

        // Populate relationship spinner
        val relationshipOptions = resources.getStringArray(R.array.relationship_options)

        // Map the relationship to the spinner index
        val relationshipIndex = relationshipOptions.indexOf(contact.relationship.name.replace("_", " "))
        if (relationshipIndex != -1) {
            binding.contactRelationshipSpinner.setSelection(relationshipIndex)
        }

        // Set status radio button based on the contact's status
        when (contact.status) {
            true -> binding.radioYes.isChecked = true
            false -> binding.radioNo.isChecked = true
        }

        // Set urgent contact checkbox
        binding.urgentContactCheckbox.isChecked = contact.emerg_contact
    }

    private fun saveContact() {
        val name = binding.contactNameInput.text.toString()
        val phoneNumber = binding.contactPhoneNumber.text.toString()

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
    }

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
            else -> null // If the name doesn't match, return null
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
