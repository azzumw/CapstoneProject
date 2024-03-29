package com.example.android.politicalpreparedness.network.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.repository.DataSourceInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RemoteDataSource : DataSourceInterface {

    override suspend fun insertElections(elections: List<Election>) {
        TODO("Not yet implemented")
    }

    override fun getElections(): LiveData<List<Election>> {
        TODO("Not yet implemented")
    }

    override fun getSavedElections(): LiveData<List<ElectionAndSavedElection>> {
        TODO("Not yet implemented")
    }

    override fun getAnElection(electionId: Int): LiveData<Election> {
        TODO("Not yet implemented")
    }

    override suspend fun saveThisElection(savedElection: SavedElection) {
        TODO("Not yet implemented")
    }

    override suspend fun removeThisElection(savedElection: SavedElection) {
        TODO("Not yet implemented")
    }

    override fun getSavedElectionByElectionID(electionId: Int): LiveData<SavedElection> {
        TODO("Not yet implemented")
    }

    override suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse {
        return CivicsApi.retrofitService.getVoterInfo(address, electionId = electionId)
    }

    override suspend fun callElectionsInfoApi(): ElectionResponse {
        return withContext(Dispatchers.IO) {
            CivicsApi.retrofitService.getElections()
        }
    }

    override suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse {
        return withContext(Dispatchers.IO) {
            CivicsApi.retrofitService.getRepresentativesInfo(address)
        }
    }

    override suspend fun deleteAllElections() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllSavedElections() {
        TODO("Not yet implemented")
    }
}