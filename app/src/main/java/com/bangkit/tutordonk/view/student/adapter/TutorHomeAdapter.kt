package com.bangkit.tutordonk.view.student.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.tutordonk.view.student.booking.BrowseTutorFragment

class TutorHomeAdapter(fragmentActivity: FragmentActivity, private val bundle: Bundle) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        val fragment = BrowseTutorFragment()
        fragment.arguments = bundle
        return fragment
    }
}