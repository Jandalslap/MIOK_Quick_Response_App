package com.example.miok_quick_response_app.miscUtil
//import android.content.Context
//import android.telephony.SmsManager
//import android.widget.Toast
//
//class SmsHelper(private val context: Context) {
//
//    // Function to send SMS message
//    fun sendSms(phoneNumber: String, message: String): Boolean {
//        return try {
//            val smsManager: SmsManager = SmsManager.getDefault()
//            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
//            Toast.makeText(context, "Message sent to: $phoneNumber", Toast.LENGTH_SHORT).show()
//            true
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Toast.makeText(context, "Failed to send message to: $phoneNumber", Toast.LENGTH_SHORT).show()
//            false
//        }
//    }
//}


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

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