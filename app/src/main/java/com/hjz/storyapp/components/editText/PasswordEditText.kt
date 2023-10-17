package com.hjz.storyapp.components.editText

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.hjz.storyapp.R

class PasswordEditText : AppCompatEditText {

    private lateinit var keyIcon : Drawable

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        keyIcon = ContextCompat.getDrawable(context, R.drawable.baseline_key_24) as Drawable
        showIconDrawable()

        addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString().length < 8) {
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Mengganti teks yang dimasukkan dengan karakter '*'
//                val text = s.toString()
//                val password = StringBuilder()
//                for (i in text.indices) {
//                    password.append('*')
//                }
                // Set teks yang sudah diubah kembali ke EditText
//                removeTextChangedListener(this) // Untuk menghindari rekursi
//                setText(password)
//                setSelection(password.length) // Agar kursor tetap di akhir teks
//                addTextChangedListener(this) // Mengaktifkan kembali TextWatcher
            }
        })
    }

    private fun showIconDrawable() {
        setIconDrawables(startOfTheText = keyIcon)
    }

    private fun setIconDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = resources.getString(R.string.passwordHint)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        compoundDrawablePadding = 20
    }
}