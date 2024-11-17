package com.example.miok_quick_response_app.miscUtil

import android.content.Context
import com.example.miok_quick_response_app.model.Profile
import com.google.firebase.firestore.FirebaseFirestore


class FirestoreHelper() {

    // Will cause crash if this class is intantiated
    //private val db = FirebaseFirestore.getInstance()

    // Insert profile data and return the generated ID
    fun addProfile(profile: Profile): String? {
        try {
            // Create a map of the profile fields (excluding the 'id' since Firestore will generate one)
            val profileData = hashMapOf(
                "name" to profile.name,
                "email" to profile.email,
                "birthday" to profile.birthday,
                "address" to profile.address,
                "fatherName" to profile.fatherName,
                "motherName" to profile.motherName,
                "imageUrl" to profile.imageUrl
            )

            // Add the profile data to the "profiles" collection (Firestore will generate an ID)
            //val docRef = db.collection("profiles").add(profileData).await()

            // Return the generated document ID
            return ""//docRef.id
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}