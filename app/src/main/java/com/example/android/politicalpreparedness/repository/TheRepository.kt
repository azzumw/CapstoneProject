package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TheRepository(val database: ElectionDao) {

    val elections: LiveData<List<Election>> = database.getAllElections()
    val savedElections: LiveData<List<ElectionAndSavedElection>> =
        database.getElectionAndSavedElection().asLiveData()

    suspend fun getElections() {
        withContext(IO) {
            val electionsFromApi = callElectionsInfoApi().elections
            database.insertAllElections(electionsFromApi)
        }
    }

    fun getAnElection(electionId: Int): LiveData<Election> {
        return database.getAnElection(electionId).asLiveData()
    }

    suspend fun saveThisElection(savedElection: SavedElection) {
        withContext(IO) {
            database.saveElection(savedElection)
        }
    }

    suspend fun removeThisElection(savedElection: SavedElection) {
        withContext(IO) {
            database.deleteElection(savedElection)
        }
    }

    fun getElectionIdFromSavedElection(electionId: Int): LiveData<SavedElection> {
        return database.getElectionIdFromSavedElection(electionId)
    }

    suspend fun clear() {
        withContext(IO) {
            database.clear()
        }
    }

    //network call for elections
    private suspend fun callElectionsInfoApi():ElectionResponse{
        return withContext(IO){
            CivicsApi.retrofitService.getElections()
        }
    }

    //network call for voters
    suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse {
        return CivicsApi.retrofitService.getVoterInfo(address, electionId = electionId)
    }

    //network call for representatives
    suspend fun callRepresentativeInfoApi(address: Address):RepresentativeResponse{
        return withContext(IO){
               CivicsApi.retrofitService.getRepresentativesInfo(address)
        }
    }
}