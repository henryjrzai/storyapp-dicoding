package com.hjz.storyapp.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.hjz.storyapp.data.model.UserModelFactory
import com.hjz.storyapp.databinding.ActivitySignupBinding
import com.hjz.storyapp.login.LoginActivity
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignupBinding

    private val viewModel by viewModels<SignupViewModel> {
        UserModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButton()
        playAnimation()

        //val viewModel = ViewModelProvider(this)[SignupViewModel::class.java]
        viewModel.successMessage.observe(this) { message ->
            if (!message.isNullOrEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_SHORT
                ).show()
                viewModel.clearSuccessMessage() // Hapus pesan sukses setelah menampilkannya

                // berpindah ke halaman Login Activity
                Handler(Looper.getMainLooper()).postDelayed({
                    // Pindah halaman di sini
                    val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                    startActivity(intent)
                }, 2000)
            }
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

        binding.signupBtn.setOnClickListener {
            binding.apply {
                lifecycleScope.launch {
                    viewModel.register(
                        edRegisterName.text.toString(),
                        edRegisterEmail.text.toString(),
                        edRegisterPassword.text.toString())
                }
                Log.d("namahenryset", "nama = ${edRegisterName.text.toString()}, email = ${edRegisterEmail.text.toString()}, password ${edRegisterPassword.text.toString()}")
            }
            viewModel.isLoading.observe(this) { isLoading ->
                showLoading(isLoading)
            }
        }
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_Y, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 1f).setDuration(100)
        val sigUp = ObjectAnimator.ofFloat(binding.signupBtn, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(sigUp)
        }
        AnimatorSet().apply {
            playSequentially(title,name, email, password, together)
            start()
        }
    }

    private fun setupButton() {
        val nameEditText = binding.edRegisterName
        val emailEditText = binding.edRegisterEmail
        val passEditText = binding.edRegisterPassword
        val signupBtn = binding.signupBtn
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isNameNotEmpty = nameEditText.text.toString().isNotEmpty()
                val isEmailNotEmpty = emailEditText.text.toString().isNotEmpty()
                val isPasswordNotEmpty = passEditText.text.toString().isNotEmpty()
                signupBtn.isEnabled = isEmailNotEmpty && isNameNotEmpty && isPasswordNotEmpty
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        nameEditText.addTextChangedListener(textWatcher)
        emailEditText.addTextChangedListener(textWatcher)
        passEditText.addTextChangedListener(textWatcher)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}