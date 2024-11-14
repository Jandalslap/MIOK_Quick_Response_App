package com.example.miok_quick_response_app.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.miok_quick_response_app.R

class QuizResultFragment : Fragment() {

    // onCreateView inflates the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using inflater
        return inflater.inflate(R.layout.fragment_quiz_result, container, false)
    }

    // onViewCreated is called after the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the views by their IDs
        val quizResultImageView: ImageView = view.findViewById(R.id.quizResultImageView)
        val quizResultTextView: TextView = view.findViewById(R.id.quizResultTextView)


    }
}