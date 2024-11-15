package com.example.miok_quick_response_app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        // Force light mode globally
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}