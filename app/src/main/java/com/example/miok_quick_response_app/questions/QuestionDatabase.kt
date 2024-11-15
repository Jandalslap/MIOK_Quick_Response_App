package com.example.miok_quick_response_app.questions

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.questions.Question

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

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the tables if they exist and create them again
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TAMARIKI")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RANGATAHI")
        onCreate(db)
    }

    // Insert a question into the Tamariki table
    fun insertQuestionToTamariki(question: Question) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_QUESTION_TEXT_ENG, question.questionTextEng)
            put(COLUMN_QUESTION_TEXT_TR, question.questionTextTR)
            put(COLUMN_USER_INPUT_ANSWER, if (question.userInputAnswer == true) 1 else 0)
            put(COLUMN_IMAGE_RES_ID, question.imageResId)
        }
        db.insert(TABLE_TAMARIKI, null, values)
        db.close()
    }

    // Insert a question into the Rangatahi table
    fun insertQuestionToRangatahi(question: Question) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_QUESTION_TEXT_ENG, question.questionTextEng)
            put(COLUMN_QUESTION_TEXT_TR, question.questionTextTR)
            put(COLUMN_USER_INPUT_ANSWER, if (question.userInputAnswer == true) 1 else 0)
            put(COLUMN_IMAGE_RES_ID, question.imageResId)
        }
        db.insert(TABLE_RANGATAHI, null, values)
        db.close()
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

    // Method to add each question one by one to the database
    fun addQuestionsToDatabase() {
        // Predefined questions for Tamariki
        val questionsTamariki = listOf(
            Question("Are you okay?(Eng)(questionsTamariki)", "Question text 4(TR)", null, R.drawable.emoji_happy),
            Question("Are you hurt?(Eng)(questionsTamariki)", "Question text 4(TR)", null, R.drawable.emoji_hurt),
            Question("Are you clean and fed?(Eng)(questionsTamariki)", "Question text 4(TR)", null, R.drawable.emoji_clean),
            Question("Is someone yelling at you?(Eng)(questionsTamariki)", "Question text 4(TR)", null, R.drawable.emoji_yelling)
        )

        // Predefined questions for Rangatahi
        val questionsRangatahi = listOf(
            Question("Are you okay?(Eng)(questionsRangatahi)", "Question text 4(TR)", null, R.drawable.emoji_happy),
            Question("Are you hurt?(Eng)(questionsRangatahi)", "Question text 4(TR)", null, R.drawable.emoji_hurt),
            Question("Are you clean and fed?(Eng)(questionsRangatahi)", "Question text 4(TR)", null, R.drawable.emoji_clean),
            Question("Is someone yelling at you?(Eng)(questionsRangatahi)", "Question text 4(TR)", null, R.drawable.emoji_yelling)
        )

        // Insert each Tamariki question one by one
        for (question in questionsTamariki) {
            insertQuestionToTamariki(question)
        }

        // Insert each Rangatahi question one by one
        for (question in questionsRangatahi) {
            insertQuestionToRangatahi(question)
        }
    }
}