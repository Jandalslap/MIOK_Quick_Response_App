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
        val numberInput = view.findViewById<EditText>(R.id.contact_phone_number)
        val relationshipSpinner = view.findViewById<Spinner>(R.id.contact_relationship_spinner)
        val addContactButton = view.findViewById<Button>(R.id.add_contact_button)
        val statusRadioGroup = view.findViewById<RadioGroup>(R.id.contact_status_radio_group)
        val statusYesButton = view.findViewById<RadioButton>(R.id.radio_yes)
        val statusNoButton = view.findViewById<RadioButton>(R.id.radio_no)
        val contactStatus = view.findViewById<TextView>(R.id.contact_status)
        val urgentContactLabel = view.findViewById<TextView>(R.id.urgent_contact_label)
        val contactStatusInfo = view.findViewById<TextView>(R.id.contact_status_info)
        val emergContact = view.findViewById<RadioButton>(R.id.urgent_contact_checkbox)



        // Set default values: make sure the radio button defaults to 'Yes'
        statusYesButton.isChecked = true

        // Set up the spinner with default English values
        updateLanguageUI(
            sharedViewModel.currentLanguage.value,
            relationshipSpinner,
            nameInput,
            numberInput,
            addContactButton,
            contactStatus,
            urgentContactLabel,
            contactStatusInfo,
            statusYesButton,
            statusNoButton
        )
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language, relationshipSpinner, nameInput, numberInput, addContactButton, contactStatus, urgentContactLabel, contactStatusInfo, statusYesButton, statusNoButton)
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

            // Capture the selected status (Yes or No)
            val status = when (statusRadioGroup.checkedRadioButtonId) {
                R.id.radio_yes -> true  // Status is "Yes"
                R.id.radio_no -> false  // Status is "No"
                else -> false  // Default to "No" if no selection
            }

            val relationship = when (relationshipSpinner.selectedItem.toString()) {
                // English values
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
                "Whaea/Tipuna" -> Relationship.AUNT_UNCLE
                "Koroua/Koro" -> Relationship.GRANDPARENT
                "Kaimahi Tokanga" -> Relationship.SOCIAL_WORKER
                "Kaimahi Tokanga Iwi" -> Relationship.IWI_SOCIAL_WORKER
                "Pirihimana" -> Relationship.POLICE
                "Ētahi Atu" -> Relationship.OTHER

                else -> Relationship.OTHER
            }

            // Validate all fields
            if (name.isEmpty()) {
                nameInput.error = "Please enter a name"
                return@setOnClickListener
            }

            // Validate name for letters only
            if (!name.matches("^[A-Za-z\\s]+$".toRegex())) {
                nameInput.error = "Name should only contain letters"
                return@setOnClickListener
            }

            if (number.isEmpty()) {
                numberInput.error = "Please enter a phone number"
                return@setOnClickListener
            }

            // Validate phone number for numbers only
            if (!number.matches("^[0-9]+$".toRegex())) {
                numberInput.error = "Phone number should only contain digits"
                return@setOnClickListener
            }

            // Capture the emerg_contact status (whether the checkbox is selected)
            val isEmergencyContact = emergContact.isChecked

            val newContact = Contact(id = generateId(),name, number, relationship, status, isEmergencyContact)

            val resultBundle = Bundle().apply {
                putParcelable("new_contact", newContact)
            }
            parentFragmentManager.setFragmentResult("newContactKey", resultBundle)
            parentFragmentManager.popBackStack()
        }

        return view
    }
    fun generateId(): Int {
        // Generate a unique ID. For example, you could use the current system time as an ID.
        return (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
    }
    // Function to update UI based on the language
    private fun updateLanguageUI(
        language: String?,
        relationshipSpinner: Spinner,
        nameInput: EditText,
        numberInput: EditText,
        addContactButton: Button,
        contactStatus: TextView,
        urgentContactLabel: TextView,
        contactStatusInfo: TextView,
        statusYesButton: TextView,
        statusNoButton: TextView
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

            // Set the text for the RadioButtons to Māori
            contactStatus.text = getString(R.string.contact_status_mr)
            statusYesButton.text = getString(R.string.status_yes_mr)
            statusNoButton.text = getString(R.string.status_no_mr)
            urgentContactLabel.text = getString(R.string.urgent_contact_label_mr)
            contactStatusInfo.text = getString(R.string.contact_status_info_mr)


        } else {
            // Default to English
            nameInput.hint = getString(R.string.contact_name_input)
            statusYesButton.text = getString(R.string.status_yes)
            statusNoButton.text = getString(R.string.status_no)
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

            // Set the text for the RadioButtons to English
            contactStatus.text = getString(R.string.contact_status)
            urgentContactLabel.text = getString(R.string.urgent_contact_label)
            contactStatusInfo.text = getString(R.string.contact_status_info)
        }
    }

}