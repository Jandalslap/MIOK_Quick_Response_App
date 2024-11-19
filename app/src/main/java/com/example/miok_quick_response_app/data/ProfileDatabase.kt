package com.example.miok_quick_response_app.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.example.miok_quick_response_app.model.Profile

// A class for managing the SQLite database that stores user profile information, including database schema and constants.
class ProfileDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ProfileDB.db"
        private const val TABLE_PROFILE = "profile"

        // Profile table columns
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_BIRTHDAY = "birthday"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_FATHER_NAME = "father_name"
        private const val COLUMN_MOTHER_NAME = "mother_name"
        private const val COLUMN_IMAGE_URL = "image_url"
    }

    // Creates the profile table in the database with columns for personal information when the database is created.
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_PROFILE ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAME TEXT, "
                + "$COLUMN_EMAIL TEXT, "
                + "$COLUMN_BIRTHDAY TEXT, "
                + "$COLUMN_ADDRESS TEXT, "
                + "$COLUMN_FATHER_NAME TEXT, "
                + "$COLUMN_MOTHER_NAME TEXT, "
                + "$COLUMN_IMAGE_URL TEXT)")
        db.execSQL(createTable)
    }

    // Drops the existing profile table and recreates it when the database is upgraded to a new version.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROFILE")
        onCreate(db)
    }

    // Adds a new profile record to the profile table in the database.
    fun addProfile(profile: Profile) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, profile.name)
            put(COLUMN_EMAIL, profile.email)
            put(COLUMN_BIRTHDAY, profile.birthday)
            put(COLUMN_ADDRESS, profile.address)
            put(COLUMN_FATHER_NAME, profile.fatherName)
            put(COLUMN_MOTHER_NAME, profile.motherName)
            put(COLUMN_IMAGE_URL, profile.imageUrl)
        }
        db.insert(TABLE_PROFILE, null, values)
        db.close()
    }

    // Function to get profile from database
    fun getProfile(): Profile? {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_PROFILE, null, null, null, null, null, null
        )

        Log.d("ProfileDatabaseHelper", "Cursor count: ${cursor.count}")

        if (cursor.moveToFirst()) {
            val profile = Profile(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                birthday = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTHDAY)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                fatherName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FATHER_NAME)),
                motherName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOTHER_NAME)),
                imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
            )
            cursor.close()
            return profile
        } else {
            cursor.close()
            Log.d("ProfileDatabaseHelper", "No profile found in database")
            return null
        }
    }

    // Inserts a new profile record into the profile table in the database.
    fun insertProfile(profile: Profile) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, profile.name)
            put(COLUMN_EMAIL, profile.email)
            put(COLUMN_BIRTHDAY, profile.birthday)
            put(COLUMN_ADDRESS, profile.address)
            put(COLUMN_FATHER_NAME, profile.fatherName)
            put(COLUMN_MOTHER_NAME, profile.motherName)
            put(COLUMN_IMAGE_URL, profile.imageUrl)
        }
        db.insert(TABLE_PROFILE, null, values)
        db.close()
    }

    // Function to update an existing profile in the database by its ID
    fun updateProfile(profile: Profile) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, profile.name)
            put(COLUMN_EMAIL, profile.email)
            put(COLUMN_BIRTHDAY, profile.birthday)
            put(COLUMN_ADDRESS, profile.address)
            put(COLUMN_FATHER_NAME, profile.fatherName)
            put(COLUMN_MOTHER_NAME, profile.motherName)
            put(COLUMN_IMAGE_URL, profile.imageUrl)
        }
        db.update(TABLE_PROFILE, values, "$COLUMN_ID = ?", arrayOf("1"))
        db.close()
    }

    // Function to check if at least one profile exists in the database
    fun hasProfile(): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_PROFILE", null)
        return cursor.use {
            it.moveToFirst()
            it.getInt(0) > 0 // Returns true if there is at least one profile
        }
    }

}
