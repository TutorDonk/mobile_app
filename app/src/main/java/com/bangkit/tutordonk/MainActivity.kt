package com.bangkit.tutordonk

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }
}

