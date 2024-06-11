package com.bangkit.tutordonk.view.teacher.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.tutordonk.databinding.FragmentTeacherHistoryStudyBinding
import com.bangkit.tutordonk.view.component.historyrecyclerview.model.HistoryItem

class TeacherHistoryStudyFragment : Fragment() {
    private var _binding: FragmentTeacherHistoryStudyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherHistoryStudyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        with(binding) {
            rvHistory.setMaxPage(5)
            rvHistory.setInitialItems(listOf(HistoryItem(0, "SUCCESS", "Kalkulus", "Prof Hamka", "24-10-2024")))
        }
    }
}