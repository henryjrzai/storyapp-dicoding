package com.hjz.storyapp.data.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hjz.storyapp.addStories.AddStoriesViewModel
import com.hjz.storyapp.data.di.Injection
import com.hjz.storyapp.data.repository.UserRepository
import com.hjz.storyapp.login.LoginViewModel
import com.hjz.storyapp.main.DetailViewModel
import com.hjz.storyapp.main.MainViewModel
import com.hjz.storyapp.maps.MapsViewModel
import com.hjz.storyapp.signup.SignupViewModel

class UserModelFactory (private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddStoriesViewModel::class.java) -> {
                AddStoriesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): UserModelFactory {
            if (INSTANCE == null) {
                synchronized(UserModelFactory::class.java) {
                    INSTANCE = UserModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as UserModelFactory
        }
    }
}