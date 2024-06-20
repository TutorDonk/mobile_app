package com.bangkit.tutordonk.view.forum.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.component.base.BaseCustomDialog
import com.bangkit.tutordonk.component.base.BaseRecyclerViewAdapter
import com.bangkit.tutordonk.component.base.GenericDiffCallback
import com.bangkit.tutordonk.databinding.FragmentForumDetailBinding
import com.bangkit.tutordonk.databinding.ItemlistCommentForumBinding
import com.bangkit.tutordonk.databinding.PopupCreateCommentBinding
import com.bangkit.tutordonk.model.Comment
import com.bangkit.tutordonk.model.ListForumItem
import com.bangkit.tutordonk.network.ApiServiceProvider
import com.bangkit.tutordonk.utils.toFormattedDateString
import com.bangkit.tutordonk.view.forum.ForumActivity
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class ForumDetailFragment : Fragment() {
    private var _binding: FragmentForumDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterComment: BaseRecyclerViewAdapter<Comment, ItemlistCommentForumBinding>
    private lateinit var dialog: BaseCustomDialog<PopupCreateCommentBinding>
    private lateinit var navController: NavController

    private val apiServiceProvider: ApiServiceProvider by inject()
    private val item: ListForumItem by lazy {
        Gson().fromJson(arguments?.getString(ForumActivity.INTENT_FORUM_ITEM), ListForumItem::class.java)
    }

    private var comment: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupUI()
        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() = with(binding) {
        tvCreator.text = item.nama
        tvTitle.text = item.title
        tvDesc.text = item.content
        tvLike.text = item.likes.toString()
        tvComments.text = item.comments.size.toString()

        tvLike.setOnClickListener { sendLike() }
        tvComments.setOnClickListener { showPopUpDialog() }
    }

    private fun setupRecyclerView() {
        with(binding) {
            adapterComment = BaseRecyclerViewAdapter(
                itemClickListener = { /* no-op */ },
                inflateBinding = ItemlistCommentForumBinding::inflate,
                bind = { binding, item ->
                    with(binding) {
                        tvName.text = item.nama
                        tvTime.text = item.createdAt._seconds.toFormattedDateString()
                        tvComments.text = item.content
                    }
                },
                diffCallback = GenericDiffCallback(
                    areTheItemsTheSame = { oldItem, newItem -> oldItem.nama == newItem.nama },
                    areTheContentsTheSame = { oldItem, newItem -> oldItem == newItem }
                )
            )
            rvComment.adapter = adapterComment
            cvComment.visibility = if (item.comments.isNotEmpty()) View.VISIBLE else View.GONE
            adapterComment.submitList(item.comments)
        }
    }

    private fun showPopUpDialog() {
        dialog = BaseCustomDialog(
            context = requireContext(),
            bindingInflater = PopupCreateCommentBinding::inflate,
            bind = { binding ->
                with(binding) {
                    tietComments.doOnTextChanged { text, _, _, _ ->
                        comment = text.toString()
                    }
                    ivClose.setOnClickListener { dialog.cancel() }
                    btnSend.setOnClickListener { sendComment() }
                }
            })
        dialog.show()
    }

    private fun sendLike() {
        val callback = apiServiceProvider.createCallback<Unit>(
            onSuccess = {
                if (navController.currentBackStack.value.isNotEmpty()) navController.popBackStack()
                else requireActivity().finish()
            }
        )

        apiServiceProvider.apiService.sendLikes(item.id, mapOf("isLike" to false)).enqueue(callback)
    }

    private fun sendComment() {
        val callback = apiServiceProvider.createCallback<Unit>(
            onSuccess = {
                dialog.cancel()
                navController.popBackStack()
            }
        )

        apiServiceProvider.apiService.sendComment(item.id, mapOf("content" to comment)).enqueue(callback)
    }
}