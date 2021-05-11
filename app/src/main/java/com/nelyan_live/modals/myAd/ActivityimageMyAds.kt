package com.nelyan_live.modals.myAd

import android.os.Parcel
import android.os.Parcelable

data class ActivityimageMyAds(
    val activityId: Int,
    val id: Int,
    val images: String,
    val mediaType: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel!!.writeInt(activityId)
        parcel.writeInt(id)
        parcel.writeString(images)
        parcel.writeInt(mediaType)

    }

    override fun describeContents(): Int {
        return 0

    }

    companion object CREATOR : Parcelable.Creator<ActivityimageMyAds> {
        override fun createFromParcel(parcel: Parcel): ActivityimageMyAds {
            return ActivityimageMyAds(parcel)
        }

        override fun newArray(size: Int): Array<ActivityimageMyAds?> {
            return arrayOfNulls(size)
        }
    }
}