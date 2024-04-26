package com.example.interviewproject.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interviewproject.adapter.PostAdapter
import com.example.interviewproject.api.response.Post
import com.example.interviewproject.databinding.ActivityMainBinding
import com.example.interviewproject.databinding.DialogPostDetailsBinding
import com.example.interviewproject.ui.viewModel.PostViewModel
import com.example.interviewproject.utils.PaginationScrollListener
import com.example.interviewproject.utils.checkInternetConnection


class MainActivity : AppCompatActivity() {

    private val b by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[PostViewModel::class.java] }

    private val list: ArrayList<Post> = ArrayList()
    private lateinit var adapter: PostAdapter
    var isLastPage = false
    var isLoading = false
    var pageNo = 1
    private val itemsPage = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

        observe()
        if (checkInternetConnection(this))
            viewModel.fetchPosts(pageNo, itemsPage)
        else
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observe() {
        viewModel.postsLiveData.observe(this) {
            isLastPage = it.size < 10
            isLoading = false
            viewModel.loadingLiveData.postValue(false)
            adapter.addList(it)
        }

        viewModel.message.observe(this) {
            viewModel.loadingLiveData.postValue(false)
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.loadingLiveData.observe(this) {
            if (it)
                b.bottomProgress.visibility = View.VISIBLE
            else
                b.bottomProgress.visibility = View.GONE
        }

    }

    private fun init() {
        adapter = PostAdapter(list, object : PostAdapter.OnItemClickListener {
            override fun onClick(post: Post, position: Int) { detailsDialog(post) }
        })

        b.rvPost.adapter = adapter
        val manager = LinearLayoutManager(this)
        b.rvPost.layoutManager = manager

        b.rvPost.addOnScrollListener(object : PaginationScrollListener(manager) {
            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading

            override fun loadMoreItems() {
                isLoading = true
                ++pageNo
                if (checkInternetConnection(this@MainActivity))
                    viewModel.fetchPosts(pageNo, itemsPage)
                else
                    Toast.makeText(this@MainActivity, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }

        })

    }

    @SuppressLint("SetTextI18n")
    private fun detailsDialog(post: Post) {
        Dialog(this).apply {
            val dBinding = DialogPostDetailsBinding.inflate(layoutInflater, null, false)
            setContentView(dBinding.root)

            window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            dBinding.apply {
                tvId.text = "${post.id}."
                tvTitle.text = post.title
                tvBody.text = post.body
                ivClose.setOnClickListener { dismiss() }
            }

        }.show()
    }

}