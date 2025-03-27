package com.example.voyaassessment.data.model.remote

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import androidx.versionedparcelable.VersionedParcelize
import com.google.gson.annotations.SerializedName



@VersionedParcelize
data class FoodDetails (

  @SerializedName("status"  ) var status  : String? = null,
  @SerializedName("message" ) var message : String? = null,
  @SerializedName("data"    ) var data    : FoodData?   = FoodData()

): Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString(),
    TODO("data")
  )

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(status)
    parcel.writeString(message)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<FoodDetails> {
    override fun createFromParcel(parcel: Parcel): FoodDetails {
      return FoodDetails(parcel)
    }

    override fun newArray(size: Int): Array<FoodDetails?> {
      return arrayOfNulls(size)
    }
  }
}