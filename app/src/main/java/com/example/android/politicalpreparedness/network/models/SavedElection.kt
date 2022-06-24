package com.example.android.politicalpreparedness.network.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "saved_election_table")
class SavedElection(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "electionDay")val electionDay: Date,
    @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division
)