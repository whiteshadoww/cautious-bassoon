package me.snowshadow.travelmantics

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TravelDeal(
    var id: String = "",
    var name: String = "",
    var price: String = "",
    var desc: String = "",
    var imgUrl: String = ""
) : Parcelable