package com.hjz.storyapp.addStories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hjz.storyapp.R
import com.hjz.storyapp.databinding.ActivityAddStoriesBinding

class AddStoriesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddStoriesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}