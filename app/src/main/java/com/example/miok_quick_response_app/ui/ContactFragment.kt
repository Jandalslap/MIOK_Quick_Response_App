package com.example.miok_quick_response_app.ui

import Contact
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.viewmodel.ContactViewModel
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

        contactAdapter = ContactAdapter(
            onItemClick = { contact ->
                // Navigate to EditContactFragment with the contact's ID
                val action =
                    ContactFragmentDirections.actionContactFragmentToEditContactFragment(contactId = contact.id)
                findNavController().navigate(action)
            },
            onRemoveClick = { contact ->
                // Remove the contact when clicked on delete and show a toast
                contactViewModel.removeContact(contact)
                Toast.makeText(context, "Contact removed", Toast.LENGTH_SHORT).show()
            },
            onCallClick = { contact ->
                initiateCall(contact.phone_number) // Trigger the call logic
            },
            onMessageClick = { contact ->
                initiateMessage(contact.phone_number,contact) // Trigger the message logic
            }

        )

        // Set up RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_tasks)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = contactAdapter

        // Set up swipe-to-delete using ItemTouchHelper
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // No need to handle move
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contactToRemove = contactAdapter.currentList[position]

                // Remove the contact from the ViewModel and show a toast
                contactViewModel.removeContact(contactToRemove)
                Toast.makeText(context, "Contact removed", Toast.LENGTH_SHORT).show()
            }
        })


        // Attach the ItemTouchHelper to the RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the contacts data from the ViewModel
        contactViewModel.contacts.observe(viewLifecycleOwner, Observer { contacts ->
            contactAdapter.submitList(contacts) // Update the adapter when contacts change
        })

        contactViewModel.loadContacts()
    }

    private fun updateLanguageUI(language: String) {
        val button = view?.findViewById<AppCompatButton>(R.id.profile_add_task)

        // Update button text
        button?.text = if (language == "Māori") "Tāpiri Rārangi" else "Add Contact"

        // Notify the adapter about the language change
        contactAdapter.setLanguage(language)
    }


    private fun initiateCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(callIntent)
    }

    private fun initiateMessage(phoneNumber: String,contact: Contact) {
        val defaultMessage =
            "Alert: ${contact.name} has triggered a safety check in the MIOK Quick Response App. " +
                    "Please review their status immediately and ensure they're okay. " +
                    "Contact us if further assistance is needed." +
                    "" +
                    "Whakamataku: Kua whakahohea e ${contact.name} tētahi arotake haumaru i te Taupānga Urupare Tere MIOK.\n" +
                    "Tēnā koa tirohia tō rātou āhuatanga ināianei, kia mārama kei te pai rātou.\n" +
                    "Whakapā mai ki a mātou mēnā ka hiahiatia he āwhina anō."

        try {
            val smsUri = Uri.parse("smsto:$phoneNumber")
            val intent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
                putExtra("sms_body", defaultMessage)
            }

            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    "No SMS app available to send messages.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "An error occurred while sending the message.",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }


}
