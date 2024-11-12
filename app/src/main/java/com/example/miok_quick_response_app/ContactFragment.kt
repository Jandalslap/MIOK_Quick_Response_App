package com.example.miok_quick_response_app


import Contact
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_quick_response_app.data.ContactAdapter

class ContactFragment : Fragment() {

    private lateinit var contactAdapter: ContactAdapter
    private val contactsList = mutableListOf<Contact>()
    private val ADD_CONTACT_REQUEST_CODE = 1  // Code to identify the request

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listen for the result from AddContactFragment
        parentFragmentManager.setFragmentResultListener("newContactKey", viewLifecycleOwner) { _, bundle ->
            val newContact = bundle.getParcelable<Contact>("new_contact")
            newContact?.let {
                contactAdapter.addContact(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_contacts, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_tasks)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        contactAdapter = ContactAdapter(contactsList) { position ->
            removeContact(position)
        }
        recyclerView.adapter = contactAdapter

        val addButton = view.findViewById<AppCompatButton>(R.id.profile_add_task)
        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_contactFragment_to_addContactFragment)
        }

        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val newContact = data?.getParcelableExtra<Contact>("new_contact")
            newContact?.let {
                contactAdapter.addContact(it)
            }
        }
    }

    private fun removeContact(position: Int) {
        contactAdapter.removeContact(position)
    }
}
