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

    // Declare the binding object
    private var _binding: FragmentEditContactBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactViewModel: ContactViewModel
    private lateinit var nameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var relationshipSpinner: Spinner
    private lateinit var statusRadioGroup: RadioGroup
    private lateinit var saveButton: Button
    private lateinit var urgentContactRadioButton: RadioButton
    private lateinit var emergContact: TextView
    private var contactId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize binding
        _binding = FragmentEditContactBinding.inflate(inflater, container, false)
        return binding.root // Return the root view from binding

        val view = inflater.inflate(R.layout.fragment_edit_contact, container, false)

        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)


        // Bind views to layout IDs using ViewBinding
        nameEditText = binding.contactNameInput
        phoneNumberEditText = binding.contactPhoneNumber
        relationshipSpinner = binding.contactRelationshipSpinner
        statusRadioGroup = binding.contactStatusRadioGroup
        urgentContactRadioButton = binding.urgentContactCheckbox
        emergContact = binding.urgentContactLabel
        saveButton = binding.saveContactButton

        // Retrieve contactId passed as an argument to the fragment
        contactId = arguments?.getInt("contactId") ?: -1

        // Ensure contactId is valid before querying
        if (contactId != -1) {
            // Observe the contact details from ViewModel and populate fields
            contactViewModel.getContactById(contactId).observe(viewLifecycleOwner) { contact ->
                contact?.let {
                    // Populate the EditText fields with the existing data
                    nameEditText.setText(it.name)
                    phoneNumberEditText.setText(it.phone_number)

                    // Set the spinner selection based on the relationship of the contact
                    relationshipSpinner.setSelection(getRelationshipIndex(it.relationship.name))

                    // Set the RadioGroup based on the contact's status
                    val radioButtonId = if (it.status) R.id.radio_yes else R.id.radio_no
                    statusRadioGroup.check(radioButtonId)

                    // Set the checkbox for emergency contact status
                    urgentContactRadioButton.isChecked = it.emerg_contact
                }
            }
        }

        // Save button click listener to update the contact
        saveButton.setOnClickListener {
            val selectedRelationshipString = relationshipSpinner.selectedItem.toString()
            val selectedRelationship = mapToRelationship(selectedRelationshipString)
            val selectedStatus = statusRadioGroup.checkedRadioButtonId == R.id.radio_yes
            val isEmergencyContact = urgentContactRadioButton.isChecked

            if (selectedRelationship != null) {
                val updatedContact = Contact(
                    id = contactId,
                    name = nameEditText.text.toString(),
                    phone_number = phoneNumberEditText.text.toString(),
                    relationship = selectedRelationship,
                    status = selectedStatus,
                    emerg_contact = isEmergencyContact  // Set emergency contact status
                )

                contactViewModel.updateContact(updatedContact)

                Toast.makeText(context, "Contact updated", Toast.LENGTH_SHORT).show()
                // findNavController().popBackStack()  // Uncomment to navigate back after saving
            } else {
                Toast.makeText(context, "Invalid relationship selected", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }


    private fun getRelationshipIndex(relationshipName: String): Int {
        val relationships = resources.getStringArray(R.array.relationship_options)
        return relationships.indexOfFirst { it.equals(relationshipName, ignoreCase = true) }
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
            else -> null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up binding to avoid memory leaks
        _binding = null
    }
}
