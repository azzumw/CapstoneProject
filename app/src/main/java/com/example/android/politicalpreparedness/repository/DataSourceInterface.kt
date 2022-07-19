package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.*

interface DataSourceInterface {

    suspend fun insertElections(elections:List<Election>)

    fun getAnElection(electionId: Int): LiveData<Election>

    suspend fun saveThisElection(savedElection: SavedElection)

    suspend fun removeThisElection(savedElection: SavedElection)

    fun getElectionIdFromSavedElection(electionId: Int): LiveData<SavedElection>

    suspend fun clear()

    //network call for voters
    suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse

    //network call for elections
    suspend fun callElectionsInfoApi():ElectionResponse

    //network call for representatives
    suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse
}