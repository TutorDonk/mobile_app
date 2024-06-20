package com.bangkit.tutordonk.view.student.booking

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.tutordonk.databinding.ItemlistBrowseTeacherBinding
import com.bangkit.tutordonk.model.CertificationData
import com.bangkit.tutordonk.model.TeacherListItem
import com.bangkit.tutordonk.network.ApiServiceProvider
import com.bangkit.tutordonk.view.student.StudentHomeActivity
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import java.text.NumberFormat
import java.util.Locale

class BrowseTutorFragment : Fragment() {
    private var _binding: ItemlistBrowseTeacherBinding? = null
    private val binding get() = _binding!!

    private val apiServiceProvider: ApiServiceProvider by inject()

    private val item by lazy {
        Gson().fromJson(arguments?.getString(ARG_TUTOR_DATA), TeacherListItem::class.java)
    }

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
        tvName.text = item.nama
        tvPhone.text = item.phoneNumber
        tvAddress.text = item.domicile
        tvRate.text = item.feePerHour.formatRupiah()
        tvCertificate.text = item.certifications.toFormattedString()

        btnHire.setOnClickListener { hireTutor() }
    }

    private fun hireTutor() {
        val reqBody = mapOf(
            "idTutor" to item.id,
            "namaTutor" to item.nama,
            "jamTutor" to item.selectedDateTime,
            "course" to item.selectedSubjects,
        )
        val callback = apiServiceProvider.createCallback<Unit>(
            onSuccess = {
                startActivity(Intent(requireActivity(), StudentHomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }
        )

        apiServiceProvider.apiService.bookingTutor(reqBody).enqueue(callback)
    }

    private fun Int.formatRupiah(): String {
        val localeID = Locale("id", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        formatter.maximumFractionDigits = 0
        return formatter.format(this)
    }

    private fun List<CertificationData>.toFormattedString(): String {
        val stringBuilder = StringBuilder()
        forEachIndexed { index, certification ->
            stringBuilder.append("$index.")
            stringBuilder.append(certification.url)
            stringBuilder.append(" | ${certification.name}")
        }
        return stringBuilder.toString().trim()
    }

    companion object {
        const val ARG_TUTOR_DATA = "ARG_TUTOR_DATA"
    }
}