package com.example.miok_quick_response_app

import Contact
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_quick_response_app.ViewModel.ContactViewModel
import com.example.miok_quick_response_app.data.ContactAdapter
class ContactFragment : Fragment() {

    private lateinit var contactViewModel: ContactViewModel
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_contacts, container, false)

        // Get the ViewModel
        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        // Initialize the adapter with a remove action callback
        contactAdapter = ContactAdapter { position ->
            // Handle the remove contact action
            contactViewModel.removeContact(position)
        }

        // Observe the contacts list
        contactViewModel.contactsList.observe(viewLifecycleOwner, Observer { contacts ->
            contactAdapter.submitList(contacts)  // Submit new list to the adapter
        })

        // Set up RecyclerView
        view.findViewById<RecyclerView>(R.id.recycler_view_tasks).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactAdapter
        }

        // Set up add task button
        view.findViewById<View>(R.id.profile_add_task).setOnClickListener {
            findNavController().navigate(R.id.action_contactFragment_to_addContactFragment)
        }

        // Set up the listener for fragment result
        parentFragmentManager.setFragmentResultListener("newContactKey", viewLifecycleOwner) { _, bundle ->
            val newContact = bundle.getParcelable<Contact>("new_contact")
            newContact?.let {
                contactViewModel.addContact(it)
            }
        }

        return view
    }
}

