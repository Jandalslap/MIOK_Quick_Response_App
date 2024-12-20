package com.example.miok_quick_response_app.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.miok_quick_response_app.data.MessageContact
import ContactDatabase
import kotlin.concurrent.thread

// ViewModel class for managing and storing UI-related data for the MessageFragment, ensuring data survives configuration changes.
class MessageViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = ContactDatabase(application)

    private val _messageContacts = MutableLiveData<List<MessageContact>>()
    val messageContacts: LiveData<List<MessageContact>> get() = _messageContacts

    // Initializes the ViewModel by loading message contacts as soon as the ViewModel is created.
    init {
        loadMessageContacts()
    }

    // Loads the contacts from the database and transforms them into MessageContact objects, updating the LiveData on the main thread.
    private fun loadMessageContacts() {
        thread {
            val contacts = dbHelper.getAllContacts()
            val messageContacts = contacts.map { contact ->
                MessageContact(
                    name = contact.name,
                    relationship = contact.relationship.name.replace("_", " "),
                    phoneNumber = contact.phone_number
                )
            }
            _messageContacts.postValue(messageContacts)
        }
    }
}
