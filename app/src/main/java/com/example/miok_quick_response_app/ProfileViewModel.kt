package com.example.miok_quick_response_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    // Profile image URL
    private val _profileImageUrl = MutableLiveData<String>()
    val profileImageUrl: LiveData<String> get() = _profileImageUrl

    // User name
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    // User email
    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    // User birthday
    private val _userBirthday = MutableLiveData<String>()
    val userBirthday: LiveData<String> get() = _userBirthday

    // User address
    private val _userAddress = MutableLiveData<String>()
    val userAddress: LiveData<String> get() = _userAddress

    // Parent information (name and relationship)
    private val _userParents = MutableLiveData<List<Parent>>()
    val userParents: LiveData<List<Parent>> get() = _userParents

    // Initialize user profile data
    fun initUserProfile(imageUrl: String, name: String, email: String, birthday: String, address: String, parents: List<Parent>) {
        _profileImageUrl.value = imageUrl
        _userName.value = name
        _userEmail.value = email
        _userBirthday.value = birthday
        _userAddress.value = address
        _userParents.value = parents
    }
}

// Data class for parent information (name and relationship)
data class Parent(
    val name: String,
    val relationship: String
)