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
        var minAge: String?,
        var maxAge: String?,
        var name: String,
        var price: String,
        val status: Int,
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
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt())
//EventMyAds(activityId=1695, city=Mohli, createdAt=2021-12-27T13:11:25.000Z, description=jioiuhjuihj, dateFrom=27/12/2021,
// dateTo=31/12/2021, startTime=18:40, endTime=20:40, id=954, image=/uploads/users/1640610628789-file.jpg,
// latitude=31.2409038, longitude=77.5834959, minAge=22, maxAge=66, name=2e34e32er4, price=565, status=1, userId=1263)
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
        parcel.writeString(minAge)
        parcel.writeString(maxAge)
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeInt(status)
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