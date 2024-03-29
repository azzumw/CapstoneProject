package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.repository.DataSourceInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource(val database:ElectionDao) : DataSourceInterface{

//    override val elections: LiveData<List<Election>> = database.getAllElections()
//     val savedElections: LiveData<List<ElectionAndSavedElection>> =
//        database.getElectionAndSavedElection().asLiveData()

    override suspend fun insertElections(elections:List<Election>){
        database.insertAllElections(elections)
    }

    override fun getSavedElections():LiveData<List<ElectionAndSavedElection>>{
        return database.getElectionAndSavedElection().asLiveData()
    }

    override fun getElections():LiveData<List<Election>>{
        return database.getAllElections()
    }

    override fun getAnElection(electionId: Int): LiveData<Election> {
        return database.getAnElection(electionId)
    }

    override suspend fun saveThisElection(savedElection: SavedElection) {
        withContext(Dispatchers.IO) {
            database.saveElection(savedElection)
        }
    }

    override suspend fun removeThisElection(savedElection: SavedElection) {
        withContext(Dispatchers.IO) {
            database.deleteElection(savedElection)
        }
    }

    override fun getSavedElectionByElectionID(electionId: Int): LiveData<SavedElection> {
        return database.getSavedElectionByElectionID(electionId)
    }

     override suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse {
        TODO("Not yet implemented")
    }

    override suspend fun callElectionsInfoApi(): ElectionResponse {
        TODO("Not yet implemented")
    }

    override suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllElections() {
        withContext(Dispatchers.IO){
            database.clearAllElectionsFromElectionsTable()
        }

    }

    override suspend fun deleteAllSavedElections() {
        withContext(Dispatchers.IO){
            database.clearAllElectionsFromSavedElectionsTable()
        }
    }
}