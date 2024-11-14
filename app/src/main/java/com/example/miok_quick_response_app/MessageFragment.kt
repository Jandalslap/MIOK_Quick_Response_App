package com.example.miok_quick_response_app

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_quick_response_app.ViewModel.ContactViewModel
import com.example.miok_quick_response_app.ViewModel.MessageViewModel
import com.example.miok_quick_response_app.data.MessageAdapter
import com.example.miok_quick_response_app.data.MessageContact


class MessageFragment : Fragment() {

    private lateinit var messageViewModel: MessageViewModel
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var contactViewModel: ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_message, container, false)

        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)

        // Define the onMessageClick lambda
        val onMessageClick: (MessageContact) -> Unit = { contact ->
            // Handle the message button click for this contact
            // For example, navigate to a new fragment, or show a message dialog, etc.
            // Example: navigate to a messaging screen (this is just a placeholder)
            // val action = HomeFragmentDirections.actionHomeFragmentToMessageFragment(contact)
            // findNavController().navigate(action)
            val smsUri = Uri.parse("smsto:${contact.phoneNumber}")
            val intent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
                putExtra(
                    "sms_body", "Alert: ${contact.name} has triggered a safety check in the" +
                            " MIOK Quick Response App. Please review their status immediately and ensure they're" +
                            " okay. Contact us if further assistance is needed."
                )
            }

            // Verify that there's an app available to handle SMS
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(context, "No SMS app available", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(context, "Button for ${contact.name} clicked", Toast.LENGTH_SHORT).show()
        }

        // Pass the onMessageClick callback to the MessageAdapter
        messageAdapter = MessageAdapter(onMessageClick)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_message)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe and update adapter with new list
        messageViewModel.messageContacts.observe(viewLifecycleOwner, { contacts ->
            messageAdapter.submitList(contacts)
        })

        return view
    }
}
