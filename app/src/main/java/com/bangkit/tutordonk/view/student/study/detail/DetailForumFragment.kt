package com.bangkit.tutordonk.view.student.study.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.tutordonk.databinding.FragmentDetailForumBinding
import com.bangkit.tutordonk.databinding.ItemlistCommentForumBinding
import com.bangkit.tutordonk.view.base.BaseRecyclerViewAdapter
import com.bangkit.tutordonk.view.base.GenericDiffCallback
import com.bangkit.tutordonk.view.component.customrecyclerview.model.ForumItem
import com.bangkit.tutordonk.view.component.customrecyclerview.model.User
import com.google.gson.Gson

class DetailForumFragment : Fragment() {
    private var _binding: FragmentDetailForumBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterComment: BaseRecyclerViewAdapter<User, ItemlistCommentForumBinding>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailForumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutListener()
        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLayoutListener() = with(binding) {
        val item = Gson().fromJson(arguments?.getString(ARG_FORUM_ITEM), ForumItem::class.java)

        tvCreator.text = item.user
        tvTitle.text = item.title
        tvDesc.text = item.subtitle
        tvLike.text = item.like.toString()
        tvComments.text = item.comment.toString()
    }

    private fun setupRecyclerView() {
        with(binding) {
            adapterComment = BaseRecyclerViewAdapter(
                itemClickListener = {/* no-op */ },
                inflateBinding = ItemlistCommentForumBinding::inflate,
                bind = { binding, item ->
                    with(binding) {
                        tvName.text = item.name
                        tvTime.text = item.time
                        tvComments.text = item.comment
                    }
                },
                diffCallback = GenericDiffCallback(
                    areTheItemsTheSame = { oldItem, newItem -> oldItem.name == newItem.name },
                    areTheContentsTheSame = { oldItem, newItem -> oldItem == newItem }
                )
            )

            rvComment.adapter = adapterComment

            val hardcodeItem = fetchData()
            adapterComment.submitList(hardcodeItem)
        }
    }

    private fun fetchData(): List<User> {
        return List(10) {
            User(
                name = "User $it",
                time = "${it * 10} Menit yang lalu",
                comment = "Ini komentar ke $it"
            )
        }
    }


    companion object {
        const val ARG_FORUM_ITEM = "FORUM_ITEM"
    }
}