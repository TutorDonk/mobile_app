package com.bangkit.tutordonk.view.teacher.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.tutordonk.databinding.FragmentTeacherHistoryStudyBinding
import com.bangkit.tutordonk.model.ListBookingItem
import com.bangkit.tutordonk.network.ApiServiceProvider
import org.koin.android.ext.android.inject

class TeacherHistoryStudyFragment : Fragment() {
    private var _binding: FragmentTeacherHistoryStudyBinding? = null
    private val binding get() = _binding!!

    private val apiServiceProvider: ApiServiceProvider by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherHistoryStudyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val callback = apiServiceProvider.createCallback<List<ListBookingItem>>(
            onSuccess = {
                with(binding) {
                    rvHistory.setInitialItems(it)
                }
            }
        )

        apiServiceProvider.apiService.bookingHistory().enqueue(callback)
    }
}