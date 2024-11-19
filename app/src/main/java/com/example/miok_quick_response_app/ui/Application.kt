package com.example.miok_quick_response_app.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

// Custom Application class that forces the app to always use light mode
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        // Force light mode globally
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}