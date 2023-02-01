package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.models.*
class FakeRepository() : RepositoryInterface {

    private val _elections = MutableLiveData<List<Election>>()
    private val elections: LiveData<List<Election>> get() = _elections

    private val _savedElections = MutableLiveData<List<ElectionAndSavedElection>>()

    override suspend fun getElections() {
        _elections.value = callElectionsInfoApi().elections
    }

    fun addElections(electionsList : List<Election>){
        _elections.value = electionsList
    }

    override fun getAnElection(electionId: Int): LiveData<Election> {
        val election = MutableLiveData<Election>()

        val e = elections.value?.first {
            it.id == electionId
        }

        election.value = e

        return election
    }

    override suspend fun saveThisElection(savedElection: SavedElection) {
        _savedElections.value = elections.value?.filter {
            it.id == savedElection.savedElectionId
        }?.map {
            ElectionAndSavedElection(it, SavedElection(it.id))
        }
    }

    override suspend fun removeThisElection(savedElection: SavedElection) {

    }

    override fun getElectionIdFromSavedElection(electionId: Int): LiveData<SavedElection> {
        val se =  _savedElections.value?.filter {
            it.savedElection.savedElectionId == electionId
        }?.map { it.savedElection }?.take(1)

        val liveSavedElection = MutableLiveData<SavedElection>()

        liveSavedElection.value = se?.first()

        return liveSavedElection
    }

    override suspend fun callElectionsInfoApi(): ElectionResponse =  ElectionResponse("someKind", elections.value!! )


    override suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse {
        TODO("Not yet implemented")
    }

    override suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse {
        TODO("Not yet implemented")
    }

    override fun getSavedElectionsFromLocalDataSource(): LiveData<List<ElectionAndSavedElection>> =
        _savedElections


    override fun getElectionsFromLocalDataBase(): LiveData<List<Election>> = elections

}