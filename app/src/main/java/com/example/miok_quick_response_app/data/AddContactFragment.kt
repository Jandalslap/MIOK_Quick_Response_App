package com.example.miok_quick_response_app.data

import Contact
import Relationship
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.miok_quick_response_app.R
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
        val dobInput = view.findViewById<EditText>(R.id.contact_phone_number)
        val relationshipSpinner = view.findViewById<Spinner>(R.id.contact_relationship_spinner)
        val statusRadioGroup = view.findViewById<RadioGroup>(R.id.contact_status_radio_group)
        val statusTextView = view.findViewById<TextView>(R.id.contact_status)
        val yesRadioButton = view.findViewById<RadioButton>(R.id.radio_yes)
        val noRadioButton = view.findViewById<RadioButton>(R.id.radio_no)
        val addContactButton = view.findViewById<Button>(R.id.add_contact_button)

        // Set up the spinner with default English values
        updateLanguageUI(
            sharedViewModel.currentLanguage.value,
            relationshipSpinner,
            nameInput,
            dobInput,
            addContactButton,
            statusTextView,
            yesRadioButton,
            noRadioButton

        )
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(
                language,
                relationshipSpinner,
                nameInput,
                dobInput,
                addContactButton,
                statusTextView,
                yesRadioButton,
                noRadioButton
            )
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
            val dob = dobInput.text.toString()

            // Get selected relationship
            val relationship = when (relationshipSpinner.selectedItem.toString()) {
                "Parent/Guardian", "Matua/Kaika" -> Relationship.PARENT_GUARDIAN
                "Caregiver", "Kaiāwhina" -> Relationship.CAREGIVER
                "Aunt/Uncle", "Whaea/Tipuna" -> Relationship.AUNT_UNCLE
                "Grandparent", "Koroua/Koro" -> Relationship.GRANDPARENT
                "Ētahi Atu" -> Relationship.OTHER  // Māori translation for "Other"
                else -> Relationship.OTHER
            }
            val status = when (statusRadioGroup.checkedRadioButtonId) {
                R.id.radio_yes -> true  // "Yes" selected
                R.id.radio_no -> false  // "No" selected
                else -> false  // Default to "No" if none selected
            }

            val newContact = Contact(name, dob, relationship, status = status)

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
        dobInput: EditText,
        addContactButton: Button,
        statusTextView: TextView,
        yesRadioButton: RadioButton,
        noRadioButton: RadioButton
    ) {
        // Update TextView text based on language
        if (language == "Māori") {
            nameInput.hint = getString(R.string.contact_name_input_mr)
            dobInput.hint = getString(R.string.contact_dob_input_mr)
            addContactButton.text = getString(R.string.add_contact_button_mr)
            statusTextView.text = getString(R.string.contact_status)

            // Update the RadioButton texts for Māori
            yesRadioButton.text = getString(R.string.status_yes_mr)
            noRadioButton.text = getString(R.string.status_no_mr)

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
            dobInput.hint = getString(R.string.contact_dob_input)
            addContactButton.text = getString(R.string.add_contact_button)
            statusTextView.text = getString(R.string.contact_status)
            yesRadioButton.text = getString(R.string.status_yes)
            noRadioButton.text = getString(R.string.status_no)

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