package com.hjz.storyapp.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hjz.storyapp.R
import com.hjz.storyapp.data.response.ListStoryItem
import com.hjz.storyapp.databinding.ListStoryBinding

class ListStoryAdapter : RecyclerView.Adapter<ListStoryAdapter.StoryViewHolder>() {

    private val listStory = ArrayList<ListStoryItem>()

    fun setListStory( story : List<ListStoryItem>) {
        listStory.clear()
        listStory.addAll(story)
        notifyDataSetChanged()
    }

    inner class StoryViewHolder(val binding : ListStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story : ListStoryItem){
            with(binding){
                tvProfileName.text = story.name
                tvTime.text = story.createdAt
                tvDescription.text = story.description
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .into(storyImage)
                cvItemStories.setOnClickListener {
                    val intent = Intent(it.context, DetailStory::class.java)
                    intent.putExtra(DetailStory.EXTRA_ID, story.id)
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun getItemCount(): Int = listStory.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(listStory[position])
    }
}