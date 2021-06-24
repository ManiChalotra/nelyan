package com.nelyanlive.modals.myAd

import android.os.Parcel
import android.os.Parcelable

data class EventMyAds(
        val activityId: Int,
        var city: String,
        val createdAt: String,
        var description: String,
        var dateFrom: String,
        var dateTo: String,
        var startTime: String,
        var endTime: String,
        val id: Int,
        var image: String,
        var latitude: String,
        var longitude: String,
        val name: String,
        var price: String,
        val status: Int,
        val updatedAt: String,
        val userId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
        parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(activityId)
        parcel.writeString(city)
        parcel.writeString(createdAt)
        parcel.writeString(description)
        parcel.writeString(dateFrom)
        parcel.writeString(dateTo)
        parcel.writeString(startTime)
        parcel.writeString(endTime)
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeInt(status)
        parcel.writeString(updatedAt)
        parcel.writeInt(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventMyAds> {
        override fun createFromParcel(parcel: Parcel): EventMyAds {
            return EventMyAds(parcel)
        }

        override fun newArray(size: Int): Array<EventMyAds?> {
            return arrayOfNulls(size)
        }
    }
}