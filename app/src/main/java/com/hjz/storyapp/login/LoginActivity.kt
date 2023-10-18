package com.hjz.storyapp.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.hjz.storyapp.R
import com.hjz.storyapp.data.model.UserModelFactory
import com.hjz.storyapp.data.pref.UserLogin
import com.hjz.storyapp.databinding.ActivityLoginBinding
import com.hjz.storyapp.main.MainActivity
import com.hjz.storyapp.signup.SignupActivity
import com.hjz.storyapp.signup.SignupViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        UserModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButton()
        playAnimation()
        binding.loginBtn.setOnClickListener {
            setLogin()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView2, View.TRANSLATION_Y, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginBtn, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(login)
        }
        AnimatorSet().apply {
            playSequentially(title, email, password, together)
            start()
        }
    }

    private fun setupButton() {
        val emailEditText = binding.edLoginEmail
        val passEditText = binding.edLoginPassword
        val loginBtn = binding.loginBtn

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isEmailNotEmpty = emailEditText.text.toString().isNotEmpty()
                val isPasswordNotEmpty = passEditText.text.toString().isNotEmpty()
                loginBtn.isEnabled = isEmailNotEmpty && isPasswordNotEmpty
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        emailEditText.addTextChangedListener(textWatcher)
        passEditText.addTextChangedListener(textWatcher)
    }

    private fun setLogin() {
        binding.apply {
            lifecycleScope.launch {
                viewModel.setLogin(
                    edLoginEmail.text.toString(),
                    edLoginPassword.text.toString()
                )
            }
        }

        viewModel.loginRespon.observe(this) { response ->
            Log.d("ResponseLogin", "${response.loginResult.name}, ${response.loginResult.userId}, ${response.loginResult.token}")
            if (response != null) {
                val userLogin = UserLogin(
                    "${response.loginResult.name}",
                    "${response.loginResult.token}",
                    "${response.loginResult.userId}",
                    true
                )

                saveSession(userLogin)
                moveToMainActivity()
            }
        }

        feedbackLogin()
    }

    private fun feedbackLogin() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        viewModel.errorMessage.observe(this) { message ->
            if (!message.isNullOrEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_SHORT
                ).show()
                viewModel.clearErrorMessage() // Hapus pesan kesalahan setelah menampilkannya
            }
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun saveSession(user: UserLogin) {
        viewModel.saveSession(user)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
