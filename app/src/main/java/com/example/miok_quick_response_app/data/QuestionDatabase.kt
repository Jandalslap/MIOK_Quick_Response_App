package com.example.miok_quick_response_app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider
import dagger.hilt.android.qualifiers.ApplicationContext

// Import the necessary Room Database classes
@Database(entities = [Question::class], version = 1)
abstract class QuestionDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    // Define the callback for the database
    class Callback @Inject constructor(
        private val database: Provider<QuestionDatabase>, // Using Provider for lazy initialization
        @ApplicationContext private val applicationScope: CoroutineScope // Using CoroutineScope injected from Hilt
    ) : RoomDatabase.Callback() {

        // This method is called when the database is created
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().questionDao()  // Get the Dao from the injected database instance

            // Use the injected CoroutineScope to perform background tasks
            applicationScope.launch {
                dao.insert(Question("Question text 1, Question text 2, Question text 3"))
            }
        }
    }
}
