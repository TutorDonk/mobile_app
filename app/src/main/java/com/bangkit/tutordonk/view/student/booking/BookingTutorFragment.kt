package com.bangkit.tutordonk.view.student.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.FragmentStudentBookingTutorBinding

class BookingTutorFragment : Fragment() {
    private var _binding: FragmentStudentBookingTutorBinding? = null
    private val binding get() = _binding!!
    private var selectedStudy = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentBookingTutorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLayoutListener() = with(binding) {
        spinnerStudy.setOnItemSelectedListener { study ->
            selectedStudy = study
            changeItemSubStudy()
        }
        mcbSelectSubStudy.setOnCheckedChangeListener { _, isChecked ->
            changeItemSubStudy()
            spinnerSubStudy.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }

    private fun changeItemSubStudy() = with(binding) {
        spinnerSubStudy.setEntries(
            when (selectedStudy) {
                B_ENG -> R.array.list_sub_study_b_english_array
                ALGORITHM -> R.array.list_sub_study_algorithm_array
                else -> R.array.list_sub_study_kalkulus_array
            }
        )
    }

    companion object {
        const val B_ENG = "B Inggris"
        const val ALGORITHM = "Algoritma Dasar"
    }
}