package com.example.miok_quick_response_app.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "question_table")
@Parcelize
data class Question(
    val questionText: String = "",
    val answerTrue: Boolean = false,
    val timeAnswered: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(timeAnswered)
}