package com.example.miok_quick_response_app.data

import androidx.room.*
import com.google.android.gms.drive.query.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    fun getTasks(query: String, hideCompleted: Boolean): Flow<List<Question>>

    @Query("SELECT * FROM question_table")
    fun getAllQuestions(): Flow<List<Question>>

    // Method below is old reference method from to do list app

//    fun getQuestions(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Question>> =
//        when (sortOrder) {
//            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted)
//            SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
//        }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Question)

    @Update
    suspend fun update(task: Question)

    @Delete
    suspend fun delete(task: Question)

//    @Query("DELETE FROM QUESTION_TABLE WHERE completed = 1")
//    suspend fun deleteCompletedTasks()
}