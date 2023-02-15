package repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.repository.RepositoryInterface

class FakeRepository(private val mElectionList : List<Election>) : RepositoryInterface {

    private val _elections = MutableLiveData<List<Election>>()
    private val elections: LiveData<List<Election>> get() = _elections

    private val _savedElections = MutableLiveData<List<ElectionAndSavedElection>>()

    override suspend fun getElections() {
        _elections.value = callElectionsInfoApi().elections
    }

    override suspend fun callElectionsInfoApi(): ElectionResponse = ElectionResponse("someKind", mElectionList)


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

    override fun getSavedElectionByElectionID(electionId: Int): LiveData<SavedElection> {
        val se =  _savedElections.value?.filter {
            it.savedElection.savedElectionId == electionId
        }?.map { it.savedElection }?.take(1)

        val liveSavedElection = MutableLiveData<SavedElection>()

        liveSavedElection.value = se?.first()

        return liveSavedElection
    }



    override suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse {
        TODO("Not yet implemented")
    }

    override suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse {
        TODO("Not yet implemented")
    }

    override fun getSavedElectionsFromLocalDataSource(): LiveData<List<ElectionAndSavedElection>> =
        _savedElections


    override fun getElectionsFromLocalDataBase(): LiveData<List<Election>> = elections
    override suspend fun deleteAllElections() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllSavedElections() {
        TODO("Not yet implemented")
    }

}