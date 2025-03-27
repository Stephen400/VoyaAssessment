package com.example.voyaassessment.data.model.remote

import com.google.gson.annotations.SerializedName


data class UpdateFoodResponse (

  @SerializedName("status"  ) var status  : String? = null,
  @SerializedName("message" ) var message : String? = null,
  @SerializedName("data"    ) var data    : Data?   = Data()

){


  data class Data (

    @SerializedName("id"          ) var id          : Int?                  = null,
    @SerializedName("name"        ) var name        : String?               = null,
    @SerializedName("description" ) var description : String?               = null,
    @SerializedName("category_id" ) var categoryId  : Int?                  = null,
    @SerializedName("calories"    ) var calories    : Int?                  = null,
    @SerializedName("created_at"  ) var createdAt   : String?               = null,
    @SerializedName("updated_at"  ) var updatedAt   : String?               = null,
    @SerializedName("foodTags"    ) var foodTags    : ArrayList<String>     = arrayListOf(),
    @SerializedName("foodImages"  ) var foodImages  : ArrayList<FoodImages> = arrayListOf(),
    @SerializedName("category"    ) var category    : Category?             = Category()

  )
}