package com.example.miok_quick_response_app.data

import Contact
import Relationship
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
        val numberInput = view.findViewById<EditText>(R.id.contact_phone_number)
        val relationshipSpinner = view.findViewById<Spinner>(R.id.contact_relationship_spinner)
        val addContactButton = view.findViewById<Button>(R.id.add_contact_button)

        // Set up the spinner with default English values
        updateLanguageUI(
            sharedViewModel.currentLanguage.value,
            relationshipSpinner,
            nameInput,
            numberInput,
            addContactButton
        )
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language, relationshipSpinner, nameInput, numberInput, addContactButton)
        }
        // Set up the spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.relationship_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            relationshipSpinner.adapter = adapter
        }

        

        view.findViewById<Button>(R.id.add_contact_button).setOnClickListener {
            val name = nameInput.text.toString()
            val number = numberInput.text.toString()

            // Get selected relationship
            val relationship = when (relationshipSpinner.selectedItem.toString()) {
                "Parent/Guardian", "Matua/Kaika" -> Relationship.PARENT_GUARDIAN
                "Caregiver", "Kaiāwhina" -> Relationship.CAREGIVER
                "Aunt/Uncle", "Whaea/Tipuna" -> Relationship.AUNT_UNCLE
                "Grandparent", "Koroua/Koro" -> Relationship.GRANDPARENT
                "Ētahi Atu" -> Relationship.OTHER  // Māori translation for "Other"
                else -> Relationship.OTHER
            }

            val newContact = Contact(name, number, relationship)

            val resultBundle = Bundle().apply {
                putParcelable("new_contact", newContact)
            }
            parentFragmentManager.setFragmentResult("newContactKey", resultBundle)
            parentFragmentManager.popBackStack()
        }

        return view
    }

    // Function to update UI based on the language
    private fun updateLanguageUI(
        language: String?,
        relationshipSpinner: Spinner,
        nameInput: EditText,
        numberInput: EditText,
        addContactButton: Button
    ) {
        // Update TextView text based on language
        if (language == "Māori") {
            nameInput.hint = getString(R.string.contact_name_input_mr)
            numberInput.hint = getString(R.string.contact_phone_input_mr)
            addContactButton.text = getString(R.string.add_contact_button_mr)

            // Set the spinner entries to Māori
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.relationship_options_mr,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                relationshipSpinner.adapter = adapter
            }
        } else {
            // Default to English
            nameInput.hint = getString(R.string.contact_name_input)
            numberInput.hint = getString(R.string.contact_phone_input)
            addContactButton.text = getString(R.string.add_contact_button)

            // Set the spinner entries to English
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.relationship_options,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                relationshipSpinner.adapter = adapter
            }
        }
    }
}