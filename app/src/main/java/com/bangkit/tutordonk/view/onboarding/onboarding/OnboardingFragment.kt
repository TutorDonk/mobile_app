package com.bangkit.tutordonk.view.onboarding.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.FragmentOnboardingBinding
import com.bangkit.tutordonk.utils.navigateWithAnimation

class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
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
        val position = requireArguments().getInt(ARG_POSITION)

        ivIndicatorActive.setImageResource(
            when (position) {
                0 -> R.drawable.ic_indicator_active_one
                1 -> R.drawable.ic_indicator_active_two
                else -> R.drawable.ic_indicator_active_three
            }
        )

        ivOnboarding.setImageResource(
            when (position) {
                0 -> R.drawable.img_certification_badges
                1 -> R.drawable.img_progress_tracking
                else -> R.drawable.img_course_catalog
            }
        )

        tvSkip.apply {
            setOnClickListener { findNavController().navigateWithAnimation(R.id.onboardingMainFragmentToLoginFragment) }
            visibility = if (position == 2) View.GONE else View.VISIBLE
        }
    }

    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

}