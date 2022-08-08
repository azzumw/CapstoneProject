package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.LocalDataSource
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.data.RemoteDataSource
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TheRepository(
    private val localDataSource: DataSourceInterface,
    private val remoteDataSource: DataSourceInterface
)  {


    val elections: LiveData<List<Election>> = (localDataSource as LocalDataSource).getElections()
    val savedElections: LiveData<List<ElectionAndSavedElection>> =
        (localDataSource as LocalDataSource).getSavedElections()

    suspend fun getElections() {
        withContext(IO) {
            val electionsFromApi = callElectionsInfoApi().elections
            localDataSource.insertElections(electionsFromApi)
        }
    }

    fun getAnElection(electionId: Int): LiveData<Election> {

        return localDataSource.getAnElection(electionId)
    }

    suspend fun saveThisElection(savedElection: SavedElection) {
        localDataSource.saveThisElection(savedElection)
    }

    suspend fun removeThisElection(savedElection: SavedElection) {
        localDataSource.removeThisElection(savedElection)
    }

    fun getElectionIdFromSavedElection(electionId: Int): LiveData<SavedElection> {
        return localDataSource.getElectionIdFromSavedElection(electionId)
    }

    //network call for elections
    suspend fun callElectionsInfoApi(): ElectionResponse {
        return remoteDataSource.callElectionsInfoApi()
    }

    //network call for voters
    suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse {
        return remoteDataSource.callVoterInfoApi(address, electionId)
    }

    //network call for representatives
    suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse {
        return remoteDataSource.callRepresentativeInfoApi(address)
    }
}