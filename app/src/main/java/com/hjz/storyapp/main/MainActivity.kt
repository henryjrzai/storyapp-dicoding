package com.hjz.storyapp.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjz.storyapp.R
import com.hjz.storyapp.data.model.UserModelFactory
import com.hjz.storyapp.data.pref.UserLogin
import com.hjz.storyapp.databinding.ActivityMainBinding
import com.hjz.storyapp.home.HomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        UserModelFactory.getInstance(this)
    }

    private lateinit var adapter : ListStoryAdapter

    private lateinit var storyViewModel: StoryViewModel

    private lateinit var name : String
    private lateinit var token : String
    private lateinit var userId : String
    private lateinit var isLogin : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSession()

        binding.btnAddStory.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun getSession() {

        viewModel.getSession().observe(this) {user ->

            name = user.name
            token = user.token
            userId = user.userId
            isLogin = user.isLogin.toString()

            Log.d("MyToken", token)

            Log.d("token saya", "name : ${name}, token : ${token}, isLogin : ${isLogin}")

            getListStory(token)

            if (!user.isLogin){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun getListStory(token : String){
        storyViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[StoryViewModel::class.java]

        adapter = ListStoryAdapter()
        adapter.notifyDataSetChanged()

        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.adapter = adapter

        storyViewModel.getListStory(token)

        storyViewModel.listStory.observe(this){ story ->
            if (story.isNotEmpty()){
                adapter.setListStory(story)
            } else {
                Log.d("ListStory", "Empty Data")
            }
        }

        storyViewModel.isLoading.observe(this){
            showLoading(it)
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