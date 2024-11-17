package com.example.miok_quick_response_app.miscUtil
import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast

class SmsHelper(private val context: Context) {

    // Function to send SMS message
    fun sendSms(phoneNumber: String, message: String): Boolean {
        return try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(context, "Message sent to: $phoneNumber", Toast.LENGTH_SHORT).show()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to send message to: $phoneNumber", Toast.LENGTH_SHORT).show()
            false
        }
    }
}