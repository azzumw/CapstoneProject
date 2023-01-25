package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import kotlinx.android.parcel.Parcelize
//import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    var line1: String,
    var line2: String? = null,
    var city: String,
    var state: String,
    var zip: String
):Parcelable{




    fun toFormattedString(): String {
        var output = line1.plus("\n")
        if (!line2.isNullOrEmpty()) output = output.plus(line2).plus("\n")
        output = output.plus("$city, $state $zip")
        return output
    }


}