package com.example.miok_quick_response_app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.viewmodel.ContactViewModel
import com.example.miok_quick_response_app.viewmodel.MessageViewModel
import com.example.miok_quick_response_app.data.MessageAdapter
import com.example.miok_quick_response_app.data.MessageContact
import com.example.miok_quick_response_app.database.ProfileDatabase
import com.example.miok_quick_response_app.databinding.FragmentMessageBinding

class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding
    private lateinit var messageViewModel: MessageViewModel
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var profileDatabase: ProfileDatabase

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_message, container, false)


        binding = FragmentMessageBinding.bind(view)

        // Observe the currentLanguage LiveData from SharedViewModel
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language)
        }

        profileDatabase = ProfileDatabase(requireContext())

        // Fetch profile name
        val profileName = profileDatabase.getProfile()?.name ?: "Unknown"
        
        // Initialize ViewModel
        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)

        // Define the onMessageClick lambda
        val onMessageClick: (MessageContact) -> Unit = { contact ->
            sendSMS(
                contact.phoneNumber,
                "Alert: ${contact.name} has triggered a safety check in the MIOK Quick Response App. " +
                        "Please review their status immediately and ensure they're okay. " +
                        "Contact us if further assistance is needed." +
                        "" +
                        "Whakamataku: Kua whakahohea e ${contact.name} tētahi arotake haumaru i te Taupānga Urupare Tere MIOK.\n" +
                        "Tēnā koa tirohia tō rātou āhuatanga ināianei, kia mārama kei te pai rātou.\n" +
                        "Whakapā mai ki a mātou mēnā ka hiahiatia he āwhina anō."

            )
        }

        // Initialize RecyclerView adapter with the callback
        messageAdapter = MessageAdapter(onMessageClick)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_message)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe and update adapter with new list
        messageViewModel.messageContacts.observe(viewLifecycleOwner) { contacts ->
            messageAdapter.submitList(contacts)
        }


// Handle "Message Everyone" button
        val messageEveryoneButton = view.findViewById<Button>(R.id.btn_message_everyone)
        messageEveryoneButton.setOnClickListener {
            messageViewModel.messageContacts.value?.let { contacts ->
                if (contacts.isNotEmpty()) {
                    // Get all contact names
                    val allNames = contacts.map { it.name }
                    // Join all names with " and "
                    val formattedNames = allNames.joinToString(" and ")

                    // Get all phone numbers
                    val allPhoneNumbers = contacts.map { it.phoneNumber }

                    // Compose the message
                    val message =
                        "Alert: $formattedNames and $profileName have triggered a safety check notification in " +
                                "the MIOK Quick Response App. " +
                                "Please review their statuses and ensure their safety.\n\n" +
                                "Whakatūpato: Kua whakaoho a $formattedNames me $profileName i tētahi whakamōhiotanga tirotiro" +
                                " haumaru i te taupānga MIOK Quick Response.\n" +
                                "Tēnā koa tirohia ō rātou āhuatanga ā tēnā whakakaha i tō rātou haumaru."

                    // Send bulk SMS
                    sendBulkSMS(allPhoneNumbers, message)
                }
            }
        }


        return view
    }

    /**
     * Sends an SMS to a single contact.
     */
    private fun sendSMS(phoneNumber: String, message: String) {
        val smsUri = Uri.parse("smsto:$phoneNumber")
        val intent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
            putExtra("sms_body", message)
        }

        try {
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

    /**
     * Sends an SMS to multiple contacts.
     */
    private fun sendBulkSMS(phoneNumbers: List<String>, message: String) {
        if (phoneNumbers.isEmpty()) {
            Toast.makeText(context, "No contacts available to message.", Toast.LENGTH_SHORT).show()
            return
        }

        val smsUri = Uri.parse("smsto:${phoneNumbers.joinToString(";")}")
        val intent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
            putExtra("sms_body", message)
        }

        try {
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
                "An error occurred while sending the messages.",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }

    // Function to update UI based on the language
    private fun updateLanguageUI(language: String) {
        // Update TextView text based on language
        if (language == "Māori") {
            binding.btnMessageEveryone.text = getString(R.string.button_message_mr)
        } else {
            // Default to English
            binding.btnMessageEveryone.text = getString(R.string.button_message)
        }
    }
}
