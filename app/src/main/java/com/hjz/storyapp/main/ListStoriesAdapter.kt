package com.hjz.storyapp.main

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hjz.storyapp.data.response.ListStoryItem
import com.hjz.storyapp.databinding.ListStoryBinding
import com.hjz.storyapp.utils.withDateFormat

class ListStoriesAdapter : PagingDataAdapter<ListStoryItem, ListStoriesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val stories = getItem(position)
        if (stories != null) {
            holder.bind(stories)
        }
    }

    class MyViewHolder (private val binding: ListStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stories : ListStoryItem){
            Log.d("AdapterStories", stories.toString())
            binding.apply {
                tvItemName.text = stories.name
                tvTime.text = stories.createdAt.withDateFormat()
                tvDescription.text = stories.description
                Glide.with(itemView)
                    .load(stories.photoUrl)
                    .into(ivItemPhoto)
                cvItemStories.setOnClickListener {
                    val intent = Intent(it.context, DetailStory::class.java)
                    intent.putExtra(DetailStory.EXTRA_ID, stories.id)
                    itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldStory: ListStoryItem, newStory: ListStoryItem): Boolean {
                return oldStory == newStory
            }

            override fun areContentsTheSame(oldStory: ListStoryItem, newStory: ListStoryItem): Boolean {
                return oldStory.name == newStory.name && oldStory.description == newStory.description && oldStory.photoUrl == newStory.photoUrl
            }
        }
    }
}