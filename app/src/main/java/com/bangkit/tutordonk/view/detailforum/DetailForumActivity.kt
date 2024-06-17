package com.bangkit.tutordonk.view.detailforum

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.tutordonk.databinding.ActivityDetailForumBinding
import com.bangkit.tutordonk.databinding.ItemlistCommentForumBinding
import com.bangkit.tutordonk.databinding.PopupCreateCommentBinding
import com.bangkit.tutordonk.view.base.BaseCustomDialog
import com.bangkit.tutordonk.view.base.BaseRecyclerViewAdapter
import com.bangkit.tutordonk.view.base.GenericDiffCallback
import com.bangkit.tutordonk.view.component.forumrecyclerview.model.ForumItem
import com.bangkit.tutordonk.view.component.forumrecyclerview.model.User
import com.google.gson.Gson

class DetailForumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailForumBinding
    private lateinit var adapterComment: BaseRecyclerViewAdapter<User, ItemlistCommentForumBinding>
    private lateinit var dialog: BaseCustomDialog<PopupCreateCommentBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailForumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        setupRecyclerView()
    }

    private fun setupUI() = with(binding) {
        val item = Gson().fromJson(intent?.getStringExtra(INTENT_FORUM_ITEM), ForumItem::class.java)

        tvCreator.text = item.user
        tvTitle.text = item.title
        tvDesc.text = item.subtitle
        tvLike.text = item.like.toString()
        tvComments.text = item.comment.toString()
    }

    private fun setupRecyclerView() {
        with(binding) {
            adapterComment = BaseRecyclerViewAdapter(
                itemClickListener = { /* no-op */ },
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

            tvComments.setOnClickListener { showPopUpDialog() }
            rvComment.adapter = adapterComment

            val hardcodeItem = fetchData()
            adapterComment.submitList(hardcodeItem)
        }
    }

    private fun showPopUpDialog() {
        dialog = BaseCustomDialog(
            context = this,
            bindingInflater = PopupCreateCommentBinding::inflate,
            bind = { binding ->
                with(binding) {
                    ivClose.setOnClickListener { dialog.cancel() }
                    btnSend.setOnClickListener { dialog.cancel() }
                }
            })
        dialog.show()
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
        const val INTENT_FORUM_ITEM = "FORUM_ITEM"
    }
}