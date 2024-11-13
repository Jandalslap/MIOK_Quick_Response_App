package com.example.miok_quick_response_app

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.miok_quick_response_app.database.ProfileDatabaseHelper
import com.example.miok_quick_response_app.model.Profile

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = ProfileDatabaseHelper(application)

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    private val _userBirthday = MutableLiveData<String>()
    val userBirthday: LiveData<String> get() = _userBirthday

    private val _userAddress = MutableLiveData<String>()
    val userAddress: LiveData<String> get() = _userAddress

    private val _fatherName = MutableLiveData<String>()
    val fatherName: LiveData<String> get() = _fatherName

    private val _motherName = MutableLiveData<String>()
    val motherName: LiveData<String> get() = _motherName

    init {
        loadProfile()
    }

    fun loadProfile() {
        val profile = dbHelper.getProfile()
        profile?.let {
            _userName.value = it.name
            _userEmail.value = it.email
            _userBirthday.value = it.birthday
            _userAddress.value = it.address
            _fatherName.value = it.fatherName
            _motherName.value = it.motherName
        }
    }

    fun updateUserProfile(
        newName: String,
        newEmail: String,
        newBirthday: String,
        newAddress: String,
        newFatherName: String,
        newMotherName: String
    ) {
        _userName.value = newName
        _userEmail.value = newEmail
        _userBirthday.value = newBirthday
        _userAddress.value = newAddress
        _fatherName.value = newFatherName
        _motherName.value = newMotherName

        // Create Profile object
        val profile = Profile(
            name = newName,
            email = newEmail,
            birthday = newBirthday,
            address = newAddress,
            fatherName = newFatherName,
            motherName = newMotherName,
            imageUrl = "" // Add image URL if needed
        )

        // Insert if not exists, otherwise update
        val existingProfile = dbHelper.getProfile()
        if (existingProfile == null) {
            dbHelper.insertProfile(profile) // Insert a new profile if it doesn't exist
        } else {
            dbHelper.updateProfile(profile) // Update the existing profile
        }
    }


}
