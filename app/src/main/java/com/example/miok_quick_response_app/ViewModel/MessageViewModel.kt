package com.example.miok_quick_response_app.ViewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.miok_quick_response_app.data.MessageContact
import ContactDatabase
import kotlin.concurrent.thread

class MessageViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = ContactDatabase(application)

    private val _messageContacts = MutableLiveData<List<MessageContact>>()
    val messageContacts: LiveData<List<MessageContact>> get() = _messageContacts

    init {
        loadMessageContacts()
    }

    private fun loadMessageContacts() {
        thread {
            val contacts = dbHelper.getAllContacts()
            val messageContacts = contacts.map { contact ->
                MessageContact(
                    name = contact.name,
                    relationship = contact.relationship.name.replace("_", " ")
                )
            }
            _messageContacts.postValue(messageContacts)
        }
    }
}
