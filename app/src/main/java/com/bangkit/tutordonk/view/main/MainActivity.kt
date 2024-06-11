package com.bangkit.tutordonk.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.view.home.StudentHomeActivity
import com.bangkit.tutordonk.view.register.RegisterActivity
import com.bangkit.tutordonk.view.teacher.TeacherActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupComponent()
        setupView()
    }

    private fun setupComponent() {
        val slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        val logo = findViewById<ImageView>(R.id.logo)
        val welcomeText = findViewById<TextView>(R.id.welcome_text)
        val googleSignInButton = findViewById<MaterialButton>(R.id.google_sign_in_button)
        val registerButton = findViewById<MaterialButton>(R.id.register_button)

        logo.startAnimation(fadeInAnimation)
        welcomeText.startAnimation(fadeInAnimation)
        googleSignInButton.startAnimation(slideInAnimation)
        registerButton.startAnimation(slideInAnimation)

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        googleSignInButton.setOnClickListener {
            val intent = Intent(this, StudentHomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}
