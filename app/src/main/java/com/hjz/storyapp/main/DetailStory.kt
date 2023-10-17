package com.hjz.storyapp.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.hjz.storyapp.data.model.UserModelFactory
import com.hjz.storyapp.databinding.ActivityDetailStoryBinding
import com.hjz.storyapp.home.HomeActivity

class DetailStory : AppCompatActivity() {

    private lateinit var binding : ActivityDetailStoryBinding

    private val viewModel by viewModels<DetailViewModel> {
        UserModelFactory.getInstance(this)
    }

    companion object {
        const val EXTRA_ID = "extra_userid"
    }

    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getSession()
    }

    private fun getSession(){

        viewModel.getSession().observe(this){ user ->
            val storyId = intent.getStringExtra(EXTRA_ID)
            token = user.token
            if (storyId != null) {
                getDetailStories(token, storyId)
            }
            if (!user.isLogin){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun getDetailStories(token : String, storyId : String) {
        viewModel.setDetailStories(token, storyId)
        viewModel.isLoading.observe(this){
            showLoading(it)
        }
        viewModel.detailStories.observe(this){stories ->
            binding.apply {
                tvProfileName.text = stories.name
                tvTime.text = stories.createdAt
                tvDescription.text = stories.description
                Glide.with(this@DetailStory)
                    .load(stories.photoUrl)
                    .into(imgDetailStory)
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}