package com.example.android.politicalpreparedness.network.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.repository.DataSourceInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RemoteDataSource:DataSourceInterface {


    override fun getAnElection(electionId: Int): LiveData<Election> {
        TODO("Not yet implemented")
    }

    override suspend fun saveThisElection(savedElection: SavedElection) {
        TODO("Not yet implemented")
    }

    override suspend fun removeThisElection(savedElection: SavedElection) {
        TODO("Not yet implemented")
    }

    override fun getElectionIdFromSavedElection(electionId: Int): LiveData<SavedElection> {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }


    override suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse {
        return CivicsApi.retrofitService.getVoterInfo(address, electionId = electionId)
    }

    override suspend fun callElectionsInfoApi(): ElectionResponse {
        return withContext(Dispatchers.IO){
            CivicsApi.retrofitService.getElections()
        }
    }

    override suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse {
        return withContext(Dispatchers.IO){
            CivicsApi.retrofitService.getRepresentativesInfo(address)
        }
    }
}