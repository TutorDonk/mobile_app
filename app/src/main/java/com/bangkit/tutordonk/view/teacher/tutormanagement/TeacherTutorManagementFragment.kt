package com.bangkit.tutordonk.view.teacher.tutormanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.component.base.BaseRecyclerViewAdapter
import com.bangkit.tutordonk.component.base.GenericDiffCallback
import com.bangkit.tutordonk.databinding.FragmentTeacherTutorManagementBinding
import com.bangkit.tutordonk.databinding.ItemlistTutorManagementBinding
import com.bangkit.tutordonk.model.ListBookingItem
import com.bangkit.tutordonk.network.ApiServiceProvider
import com.bangkit.tutordonk.utils.navigateWithAnimation
import com.bangkit.tutordonk.utils.toFormattedDateString
import org.koin.android.ext.android.inject

class TeacherTutorManagementFragment : Fragment() {
    private var _binding: FragmentTeacherTutorManagementBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var adapterTutorManagement: BaseRecyclerViewAdapter<ListBookingItem, ItemlistTutorManagementBinding>

    private val apiServiceProvider: ApiServiceProvider by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherTutorManagementBinding.inflate(inflater, container, false)
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

    override fun onResume() {
        super.onResume()
        val callback = apiServiceProvider.createCallback<List<ListBookingItem>>(
            onSuccess = {
                adapterTutorManagement.submitList(it)
            }
        )

        apiServiceProvider.apiService.bookingHistory().enqueue(callback)
    }

    private fun setLayoutListener() = with(binding) {
        adapterTutorManagement = BaseRecyclerViewAdapter(
            itemClickListener = {},
            inflateBinding = ItemlistTutorManagementBinding::inflate,
            bind = { binding, item ->
                with(binding) {
                    tvName.text = item.namaSiswa
                    tvTime.text = item.createdAt._seconds.toFormattedDateString("dd-mm-yyyy")

                    btnAccept.setOnClickListener { navController.navigateWithAnimation(R.id.teacherHistoryFragment) }
                }
            },
            diffCallback = GenericDiffCallback(
                areTheItemsTheSame = { oldItem, newItem -> oldItem.namaSiswa == newItem.namaSiswa },
                areTheContentsTheSame = { oldItem, newItem -> oldItem == newItem }
            )
        )
        rvTutorManagement.adapter = adapterTutorManagement
    }
}