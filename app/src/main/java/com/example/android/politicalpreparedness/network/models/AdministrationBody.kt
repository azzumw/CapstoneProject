package com.example.android.politicalpreparedness.network.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AdministrationBody (
        val name: String? = null,
        val electionInfoUrl: String? = null,
        val votingLocationFinderUrl: String? = null,
        val ballotInfoUrl: String? = null,
        @Embedded(prefix = "add_") val correspondenceAddress: Address? = null
)