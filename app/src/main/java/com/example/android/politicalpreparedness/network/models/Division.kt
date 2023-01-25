package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
//import kotlinx.parcelize.Parcelize
@Parcelize
data class Division(
        val id: String,
        val country: String,
        val state: String
) : Parcelable