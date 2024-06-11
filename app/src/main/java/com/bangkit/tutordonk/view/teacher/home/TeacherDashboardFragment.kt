package com.bangkit.tutordonk.view.teacher.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.FragmentTeacherDashboardBinding
import com.bangkit.tutordonk.view.component.forumrecyclerview.model.ForumItem
import com.bangkit.tutordonk.view.navigateWithAnimation

class TeacherDashboardFragment : Fragment() {
    private var _binding: FragmentTeacherDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setLayoutListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLayoutListener() = with(binding) {
        rvForumHottest.setItemsPerPage(10)
        rvForumHottest.setMaxPage(3)
        rvForumHottest.setInitialItems(listOf(ForumItem(0, "User 1", "Initial Title", "Initial Subtitle", 1, 1, 1)))

        cvTutorManagement.setOnClickListener { navController.navigateWithAnimation(R.id.teacherTutorManagement) }
        cvStudyHistory.setOnClickListener { navController.navigateWithAnimation(R.id.teacherHistoryFragment) }
    }
}