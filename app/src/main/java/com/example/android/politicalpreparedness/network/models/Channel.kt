package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
//import kotlinx.parcelize.Parcelize

@Parcelize
data class Channel (
    val type: String,
    val id: String
):Parcelable