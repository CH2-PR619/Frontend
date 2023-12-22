package com.malletsplay.eyecare.data.response

import com.google.gson.annotations.SerializedName

data class ResultResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("result")
	val result: String
)
