package com.example.miok_quick_response_app.questions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.data.Question


class QuestionAdapter(private val questions: List<Question>) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_question, parent, false)
        return QuestionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    // ViewHolder class for each question item
    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionText: TextView = itemView.findViewById(R.id.question_text_view)
        private val trueOption: Button = itemView.findViewById(R.id.question_button_yes)
        private val falseOption: Button = itemView.findViewById(R.id.question_button_yes)

        fun bind(question: Question) {
            questionText.text = question.questionText

//            // Handle answer selection
//            optionsGroup.setOnCheckedChangeListener { _, checkedId ->
//                val isAnswerTrue = when (checkedId) {
//                    R.id.trueOption -> true
//                    R.id.falseOption -> false
//                    else -> false
//                }
//
//                if (isAnswerTrue == question.answerTrue) {
//                    // Correct answer selected
//                    // Handle the correct answer logic here (e.g., increase score, show feedback, etc.)
//                } else {
//                    // Incorrect answer selected
//                    // Handle incorrect answer logic (e.g., show feedback)
//                }
//            }
        }
    }
}