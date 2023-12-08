package com.malletsplay.eyecare.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.malletsplay.eyecare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivHelp.setOnClickListener {
            Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MainActivity, AboutActivity::class.java))
        }

        binding.btnCamera.setOnClickListener {
            startActivity(Intent(this@MainActivity, CameraActivity::class.java))
        }
    }
}