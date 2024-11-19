package com.example.miok_quick_response_app.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

// Helper class to handle SMS-related operations
class SmsHelper(private val context: Context) {

    // Function to open the SMS app with a list of numbers and a message pre-filled
    fun openSmsAppWithMessage(phoneNumbers: List<String>, message: String) {
        // Build the recipient numbers string as a comma-separated list
        val phoneNumberString = phoneNumbers.joinToString(",")

        // Create an Intent to open the SMS app
        val smsIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("sms:$phoneNumberString") // Set phone numbers as the recipient
            putExtra("sms_body", message) // Set the message as the body
            flags = Intent.FLAG_ACTIVITY_NEW_TASK // Ensure the SMS app opens in a new task
        }

        // Check if there is an app that can handle the SMS intent
        if (smsIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(smsIntent) // Open the SMS app
        } else {
            Toast.makeText(context, "No SMS app found", Toast.LENGTH_SHORT).show()
        }
    }
}