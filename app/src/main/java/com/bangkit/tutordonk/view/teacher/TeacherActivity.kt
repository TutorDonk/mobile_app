package com.bangkit.tutordonk.view.teacher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.ActivityTeacherBinding
import com.bangkit.tutordonk.view.navigateWithAnimation
import com.bangkit.tutordonk.view.student.StudentHomeActivity

class TeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherBinding

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fcv_home_teacher) as NavHostFragment
        navController = navHostFragment.navController

        setupUI()
    }

    private fun setupUI() {
        with(binding) {
            tvGreeting.text = getString(R.string.dashboard_greeting, intent.getStringExtra(StudentHomeActivity.NAME))
            tvGreeting.setOnClickListener {
                if (navController.currentDestination?.id != R.id.teacherEditProfileFragment) {
                    navController.navigateWithAnimation(R.id.teacherEditProfileFragment)
                }
            }
        }
    }
}