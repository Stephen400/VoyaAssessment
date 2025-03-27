package com.example.voyaassessment.data.model.remote.request

import com.google.gson.annotations.SerializedName

data class UpdateFoodRequest(
    val name: String,
    val description: String,
    @SerializedName("category_id")
    val categoryId: String,
    val calories: String,
    val tags: List<Int>,
    val images: List<String>
)
