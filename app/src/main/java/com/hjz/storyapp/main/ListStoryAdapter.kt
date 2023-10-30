package com.hjz.storyapp.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hjz.storyapp.R
import com.hjz.storyapp.data.response.ListStoryItem
import com.hjz.storyapp.databinding.ListStoryBinding
import com.hjz.storyapp.utils.withDateFormat
import androidx.core.util.Pair
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

class ListStoryAdapter : PagingDataAdapter<ListStoryItem, ListStoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    inner class StoryViewHolder(private val binding : ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story : ListStoryItem){
            with(binding){
                tvItemName.text = story.name
                tvTime.text = story.createdAt.withDateFormat()
                tvDescription.text = story.description
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .into(ivItemPhoto)
                cvItemStories.setOnClickListener {
                    val intent = Intent(it.context, DetailStory::class.java)
                    intent.putExtra(DetailStory.EXTRA_ID, story.id)
                    itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}