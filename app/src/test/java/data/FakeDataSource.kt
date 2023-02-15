package data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.repository.DataSourceInterface

class FakeDataSource(private val mElectionList :MutableList<Election> ) : DataSourceInterface {

    private val _savedElectionsList = MutableLiveData<List<ElectionAndSavedElection>>()
    private val savedElectionsList: LiveData<List<ElectionAndSavedElection>>
        get() = _savedElectionsList

    private val _eList = MutableLiveData<List<Election>>()
    private val eList: LiveData<List<Election>> get() = _eList

    private val savedElectionList = mutableListOf<SavedElection>()

    private val data = MutableLiveData<Election>()

    override suspend fun insertElections(elections: List<Election>) {
        _eList.value = elections
    }

    override fun getElections(): LiveData<List<Election>> = eList


    override fun getSavedElections(): LiveData<List<ElectionAndSavedElection>> =  savedElectionsList


    override fun getAnElection(electionId: Int): LiveData<Election> {
        data.value = eList.value?.get(electionId)
        return data
    }

    override suspend fun saveThisElection(savedElection: SavedElection) {
        savedElectionList.add(savedElection)

        _savedElectionsList.value = savedElectionList.map {
            ElectionAndSavedElection(getAnElection(it.savedElectionId).value!!,it)
        }

    }

    override suspend fun removeThisElection(savedElection: SavedElection) {
        savedElectionList.remove(savedElection)

        _savedElectionsList.value = savedElectionList.map {
            ElectionAndSavedElection(getAnElection(it.savedElectionId).value!!,it)
        }
    }

    override fun getSavedElectionByElectionID(electionId: Int): LiveData<SavedElection> {
        val result = savedElectionList.firstOrNull {
            it.savedElectionId == electionId
        }

        return MutableLiveData(result)
    }

    override suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse {
       val election = getAnElection(electionId.toInt()).value!!
        return VoterInfoResponse(election)
    }

    override suspend fun callElectionsInfoApi(): ElectionResponse = ElectionResponse("someKind", mElectionList)


    override suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllElections() {
        mElectionList.clear()
        _eList.value = null
    }

    override suspend fun deleteAllSavedElections() {
        savedElectionList.clear()
        _savedElectionsList.value = null
    }
}