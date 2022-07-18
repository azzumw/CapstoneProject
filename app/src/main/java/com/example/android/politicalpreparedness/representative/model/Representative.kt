package com.example.android.politicalpreparedness.representative.model

import android.os.Parcelable
import com.example.android.politicalpreparedness.network.models.Office
import com.example.android.politicalpreparedness.network.models.Official
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Representative (
        val official: Official,
        val office: Office
):Parcelable