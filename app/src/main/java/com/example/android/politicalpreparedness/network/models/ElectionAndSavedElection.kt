package com.example.android.politicalpreparedness.network.models

import androidx.room.Embedded
import androidx.room.Relation


data class ElectionAndSavedElection(
    @Embedded val election: Election,
    @Relation(
        parentColumn = "id",
        entityColumn = "saved_election_id"
    )
    val savedElection: SavedElection
)
