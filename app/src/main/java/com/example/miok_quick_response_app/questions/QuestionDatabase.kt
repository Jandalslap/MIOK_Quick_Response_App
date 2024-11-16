package com.example.miok_quick_response_app.questions

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.questions.Question
import javax.inject.Singleton

@Singleton
class QuestionDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "QuestionsDB.db"

        // Tables
        private const val TABLE_TAMARIKI = "questions_tamariki"
        private const val TABLE_RANGATAHI = "questions_rangatahi"

        // Columns (same for both tables)
        private const val COLUMN_ID = "id"
        private const val COLUMN_QUESTION_TEXT_ENG = "question_text_eng"
        private const val COLUMN_QUESTION_TEXT_TR = "question_text_tr"
        private const val COLUMN_USER_INPUT_ANSWER = "user_input_answer"
        private const val COLUMN_IMAGE_RES_ID = "image_res_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Tamariki table
        val createTamarikiTable = """
            CREATE TABLE $TABLE_TAMARIKI (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_QUESTION_TEXT_ENG TEXT,
                $COLUMN_QUESTION_TEXT_TR TEXT,
                $COLUMN_USER_INPUT_ANSWER INTEGER,
                $COLUMN_IMAGE_RES_ID INTEGER
            )
        """
        db.execSQL(createTamarikiTable)

        // Create Rangatahi table
        val createRangatahiTable = """
            CREATE TABLE $TABLE_RANGATAHI (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_QUESTION_TEXT_ENG TEXT,
                $COLUMN_QUESTION_TEXT_TR TEXT,
                $COLUMN_USER_INPUT_ANSWER INTEGER,
                $COLUMN_IMAGE_RES_ID INTEGER
            )
        """
        db.execSQL(createRangatahiTable)

        // Insert predefined questions only if tables are empty
        addQuestionsToDatabase(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the tables if they exist and create them again
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TAMARIKI")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RANGATAHI")
        onCreate(db)
    }

    // Method to add each question one by one to the database
    private fun addQuestionsToDatabase(db: SQLiteDatabase) {
        // Check if questions already exist in the tables before inserting
        if (isTableEmpty(db, TABLE_TAMARIKI) && isTableEmpty(db, TABLE_RANGATAHI)) {
            // Predefined questions for Tamariki
            val questionsTamariki = listOf(
                Question("Are you okay?", "Kei te pai koe?", null, R.drawable.emoji_happy),
                Question("Are you hurt?", "Kei te pātea koe?", null, R.drawable.emoji_hurt),
                Question("Are you clean and fed?", "Kua horoia koe, kua kai?", null, R.drawable.emoji_clean),
                Question("Is someone yelling at you?", "E karanga ana te tangata ki a koe?", null, R.drawable.emoji_yelling),
                Question("Do you need help?", "Kei te hiahia awhina koe?", null, R.drawable.emoji_help)
            )

            // Predefined questions for Rangatahi
            val questionsRangatahi = listOf(
                Question("Are you okay?", "Kei te pai koe?", null, R.drawable.emoji_happy),
                Question("Are you hurt?", "Kei te pātea koe?", null, R.drawable.emoji_hurt),
                Question("Are you clean and fed?", "Kua horoia koe, kua kai?", null, R.drawable.emoji_clean),
                Question("Is someone yelling at you?", "E karanga ana te tangata ki a koe?", null, R.drawable.emoji_yelling),
                Question("Do you need help?", "Kei te hiahia awhina koe?", null, R.drawable.emoji_help)
            )

            // Insert each Tamariki question one by one
            for (question in questionsTamariki) {
                insertQuestionToTamariki(db, question)
            }

            // Insert each Rangatahi question one by one
            for (question in questionsRangatahi) {
                insertQuestionToRangatahi(db, question)
            }
        }
    }

    // Helper function to check if a table is empty
    private fun isTableEmpty(db: SQLiteDatabase, tableName: String): Boolean {
        val cursor: Cursor = db.rawQuery("SELECT COUNT(*) FROM $tableName", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count == 0
    }

    // Insert a question into the Tamariki table
    private fun insertQuestionToTamariki(db: SQLiteDatabase, question: Question) {
        val values = ContentValues().apply {
            put(COLUMN_QUESTION_TEXT_ENG, question.questionTextEng)
            put(COLUMN_QUESTION_TEXT_TR, question.questionTextTR)
            put(COLUMN_USER_INPUT_ANSWER, if (question.userInputAnswer == true) 1 else 0)
            put(COLUMN_IMAGE_RES_ID, question.imageResId)
        }
        db.insert(TABLE_TAMARIKI, null, values)
    }

    // Insert a question into the Rangatahi table
    private fun insertQuestionToRangatahi(db: SQLiteDatabase, question: Question) {
        val values = ContentValues().apply {
            put(COLUMN_QUESTION_TEXT_ENG, question.questionTextEng)
            put(COLUMN_QUESTION_TEXT_TR, question.questionTextTR)
            put(COLUMN_USER_INPUT_ANSWER, if (question.userInputAnswer == true) 1 else 0)
            put(COLUMN_IMAGE_RES_ID, question.imageResId)
        }
        db.insert(TABLE_RANGATAHI, null, values)
    }

    // Get all questions from the Tamariki table
    fun getAllQuestionsFromTamariki(): List<Question> {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_TAMARIKI, null, null, null, null, null, null)
        val questions = mutableListOf<Question>()

        if (cursor.moveToFirst()) {
            do {
                val question = Question(
                    questionTextEng = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT_ENG)),
                    questionTextTR = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT_TR)),
                    userInputAnswer = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_INPUT_ANSWER)) == 1,
                    imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_RES_ID))
                )
                questions.add(question)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return questions
    }

    // Get all questions from the Rangatahi table
    fun getAllQuestionsFromRangatahi(): List<Question> {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_RANGATAHI, null, null, null, null, null, null)
        val questions = mutableListOf<Question>()

        if (cursor.moveToFirst()) {
            do {
                val question = Question(
                    questionTextEng = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT_ENG)),
                    questionTextTR = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT_TR)),
                    userInputAnswer = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_INPUT_ANSWER)) == 1,
                    imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_RES_ID))
                )
                questions.add(question)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return questions
    }
}
