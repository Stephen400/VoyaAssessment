package com.example.voyaassessment.data.model.remote

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import com.google.gson.annotations.SerializedName

@VersionedParcelize
data class FoodData (

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

  ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        TODO("foodTags"),
        TODO("foodImages"),
        TODO("category")
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeValue(categoryId)
        parcel.writeValue(calories)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FoodData> {
        override fun createFromParcel(parcel: Parcel): FoodData {
            return FoodData(parcel)
        }

        override fun newArray(size: Int): Array<FoodData?> {
            return arrayOfNulls(size)
        }
    }
}