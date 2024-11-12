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


class AddContactFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_contact, container, false)

        // Get references to the EditText fields
        val nameInput = view.findViewById<EditText>(R.id.contact_name_input)
        val dobInput = view.findViewById<EditText>(R.id.contact_dob_input)

        // Add Contact button listener
        view.findViewById<Button>(R.id.add_contact_button).setOnClickListener {
            val name = nameInput.text.toString()
            val dob = dobInput.text.toString()

            // Create a new Contact object (assuming Contact is Parcelable or Serializable)
            val newContact = Contact(name, dob)

            // Use a NavController to return the result back to the calling fragment
            val resultBundle = Bundle().apply {
                putParcelable("new_contact", newContact)
            }
            parentFragmentManager.setFragmentResult("newContactKey", resultBundle)
            parentFragmentManager.popBackStack()  // Close the fragment
        }

        return view
    }
}