package com.example.android.politicalpreparedness.network.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "saved_election_table")
class SavedElection(
    @PrimaryKey
    @ColumnInfo(name ="saved_election_id")val savedElectionId: Int)