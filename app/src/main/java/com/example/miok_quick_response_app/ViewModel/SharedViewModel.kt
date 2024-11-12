// SharedViewModel.kt
package com.example.miok_info_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _currentLanguage = MutableLiveData<String>("English") // Holds the current language
    val currentLanguage: LiveData<String> get() = _currentLanguage // Publicly accessible current language

    // Updates the current language
    fun updateLanguage(language: String) {
        _currentLanguage.value = language
    }

}
