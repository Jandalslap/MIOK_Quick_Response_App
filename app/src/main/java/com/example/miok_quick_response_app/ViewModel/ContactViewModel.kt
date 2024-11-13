package com.example.miok_quick_response_app.ViewModel

import Contact
import ContactDatabaseHelper
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = ContactDatabaseHelper(application)

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts

    init {
        loadContacts()
    }

    // Load contacts from the database
    private fun loadContacts() {
        _contacts.value = dbHelper.getAllContacts()
    }

    // Add a new contact to the database
    fun addContact(contact: Contact) {
        dbHelper.insertContact(contact)
        loadContacts() // Refresh the list of contacts
    }

    fun getAllContacts(): LiveData<List<Contact>> {
        // If you're using LiveData for observing contacts, you can convert the list to LiveData
        val contacts = dbHelper.getAllContacts()
        return MutableLiveData(contacts)
    }
}

