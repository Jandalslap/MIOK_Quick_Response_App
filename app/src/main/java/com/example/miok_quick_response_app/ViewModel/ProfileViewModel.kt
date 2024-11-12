package com.example.miok_quick_response_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    private val _userBirthday = MutableLiveData<String>()
    val userBirthday: LiveData<String> get() = _userBirthday

    private val _userAddress = MutableLiveData<String>()
    val userAddress: LiveData<String> get() = _userAddress

    private val _profileImageUrl = MutableLiveData<String>()
    val profileImageUrl: LiveData<String> get() = _profileImageUrl

    // Add fatherName and motherName as LiveData properties
    private val _fatherName = MutableLiveData<String>()
    val fatherName: LiveData<String> get() = _fatherName

    private val _motherName = MutableLiveData<String>()
    val motherName: LiveData<String> get() = _motherName

    // Initialize user profile data
    fun initUserProfile(
        imageUrl: String,
        name: String,
        email: String,
        birthday: String,
        address: String,
        fatherName: String,
        motherName: String
    ) {
        _profileImageUrl.value = imageUrl
        _userName.value = name
        _userEmail.value = email
        _userBirthday.value = birthday
        _userAddress.value = address
        _fatherName.value = fatherName
        _motherName.value = motherName
    }
    // Function to update profile data
    fun updateUserProfile(name: String, email: String, birthday: String, address: String) {
        _userName.value = name
        _userEmail.value = email
        _userBirthday.value = birthday
        _userAddress.value = address
    }
}

// Data class for parent information (name and relationship)
data class Parent(
    val name: String,
    val relationship: String
)
