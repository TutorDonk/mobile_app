package com.bangkit.tutordonk.view.student.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.tutordonk.databinding.FragmentTutorHomeBinding
import com.bangkit.tutordonk.view.student.adapter.TutorHomeAdapter

class TutorHomeFragment : Fragment() {
    private var _binding: FragmentTutorHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTutorHomeBinding.inflate(inflater, container, false)
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
        val bundle = Bundle().apply {
            putString(BrowseTutorFragment.ARG_TUTOR_DATA, arguments?.getString(BrowseTutorFragment.ARG_TUTOR_DATA))
        }

        val viewPagerAdapter = TutorHomeAdapter(requireActivity(), bundle)
        vpTutor.adapter = viewPagerAdapter
    }
}