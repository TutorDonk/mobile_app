package com.bangkit.tutordonk.view.student.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.tutordonk.model.ListTutor
import com.bangkit.tutordonk.view.student.booking.BrowseTutorFragment
import com.bangkit.tutordonk.view.student.booking.BrowseTutorFragment.Companion.ARG_TUTOR_DATA
import com.google.gson.Gson

class TutorHomeAdapter(fragmentActivity: FragmentActivity, private val bundle: Bundle) :
    FragmentStateAdapter(fragmentActivity) {

    private val data =  Gson().fromJson(bundle.getString(ARG_TUTOR_DATA), ListTutor::class.java)

    override fun getItemCount(): Int = data.data.size

    override fun createFragment(position: Int): Fragment {
        val fragment = BrowseTutorFragment(position)
        fragment.arguments = bundle
        return fragment
    }
}