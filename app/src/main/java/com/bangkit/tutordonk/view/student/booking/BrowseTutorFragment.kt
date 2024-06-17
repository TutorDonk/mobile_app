package com.bangkit.tutordonk.view.student.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.tutordonk.databinding.ItemlistBrowseTeacherBinding
import com.bangkit.tutordonk.view.model.ListTutor
import com.google.gson.Gson
import java.text.NumberFormat
import java.util.Locale

class BrowseTutorFragment(val position: Int) : Fragment() {
    private var _binding: ItemlistBrowseTeacherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemlistBrowseTeacherBinding.inflate(inflater, container, false)
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
        val item = Gson().fromJson(arguments?.getString(ARG_TUTOR_DATA), ListTutor::class.java)
        tvName.text = item.data[position].name
        tvPhone.text = item.data[position].phoneNumber
        tvAddress.text = item.data[position].address
        tvRate.text = item.data[position].rate.toInt().formatRupiah()
        tvCertificate.text = item.data[position].urlCertificate
    }


    private fun Int.formatRupiah(): String {
        val localeID = Locale("id", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        formatter.maximumFractionDigits = 0
        return formatter.format(this)
    }

    companion object {
        const val ARG_TUTOR_DATA = "ARG_TUTOR_DATA"
    }
}