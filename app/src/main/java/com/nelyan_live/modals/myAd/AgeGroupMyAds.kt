package com.nelyan_live.modals.myAd

import android.os.Parcel
import android.os.Parcelable

data class AgeGroupMyAds(
        val activityPostId: Int,
        var ageFrom: String?,
        var ageTo: String,
        val createdAt: String,
        var days: String,
        val eventId: Int,
        val id: Int,
        var timeFrom: String,
        var timeTo: String,
        val updatedAt: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(activityPostId)
        parcel.writeString(ageFrom)
        parcel.writeString(ageTo)
        parcel.writeString(createdAt)
        parcel.writeString(days)
        parcel.writeInt(eventId)
        parcel.writeInt(id)
        parcel.writeString(timeFrom)
        parcel.writeString(timeTo)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AgeGroupMyAds> {
        override fun createFromParcel(parcel: Parcel): AgeGroupMyAds {
            return AgeGroupMyAds(parcel)
        }

        override fun newArray(size: Int): Array<AgeGroupMyAds?> {
            return arrayOfNulls(size)
        }
    }
}