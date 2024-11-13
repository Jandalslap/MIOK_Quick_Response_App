import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ContactDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_CONTACTS = "contacts"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PHONE_NUMBER = "phone_number"
        private const val COLUMN_RELATIONSHIP = "relationship"
        private const val COLUMN_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = """
            CREATE TABLE $TABLE_CONTACTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_PHONE_NUMBER TEXT NOT NULL,
                $COLUMN_RELATIONSHIP TEXT NOT NULL,
                $COLUMN_STATUS INTEGER NOT NULL DEFAULT 0
            );
        """
        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
            onCreate(db)
        }
    }

    // Insert a contact into the database
    fun insertContact(contact: Contact) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, contact.name)
            put(COLUMN_PHONE_NUMBER, contact.phone_number)
            put(COLUMN_RELATIONSHIP, contact.relationship.name)
            put(COLUMN_STATUS, if (contact.status) 1 else 0)
        }
        db.insert(TABLE_CONTACTS, null, values)
        db.close()
    }

    // Get all contacts from the database
    fun getAllContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CONTACTS, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val contact = Contact(
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    phone_number = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)),
                    relationship = Relationship.valueOf(
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                COLUMN_RELATIONSHIP
                            )
                        )
                    ),
                    status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)) == 1
                )
                contacts.add(contact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return contacts
    }
}
