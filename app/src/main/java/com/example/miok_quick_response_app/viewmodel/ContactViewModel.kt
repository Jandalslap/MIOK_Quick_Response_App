package com.example.miok_quick_response_app.viewmodel

import Contact
import ContactDatabase
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import kotlin.concurrent.thread

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = ContactDatabase(application)

    // LiveData to observe the list of contacts
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts

    init {
        loadContacts()  // Load contacts initially
    }

    // Load contacts from the database and post the results to LiveData
    fun loadContacts() {
        thread {  // Use a background thread to prevent blocking the UI
            val contactList = dbHelper.getAllContacts()
            Log.d("ContactViewModel", "Contacts loaded: $contactList")
            // Post the updated contact list to LiveData from the main thread
            Handler(Looper.getMainLooper()).post {
                _contacts.postValue(contactList)
            }
        }
    }

    // Add a new contact to the database and refresh the contacts list
    fun addContact(contact: Contact) {
        thread {  // Background thread for database operations
            dbHelper.insertContact(contact)
            loadContacts()  // Refresh the list after insertion
        }
    }

    // Remove a contact from the database and update LiveData
    fun removeContact(contact: Contact) {
        thread {  // Background thread
            dbHelper.removeContact(contact)
            loadContacts()  // Refresh the list after deletion
        }
    }

    // Get contact by ID
    fun getContactById(id: Int): LiveData<Contact?> {
        val contactData = MutableLiveData<Contact?>()
        thread {
            val contact = dbHelper.getContactById(id)
            contactData.postValue(contact)
        }
        return contactData
    }

    // Update an existing contact in the database and refresh the contacts list
    fun updateContact(contact: Contact) {
        thread {
            dbHelper.updateContact(contact)
            loadContacts()  // Refresh the list after updating
        }
    }

}
