package com.example.android.politicalpreparedness.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TheRepository(
    private val localDataSource: DataSourceInterface,
    private val remoteDataSource: DataSourceInterface,
    private val ioDispatcher: CoroutineDispatcher = IO
) : RepositoryInterface {

    override suspend fun getElections() {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                val electionsFromApi = callElectionsInfoApi().elections
                insertElections(electionsFromApi)
            }
        }
    }

    private suspend fun insertElections(list: List<Election>) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                localDataSource.insertElections(list)
            }
        }
    }


    override fun getAnElection(electionId: Int): LiveData<Election> {
        wrapEspressoIdlingResource {
            return localDataSource.getAnElection(electionId)
        }
    }

    override suspend fun saveThisElection(savedElection: SavedElection) {
        wrapEspressoIdlingResource {
            localDataSource.saveThisElection(savedElection)
        }
    }

    override suspend fun removeThisElection(savedElection: SavedElection) {
        wrapEspressoIdlingResource {
            localDataSource.removeThisElection(savedElection)
        }
    }

    override fun getSavedElectionByElectionID(electionId: Int): LiveData<SavedElection> {
        wrapEspressoIdlingResource {
            return localDataSource.getSavedElectionByElectionID(electionId)
        }
    }

    override fun getSavedElectionsFromLocalDataSource() =
        wrapEspressoIdlingResource { localDataSource.getSavedElections() }

    override fun getElectionsFromLocalDataBase() =
        wrapEspressoIdlingResource { localDataSource.getElections() }

    //network call for elections
    override suspend fun callElectionsInfoApi(): ElectionResponse {
        wrapEspressoIdlingResource {
            return remoteDataSource.callElectionsInfoApi()
        }
    }

    //network call for voters
    override suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse? {
        wrapEspressoIdlingResource {
            return remoteDataSource.callVoterInfoApi(address, electionId)
        }
    }

    //network call for representatives
    override suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse {
        wrapEspressoIdlingResource {
            return remoteDataSource.callRepresentativeInfoApi(address)
        }
    }

    @VisibleForTesting
    override suspend fun deleteAllElections() {
        withContext(ioDispatcher) {
            localDataSource.deleteAllElections()
        }
    }

    @VisibleForTesting
    override suspend fun deleteAllSavedElections() {
        withContext(ioDispatcher) {
            localDataSource.deleteAllSavedElections()
        }
    }
}