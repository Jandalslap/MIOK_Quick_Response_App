package com.example.miok_quick_response_app.ViewModel

import Contact
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactViewModel : ViewModel() {
    private val _contactsList = MutableLiveData<MutableList<Contact>>(mutableListOf())
    val contactsList: LiveData<MutableList<Contact>> = _contactsList

    fun addContact(contact: Contact) {
        _contactsList.value?.add(contact)
    }

    fun removeContact(position: Int) {
        _contactsList.value?.let {
            it.removeAt(position)
            _contactsList.value = it  // Trigger LiveData update
        }
    }
}
