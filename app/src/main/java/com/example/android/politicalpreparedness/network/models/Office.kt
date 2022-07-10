package com.example.android.politicalpreparedness.network.models

import com.example.android.politicalpreparedness.representative.model.Representative
import com.squareup.moshi.Json

data class Office (
    val name: String,
    @Json(name="divisionId") val division:Division,
    @Json(name="officialIndices") val officials: List<Int>
) {
    fun getRepresentatives(pOfficials: List<Official>): List<Representative> {
        return this.officials.map {
            Representative(pOfficials[it], this)
        }
    }
}
