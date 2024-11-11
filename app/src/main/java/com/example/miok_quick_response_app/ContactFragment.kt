package com.example.miok_quick_response_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_quick_response_app.data.Contact
import com.example.miok_quick_response_app.data.ContactAdapter

class ContactFragment : Fragment() {

    // Adapter for RecyclerView
    private lateinit var contactAdapter: ContactAdapter

    // Mutable list to hold contacts
    private val contactsList = mutableListOf<Contact>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment's layout
        val view = inflater.inflate(R.layout.fragment_profile_contacts, container, false)

        // Set up RecyclerView with adapter and layout manager
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_tasks)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize adapter with remove contact callback
        contactAdapter = ContactAdapter(contactsList) { position ->
            removeContact(position)  // Callback to remove contact
        }
        recyclerView.adapter = contactAdapter

        // Set up Add Button
        val addButton = view.findViewById<AppCompatButton>(R.id.profile_add_task)
        addButton.setOnClickListener { addContact() }

        return view
    }

    // Adds a new contact to the list
    private fun addContact() {
        // Add a new contact with a unique name for testing
        val newContact = Contact("New Contact ${contactsList.size + 1}")
        contactAdapter.addContact(newContact)
    }

    // Removes a contact from the list by position
    private fun removeContact(position: Int) {
        contactAdapter.removeContact(position)
    }
}
