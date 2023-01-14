package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.LocalDataSource
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.data.RemoteDataSource
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TheRepository(
    private val localDataSource: DataSourceInterface,
    private val remoteDataSource: DataSourceInterface,
    private val ioDispatcher: CoroutineDispatcher = IO
) : RepositoryInterface {

//    val elections: LiveData<List<Election>> = localDataSource.getElections()
//    val savedElections: LiveData<List<ElectionAndSavedElection>> =
//        localDataSource.getSavedElections()

    override suspend fun getElections() {
        withContext(ioDispatcher) {
            val electionsFromApi = callElectionsInfoApi().elections
            localDataSource.insertElections(electionsFromApi)
        }
    }



    override fun getAnElection(electionId: Int): LiveData<Election> {

        return localDataSource.getAnElection(electionId)
    }

    override suspend fun saveThisElection(savedElection: SavedElection) {
        localDataSource.saveThisElection(savedElection)
    }

    override suspend fun removeThisElection(savedElection: SavedElection) {
        localDataSource.removeThisElection(savedElection)
    }

    override fun getElectionIdFromSavedElection(electionId: Int): LiveData<SavedElection> {
        return localDataSource.getElectionIdFromSavedElection(electionId)
    }

    //network call for elections
    override suspend fun callElectionsInfoApi(): ElectionResponse {
        return remoteDataSource.callElectionsInfoApi()
    }

    //network call for voters
    override suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse {
        return remoteDataSource.callVoterInfoApi(address, electionId)
    }

    //network call for representatives
    override suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse {
        return remoteDataSource.callRepresentativeInfoApi(address)
    }

    override fun getSavedElectionsFromLocalDataSource() = localDataSource.getSavedElections()

    override fun getElectionsFromLocalDataBase() = localDataSource.getElections()


}