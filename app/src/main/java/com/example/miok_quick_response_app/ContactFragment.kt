package com.example.miok_quick_response_app

import Contact
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.ViewModel.ContactViewModel
import com.example.miok_quick_response_app.data.ContactAdapter

class ContactFragment : Fragment() {

    private lateinit var contactViewModel: ContactViewModel
    private lateinit var contactAdapter: ContactAdapter

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_contacts, container, false)

        // Get the ViewModel
        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        // Initialize the RecyclerView
        contactAdapter = ContactAdapter {
            // Handle contact removal if needed, for now, no removal functionality
        }

        // Observe the contacts data from the ViewModel
        contactViewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactAdapter.submitList(contacts)
        }

        // Set up RecyclerView
        view.findViewById<RecyclerView>(R.id.recycler_view_tasks).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactAdapter
        }

        // Set up add contact button
        view.findViewById<View>(R.id.profile_add_task).setOnClickListener {
            findNavController().navigate(R.id.action_contactFragment_to_addContactFragment)
        }

        // Handle the result from AddContactFragment
        parentFragmentManager.setFragmentResultListener(
            "newContactKey",
            viewLifecycleOwner
        ) { _, bundle ->
            val newContact = bundle.getParcelable<Contact>("new_contact")
            newContact?.let {
                contactViewModel.addContact(it)
            }
        }

        // Observe the currentLanguage LiveData in the SharedViewModel
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update UI based on the language, e.g., changing text on buttons
            updateLanguageUI(language)
        }

        return view
    }

    private fun updateLanguageUI(language: String) {
        val button = view?.findViewById<AppCompatButton>(R.id.profile_add_task)
        // Depending on the current language, update UI elements here
        if (language == "Māori") {
            button?.text = "Tāpiri Rārangi"  // Example text change
        } else {
            button?.text = "Add Contact"  // Default English text
        }
    }
}
