package com.example.miok_quick_response_app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Query("SELECT * FROM question_table")
    fun getAllQuestions(): List<Question>  // Return List<Question> directly, not Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Question)

    @Update
    suspend fun update(task: Question)

    @Delete
    suspend fun delete(task: Question)

}