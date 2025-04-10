package com.example.voyaassessment.data.model.remote.request

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class UpdateFoodRequest(
    val name: String,
    val description: String,
    @SerializedName("category_id")
    val categoryId: String,
    val calories: String,
    val tags: List<String>,
    val images: List<MultipartBody.Part>
)
