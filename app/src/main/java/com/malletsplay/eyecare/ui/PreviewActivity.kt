package com.malletsplay.eyecare.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.malletsplay.eyecare.databinding.ActivityPreviewBinding

class PreviewActivity : AppCompatActivity() {
    companion object {
        const val IMAGE_URI = "uri"
    }

    private lateinit var binding: ActivityPreviewBinding
    private var currentImageUri: Uri? = null
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(this, "No media selected", Toast.LENGTH_SHORT).show()
        }
    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraActivity.CAMERA_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERA_IMAGE)?.toUri()
            showImage()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        currentImageUri = intent.getStringExtra(IMAGE_URI)?.let { Uri.parse(it) }

        if (savedInstanceState != null){
            val raw = savedInstanceState.getString("imageUri")
            if (raw != null) {
                currentImageUri = Uri.parse(raw)
            }
        }
        showImage()

        binding.btnCamera.setOnClickListener {
            launcherIntentCamera.launch(Intent(this, CameraActivity::class.java))
        }

        binding.btnGallery.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentImageUri?.let { uri ->
            outState.putString(IMAGE_URI, uri.toString())
        }
    }

    private fun showImage(){
        currentImageUri?.let {
            binding.ivPreview.setImageURI(it)
        }
    }
}