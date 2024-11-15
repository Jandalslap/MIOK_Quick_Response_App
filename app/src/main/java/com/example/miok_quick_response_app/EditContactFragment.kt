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

class EditContactFragment : Fragment() {

    private lateinit var contactViewModel: ContactViewModel
    private lateinit var nameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var relationshipSpinner: Spinner
    private lateinit var statusRadioGroup: RadioGroup
    private lateinit var saveButton: Button
    private var contactId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_contact, container, false)

        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)

        // Bind views to layout IDs
        nameEditText = view.findViewById(R.id.contact_name_input)
        phoneNumberEditText = view.findViewById(R.id.contact_phone_number)
        relationshipSpinner = view.findViewById(R.id.contact_relationship_spinner)
        statusRadioGroup = view.findViewById(R.id.contact_status_radio_group)
        saveButton = view.findViewById(R.id.save_contact_button)

        contactId = arguments?.getInt("contactId") ?: -1

        // Observe the contact details from ViewModel and populate fields
        contactViewModel.getContactById(contactId).observe(viewLifecycleOwner) { contact ->
            contact?.let {
                nameEditText.setText(it.name)
                phoneNumberEditText.setText(it.phone_number)
                relationshipSpinner.setSelection(getRelationshipIndex(it.relationship.name))
                val radioButtonId = if (it.status) R.id.radio_yes else R.id.radio_no
                statusRadioGroup.check(radioButtonId)
            }
        }

        // Save button click listener to update the contact
        saveButton.setOnClickListener {
            val selectedRelationshipString = relationshipSpinner.selectedItem.toString()
            val selectedRelationship = mapToRelationship(selectedRelationshipString)
            val selectedStatus = statusRadioGroup.checkedRadioButtonId == R.id.radio_yes

            if (selectedRelationship != null) {
                val updatedContact = Contact(
                    id = contactId,
                    name = nameEditText.text.toString(),
                    phone_number = phoneNumberEditText.text.toString(),
                    relationship = selectedRelationship,
                    status = selectedStatus
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
}
