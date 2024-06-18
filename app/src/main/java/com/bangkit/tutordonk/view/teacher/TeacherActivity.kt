package com.bangkit.tutordonk.view.teacher

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.ActivityTeacherBinding
import com.bangkit.tutordonk.model.ShareViewModel
import com.bangkit.tutordonk.utils.navigateWithAnimation

class TeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherBinding

    private lateinit var navController: NavController

    val data: ShareViewModel by viewModels()
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
            data.name.observe(this@TeacherActivity) {
                tvGreeting.text = getString(R.string.dashboard_greeting, it)
            }

            tvGreeting.setOnClickListener {
                if (navController.currentDestination?.id != R.id.teacherEditProfileFragment) {
                    navController.navigateWithAnimation(R.id.teacherEditProfileFragment)
                }
            }
        }
    }
}