package com.example.interviewproject.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewproject.api.response.Post
import com.example.interviewproject.databinding.RvPostBinding

class PostAdapter(private val list: ArrayList<Post>, val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(RvPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    inner class ViewHolder(private val b: RvPostBinding) : RecyclerView.ViewHolder(b.root) {
        @SuppressLint("SetTextI18n")
        fun bindData(post: Post) {
            b.tvId.text = "${post.id}."
            b.tvTitle.text = post.title

            b.clPostItem.setOnClickListener {
                onItemClickListener.onClick(post, adapterPosition)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(list: ArrayList<Post>) {
        list.forEach {
            if(!this.list.contains(it))
                this.list.add(it)
        }
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(post: Post, position: Int)
    }
}