package com.bangkit.tutordonk.view.onboarding.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.FragmentOnboardingMainBinding
import com.bangkit.tutordonk.view.navigateWithAnimation

class OnboardingMainFragment : Fragment() {
    private var _binding: FragmentOnboardingMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingMainBinding.inflate(inflater, container, false)
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
        val onboardingAdapter = OnboardingPagerAdapter(requireActivity())
        vpMain.adapter = onboardingAdapter

        btnNext.setOnClickListener {
            if (vpMain.currentItem < 2) vpMain.currentItem += 1
            else navController.navigateWithAnimation(R.id.onboardingMainFragmentToLoginFragment)
        }
    }

    private inner class OnboardingPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment = OnboardingFragment.newInstance(position)
    }
}