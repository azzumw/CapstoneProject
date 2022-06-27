package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TheRepository(val database: ElectionDao) {

    val elections: LiveData<List<Election>> = database.getAllElections()



    suspend fun getElections() {

        withContext(IO) {
            val electionsFromApi = CivicsApi.retrofitService.getElections().elections
            database.insertAllElections(electionsFromApi)
        }
    }

    suspend fun getElection(id: Int) {

    }

    suspend fun deleteElection(savedElection: SavedElection) {
        withContext(IO) {
            database.deleteElection(savedElection)
        }
    }

    suspend fun clear() {
        withContext(IO) {
            database.clear()
        }
    }
}