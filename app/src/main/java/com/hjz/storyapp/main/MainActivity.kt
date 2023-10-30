package com.hjz.storyapp.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjz.storyapp.maps.MapsActivity
import com.hjz.storyapp.R
import com.hjz.storyapp.addStories.AddStoriesActivity
import com.hjz.storyapp.data.model.UserModelFactory
import com.hjz.storyapp.databinding.ActivityMainBinding
import com.hjz.storyapp.home.HomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        UserModelFactory.getInstance(this)
    }
    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSession()
        setupAction()
    }

    private fun setupAction(){
        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoriesActivity::class.java)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_maps -> {
                    startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                    true
                }
                R.id.menu_settings -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.menu_logout -> {
                    viewModel.logout()
                    true
                }
                else -> false
            }
        }
    }

    private fun getSession() {

        viewModel.getSession().observe(this) {user ->

            token = user.token

            if (!user.isLogin){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                getData()
            }
        }
    }


    private fun getData() {
        val adapter = ListStoriesAdapter()
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.stories.observe(this) {
            Log.d("ListStoryPaging", it.toString())
            adapter.submitData(lifecycle, it)
        }
    }
}