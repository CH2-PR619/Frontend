package com.malletsplay.eyecare.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.malletsplay.eyecare.data.api.ApiConfig
import com.malletsplay.eyecare.data.response.ResultResponse
import com.malletsplay.eyecare.data.response.UploadResponse
import com.malletsplay.eyecare.databinding.ActivityPreviewBinding
import com.malletsplay.eyecare.ui.ResultActivity.Companion.EXTRA_RESULT
import com.malletsplay.eyecare.util.reduceFileImage
import com.malletsplay.eyecare.util.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException

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

        binding.btnSend.setOnClickListener {
            uploadImage()
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

    private fun uploadImage(){
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            showLoading(true)

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService()
//                    val successResponse = apiService.uploadImage(multipartBody)
                    val resultResponse = apiService.getResult()
                    showLoading(false)
                    moveResult(resultResponse)
                } catch (e: HttpException) {
//                    val errorBody = e.response()?.errorBody()?.string()
//                    val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
                    Toast.makeText(this@PreviewActivity, e.message, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }
    }

    private fun moveResult(resultResponse: ResultResponse) {
        val result = resultResponse.data.result
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(EXTRA_RESULT, result)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}