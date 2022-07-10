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

class TheRepository(private val localDataSource: LocalDataSource, private val remoteDataSource: RemoteDataSource) : DataSourceInterface {

//    val elections: LiveData<List<Election>> = database.getAllElections()
    val elections: LiveData<List<Election>> = localDataSource.elections
    val savedElections : LiveData<List<ElectionAndSavedElection>> = localDataSource.savedElections
//    val savedElections: LiveData<List<ElectionAndSavedElection>> =
//        database.getElectionAndSavedElection().asLiveData()

     suspend fun getElections() {
        withContext(IO) {
            val electionsFromApi = callElectionsInfoApi().elections
//            database.insertAllElections(electionsFromApi)
            localDataSource.insertElections(electionsFromApi)
        }
    }

    override fun getAnElection(electionId: Int): LiveData<Election> {
//        return database.getAnElection(electionId).asLiveData()
        return localDataSource.getAnElection(electionId)
    }

    override suspend fun saveThisElection(savedElection: SavedElection) {
//        withContext(IO) {
//            database.saveElection(savedElection)
//        }
        localDataSource.saveThisElection(savedElection)
    }

    override suspend fun removeThisElection(savedElection: SavedElection) {
//        withContext(IO) {
//            database.deleteElection(savedElection)
//        }
        localDataSource.removeThisElection(savedElection)
    }

    override fun getElectionIdFromSavedElection(electionId: Int): LiveData<SavedElection> {
//        return database.getElectionIdFromSavedElection(electionId)
        return localDataSource.getElectionIdFromSavedElection(electionId)
    }

    override suspend fun clear() {
//        withContext(IO) {
//            database.clear()
//        }
        localDataSource.clear()
    }

    //network call for elections
     override suspend fun callElectionsInfoApi():ElectionResponse{
//        return withContext(IO){
//            CivicsApi.retrofitService.getElections()
//        }

         return remoteDataSource.callElectionsInfoApi()
    }

    //network call for voters
    override suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse {
//        return CivicsApi.retrofitService.getVoterInfo(address, electionId = electionId)
        return remoteDataSource.callVoterInfoApi(address,electionId)
    }

    //network call for representatives
    override suspend fun callRepresentativeInfoApi(address: Address):RepresentativeResponse{
        return remoteDataSource.callRepresentativeInfoApi(address)
    }
}