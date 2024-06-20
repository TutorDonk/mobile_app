package com.bangkit.tutordonk.view.forum.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.component.base.BaseCustomDialog
import com.bangkit.tutordonk.databinding.FragmentForumListBinding
import com.bangkit.tutordonk.databinding.PopupCreateForumBinding
import com.bangkit.tutordonk.model.ListForumItem
import com.bangkit.tutordonk.network.ApiServiceProvider
import com.bangkit.tutordonk.utils.navigateWithAnimation
import com.bangkit.tutordonk.view.forum.ForumActivity
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class ForumListFragment : Fragment() {

    private var _binding: FragmentForumListBinding? = null
    private val binding get() = _binding!!
    private var isSortClicked = false
    private var spinnerInteracted = false

    private lateinit var dialog: BaseCustomDialog<PopupCreateForumBinding>
    private lateinit var navController: NavController
    private val apiServiceProvider: ApiServiceProvider by inject()

    private var content = ""
    private var title = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForumListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val callback = apiServiceProvider.createCallback<List<ListForumItem>>(
            onSuccess = { response ->
                with(binding) {
                    rvForum.setMaxPage(5)
                    rvForum.setInitialItems(response)
                    rvForum.setOnItemClickListener { forumItem ->
                        val bundle = bundleOf(
                            ForumActivity.INTENT_FORUM_ITEM to Gson().toJson(forumItem)
                        )
                        navController.navigateWithAnimation(R.id.forumDetailFragment, args = bundle)
                    }
                }
            }
        )

        apiServiceProvider.apiService.listForum().enqueue(callback)
    }

    private fun showPopUpDialog() {
        dialog = BaseCustomDialog(
            context = requireContext(),
            bindingInflater = PopupCreateForumBinding::inflate,
            bind = { binding ->
                with(binding) {
                    tietTitle.doOnTextChanged { text, _, _, _ -> title = text.toString() }
                    tietContent.doOnTextChanged { text, _, _, _ -> content = text.toString() }
                    ivClose.setOnClickListener { dialog.cancel() }
                    btnSend.setOnClickListener { createForum() }
                }
            })
        dialog.show()
    }

    private fun createForum() {
        val callback = apiServiceProvider.createCallback<Unit>(
            onSuccess = { dialog.cancel() }
        )

        apiServiceProvider.apiService.createForum(
            mapOf(
                "title" to title,
                "content" to content
            )
        ).enqueue(callback)
    }

    private fun setupUI() {
        setupSortIcon()
        setupSortSpinner()
        binding.fabCreateForum.setOnClickListener { showPopUpDialog() }
    }

    private fun setupSortIcon() = with(binding) {
        ivSort.setOnClickListener {
            toggleSortOptions()
        }
    }

    private fun toggleSortOptions() = with(binding) {
        isSortClicked = !isSortClicked
        spinnerSort.visibility = if (isSortClicked) View.VISIBLE else View.GONE
        ivSort.rotation = if (isSortClicked) 180f else 0f
    }

    private fun setupSortSpinner() = with(binding) {
        spinnerSort.setOnItemSelectedListener {
            if (!spinnerInteracted) {
                spinnerInteracted = true
                return@setOnItemSelectedListener
            }
            if (isSortClicked) {
                applySort(it)
            }
        }
    }

    private fun applySort(selectedSort: String) = with(binding) {
        toggleSortOptions()
        val sortedItems = rvForum.getAllItems().sortedByDescending { item ->
            when (selectedSort.lowercase()) {
                "popularitas" -> item.likes + item.comments.size
                "like" -> item.likes
                else -> item.comments.size
            }
        }
        rvForum.setInitialItems(sortedItems)
    }
}