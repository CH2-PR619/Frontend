package com.malletsplay.eyecare.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.malletsplay.eyecare.R
import com.malletsplay.eyecare.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_RESULT = "extra_result"
    }

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getStringExtra(EXTRA_RESULT).toString()

        if (result == "normal") {
            binding.ivResult.setImageResource(R.drawable.result_healthy)
        } else {
            binding.ivResult.setImageResource(R.drawable.result_caution)
        }
    }
}