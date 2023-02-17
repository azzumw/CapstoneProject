package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.*

interface  RepositoryInterface {

    suspend fun getElections()

    fun getAnElection(electionId: Int): LiveData<Election>

    suspend fun saveThisElection(savedElection: SavedElection)

    suspend fun removeThisElection(savedElection: SavedElection)
    fun getSavedElectionByElectionID(electionId: Int): LiveData<SavedElection>

    //network call for elections
    suspend fun callElectionsInfoApi(): ElectionResponse

    //network call for voters
    suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse

    //network call for representatives
    suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse

    fun getSavedElectionsFromLocalDataSource():LiveData<List<ElectionAndSavedElection>>

    fun getElectionsFromLocalDataBase():LiveData<List<Election>>
    suspend fun deleteAllElections()

    suspend fun deleteAllSavedElections()
}