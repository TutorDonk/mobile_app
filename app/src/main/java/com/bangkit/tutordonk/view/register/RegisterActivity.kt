package com.bangkit.tutordonk.view.register

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.tutordonk.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var studentFields: View
    private lateinit var teacherFields: View
    private lateinit var roleSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize fields
        studentFields = findViewById(R.id.student_fields)
        teacherFields = findViewById(R.id.teacher_fields)
        roleSpinner = findViewById(R.id.spinner_role)

        // Set up the Spinner
        val roles = resources.getStringArray(R.array.roles_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedRole = parent.getItemAtPosition(position).toString()
                switchRoleFields(selectedRole)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun switchRoleFields(role: String) {
        val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in)
        val slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out)

        when (role) {
            "Siswa" -> {
                teacherFields.startAnimation(slideOut)
                teacherFields.visibility = View.GONE
                studentFields.visibility = View.VISIBLE
                studentFields.startAnimation(slideIn)
            }
            "Pengajar" -> {
                studentFields.startAnimation(slideOut)
                studentFields.visibility = View.GONE
                teacherFields.visibility = View.VISIBLE
                teacherFields.startAnimation(slideIn)
            }
        }
    }

    private fun submitData() {
        val address = findViewById<EditText>(R.id.et_address).text.toString()
        val phoneNumber = findViewById<EditText>(R.id.et_phone_number).text.toString()
        val role = roleSpinner.selectedItem.toString()

        if (role == "Siswa") {
            val parentPhoneNumber = findViewById<EditText>(R.id.et_parent_phone_number).text.toString()
            val parentName = findViewById<EditText>(R.id.et_parent_name).text.toString()
            val educationLevel = findViewById<EditText>(R.id.et_education_level).text.toString()
            val classLevel = findViewById<EditText>(R.id.et_class).text.toString()

            // Handle student data submission
            // Submit student data to backend or Firebase

        } else if (role == "Pengajar") {
            val subjects = findViewById<EditText>(R.id.et_subjects).text.toString()
            val certifications = findViewById<EditText>(R.id.et_certifications).text.toString()

            // Handle teacher data submission
            // Submit teacher data to backend or Firebase
        }
    }
}
