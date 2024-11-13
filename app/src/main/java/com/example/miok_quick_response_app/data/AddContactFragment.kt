package com.example.miok_quick_response_app.data

import Contact
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.miok_quick_response_app.R
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import com.example.miok_info_app.viewmodel.SharedViewModel

class AddContactFragment : Fragment() {

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_contact, container, false)

        val nameInput = view.findViewById<EditText>(R.id.contact_name_input)
        val dobInput = view.findViewById<EditText>(R.id.contact_dob_input)
        val relationshipSpinner = view.findViewById<Spinner>(R.id.contact_relationship_spinner)

        // Set up the spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.relationship_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            relationshipSpinner.adapter = adapter
        }

        // Observe the currentLanguage LiveData from SharedViewModel
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language)
        }

        view.findViewById<Button>(R.id.add_contact_button).setOnClickListener {
            val name = nameInput.text.toString()
            val dob = dobInput.text.toString()

            // Get selected relationship
            val relationship = when (relationshipSpinner.selectedItem.toString()) {
                "Parent/Guardian" -> Relationship.PARENT_GUARDIAN
                "Caregiver" -> Relationship.CAREGIVER
                "Aunt/Uncle" -> Relationship.AUNT_UNCLE
                "Grandparent" -> Relationship.GRANDPARENT
                else -> Relationship.OTHER
            }

            val newContact = Contact(name, dob, relationship)

            val resultBundle = Bundle().apply {
                putParcelable("new_contact", newContact)
            }
            parentFragmentManager.setFragmentResult("newContactKey", resultBundle)
            parentFragmentManager.popBackStack()
        }

        return view
    }

    // Function to update UI based on the language
    private fun updateLanguageUI(language: String) {
        // Update TextView text based on language
        if (language == "Māori") {

        } else {
            // Default to English

        }
    }
}