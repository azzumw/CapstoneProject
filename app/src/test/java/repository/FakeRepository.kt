package repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.data.RemoteDataSource.getAnElection
import com.example.android.politicalpreparedness.network.data.RemoteDataSource.getElections
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.repository.RepositoryInterface
import util.createStates



class FakeRepository(private val mElectionList: List<Election>) : RepositoryInterface {

    private val _elections = MutableLiveData<List<Election>>()
    private val elections: LiveData<List<Election>> get() = _elections

    private val _savedElections = MutableLiveData<List<ElectionAndSavedElection>>()
    val savedElections : LiveData<List<ElectionAndSavedElection>>   = _savedElections

    private val tempSavedElections = mutableListOf<SavedElection?>()

    private val _liveSavedElection = MutableLiveData<SavedElection>()
    private val liveSavedElection : LiveData<SavedElection> get() = _liveSavedElection

    var optionResult:Int = 0

    override suspend fun getElections() {
        _elections.value = callElectionsInfoApi().elections
    }

    override suspend fun callElectionsInfoApi(): ElectionResponse =
        ElectionResponse("someKind", mElectionList)


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
            ElectionAndSavedElection(it, savedElection)
        }

        //unsure why we need this?
       getSavedElectionByElectionID(savedElection.savedElectionId)
    }

    override suspend fun removeThisElection(savedElection: SavedElection) {
        _savedElections.value = _savedElections.value?.filterNot {
            it.savedElection.savedElectionId == savedElection.savedElectionId
        }

        getSavedElectionByElectionID(savedElection.savedElectionId)
    }

    override fun getSavedElectionByElectionID(electionId: Int): LiveData<SavedElection> {

        val se : SavedElection?

        val isNullOrEmpty = _savedElections.value.isNullOrEmpty()

        se = if(!isNullOrEmpty){
            _savedElections.value?.filter {

                it.savedElection.savedElectionId == electionId
            }?.map { it.savedElection }?.take(1)?.first()
        } else null

        _liveSavedElection.value  =  se

        return liveSavedElection
    }


    override suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse? {

        getElections()

        val election = getAnElection(electionId.toInt()).value

        val states =  createStates(electionId.toInt(),optionResult)

        return VoterInfoResponse(election!!, state = states)
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

    fun clearRepo() {
        _savedElections.value = emptyList()
        _elections.value = emptyList()

    }

}