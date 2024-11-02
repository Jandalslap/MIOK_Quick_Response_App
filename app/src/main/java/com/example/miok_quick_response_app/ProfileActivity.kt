package com.example.miok_quick_response_app

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer



class ProfileActivity : AppCompatActivity() {

    // Initialize ViewModel
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)

        // Initialize user profile with sample data
        profileViewModel.initUserProfile(
            imageUrl = "https://example.com/profile.jpg",
            name = "Lily Doe",
            email = "IamSafeAtHome@Gmail.com",
            birthday = "Sept 05, 2010",
            address = "A Block, Gate 3, Tristram Street, Hamilton",
            parents = listOf(
                Parent(name = "Lara Doe", relationship = "Mother"),
                Parent(name = "John Doe", relationship = "Father")
            )
        )

        // Observe LiveData to update UI
        profileViewModel.userName.observe(this, Observer { name ->
            findViewById<TextView>(R.id.user_name).text = name
        })

        profileViewModel.userEmail.observe(this, Observer { email ->
            findViewById<TextView>(R.id.user_email).text = email
        })

        profileViewModel.userBirthday.observe(this, Observer { birthday ->
            findViewById<TextView>(R.id.user_birthday).text = birthday
        })

        profileViewModel.userAddress.observe(this, Observer { address ->
            findViewById<TextView>(R.id.user_address).text = address
        })

        profileViewModel.userParents.observe(this, Observer { parents ->
            // Combine parent names and relationships into a single string
            val parentInfo = parents.joinToString { "${it.name} (${it.relationship})" }

          //  findViewById<TextView>(R.id.user_parents).text = parentInfo
        })

        profileViewModel.profileImageUrl.observe(this, Observer { imageUrl ->
            // Load image using an image loading library (e.g., Glide)

            //Glide.with(this).load(imageUrl).into(findViewById<ImageView>(R.id.profile_image))
        })
    }
}