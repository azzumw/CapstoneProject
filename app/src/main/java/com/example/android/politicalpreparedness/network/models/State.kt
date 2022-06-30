package com.example.android.politicalpreparedness.network.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass


@Entity(tableName = "state_table")
data class State (
    @PrimaryKey val name: String,
    @Embedded(prefix = "admin_") val electionAdministrationBody: AdministrationBody
)