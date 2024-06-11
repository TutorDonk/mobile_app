package com.bangkit.tutordonk.view.teacher.tutormanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.FragmentTeacherTutorManagementBinding
import com.bangkit.tutordonk.databinding.ItemlistTutorManagementBinding
import com.bangkit.tutordonk.view.base.BaseRecyclerViewAdapter
import com.bangkit.tutordonk.view.base.GenericDiffCallback
import com.bangkit.tutordonk.view.navigateWithAnimation
import com.bangkit.tutordonk.view.teacher.tutormanagement.model.TutorManagementItem

class TeacherTutorManagementFragment : Fragment() {
    private var _binding: FragmentTeacherTutorManagementBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var adapterTutorManagement: BaseRecyclerViewAdapter<TutorManagementItem, ItemlistTutorManagementBinding>

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

    private fun setLayoutListener() = with(binding) {
        adapterTutorManagement = BaseRecyclerViewAdapter(
            itemClickListener = {},
            inflateBinding = ItemlistTutorManagementBinding::inflate,
            bind = { binding, item ->
                with(binding) {
                    tvName.text = item.name
                    tvTime.text = item.time

                    btnAccept.setOnClickListener { navController.navigateWithAnimation(R.id.teacherHistoryFragment) }
                }
            },
            diffCallback = GenericDiffCallback(
                areTheItemsTheSame = { oldItem, newItem -> oldItem.name == newItem.name },
                areTheContentsTheSame = { oldItem, newItem -> oldItem == newItem }
            )
        )
        rvTutorManagement.adapter = adapterTutorManagement
        adapterTutorManagement.submitList(hardCodeData())
    }

    private fun hardCodeData(): List<TutorManagementItem> {
        return List(5) {
            TutorManagementItem(
                name = "Nama $it",
                time = "$it-06-2024",
                status = "SUCCESS",
            )
        }
    }
}