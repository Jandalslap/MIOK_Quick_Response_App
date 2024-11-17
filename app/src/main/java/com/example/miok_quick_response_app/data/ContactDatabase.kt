import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ContactDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 2 //1
        private const val TABLE_CONTACTS = "contacts"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PHONE_NUMBER = "phone_number"
        private const val COLUMN_RELATIONSHIP = "relationship"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_EMERG_CONTACT = "emerg_contact"

        // Singleton instance
        @Volatile
        private var INSTANCE: ContactDatabase? = null

        // Get instance method for singleton access
        fun getInstance(context: Context): ContactDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ContactDatabase(context).also { INSTANCE = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = """
            CREATE TABLE $TABLE_CONTACTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_PHONE_NUMBER TEXT NOT NULL,
                $COLUMN_RELATIONSHIP TEXT NOT NULL,
                $COLUMN_STATUS INTEGER NOT NULL,
                $COLUMN_EMERG_CONTACT INTEGER NOT NULL
            );
        """
        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            //db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
            //onCreate(db)
            db.execSQL("ALTER TABLE $TABLE_CONTACTS ADD COLUMN $COLUMN_EMERG_CONTACT INTEGER NOT NULL DEFAULT 0")
            //Jung
        }
    }

    // Insert a contact into the database
    @Synchronized
    fun insertContact(contact: Contact) {
        val db = writableDatabase

        // If the contact is an emergency contact, make sure to unset any existing emergency contact
        if (contact.emerg_contact) {
            // Unset the current emergency contact, if it exists
            db.execSQL("UPDATE $TABLE_CONTACTS SET $COLUMN_EMERG_CONTACT = 0 WHERE $COLUMN_EMERG_CONTACT = 1")
        }

        val values = ContentValues().apply {
            put(COLUMN_NAME, contact.name)
            put(COLUMN_PHONE_NUMBER, contact.phone_number)
            put(COLUMN_RELATIONSHIP, contact.relationship.name)
            put(COLUMN_STATUS, if (contact.status) 1 else 0)
            put(COLUMN_EMERG_CONTACT, if (contact.emerg_contact) 1 else 0)
        }
        db.insert(TABLE_CONTACTS, null, values)
        db.close()
    }

    // Get all contacts from the database
    @Synchronized
    fun getAllContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CONTACTS, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val phoneNumber =
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER))
                val relationship = Relationship.valueOf(
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_RELATIONSHIP)
                    )
                )
                val status =
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)) == 1  // 1 = true, 0 = false

                val emergContact =
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EMERG_CONTACT)) == 1  // 1 = true, 0 = false

                val contact = Contact(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = name,
                    phone_number = phoneNumber,
                    relationship = relationship,
                    status = status,
                    emerg_contact = emergContact
                )

                contacts.add(contact)

                // Log or display the status text if needed
                println(contact.statusText)  // This will print "Status: Approved" or "Status: Not Approved"
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return contacts
    }

    // Remove a contact from the database by name or phone number (depending on uniqueness)
    fun removeContact(contact: Contact) {
        val db = writableDatabase
        val selection = "$COLUMN_PHONE_NUMBER = ?"
        val selectionArgs = arrayOf(contact.phone_number) // Assuming phone number is unique
        db.delete(TABLE_CONTACTS, selection, selectionArgs)
        db.close()
    }

    fun getContactById(id: Int): Contact? {
        val db = readableDatabase
        var contact: Contact? = null
        val cursor = db.query(
            TABLE_CONTACTS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER))
            val relationship = Relationship.valueOf(
                cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_RELATIONSHIP)
                )
            )
            val status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)) == 1

            val emergContact = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EMERG_CONTACT)) == 1

            contact = Contact(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = name,
                phone_number = phoneNumber,
                relationship = relationship,
                status = status,
                emerg_contact = emergContact
            )
        }
        cursor.close()
        //db.close()
        return contact
    }

    @Synchronized
    fun updateContact(contact: Contact) {
        val db = writableDatabase

        // Log the emergency contact status before making changes in the database
        Log.d("ContactDatabase", "Updating contact: ${contact.name}, Emergency Contact: ${contact.emerg_contact}")

        // If the contact is an emergency contact, make sure to unset any existing emergency contact
        if (contact.emerg_contact) {
            // Unset the current emergency contact, if it exists
            db.execSQL("UPDATE $TABLE_CONTACTS SET $COLUMN_EMERG_CONTACT = 0 WHERE $COLUMN_EMERG_CONTACT = 1")
        }

        // Prepare the new contact values to be updated in the database
        val values = ContentValues().apply {
            put(COLUMN_NAME, contact.name)
            put(COLUMN_PHONE_NUMBER, contact.phone_number)
            put(COLUMN_RELATIONSHIP, contact.relationship.name)
            put(COLUMN_STATUS, if (contact.status) 1 else 0)
            put(COLUMN_EMERG_CONTACT, if (contact.emerg_contact) 1 else 0)
        }

        // Update the contact record in the database
        db.update(TABLE_CONTACTS, values, "$COLUMN_ID = ?", arrayOf(contact.id.toString()))
        db.close()
    }


    @SuppressLint("Range")
    fun getEmergencyContact(): String? {
        val db = readableDatabase
        val query = "SELECT $COLUMN_PHONE_NUMBER FROM $TABLE_CONTACTS WHERE $COLUMN_EMERG_CONTACT = 1 LIMIT 1"
        val cursor = db.rawQuery(query, null)
        var phoneNumber: String? = null

        if (cursor.moveToFirst()) {
            phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER))
        }
        cursor.close()
        db.close()
        return phoneNumber
    }


}
