package data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.repository.DataSourceInterface
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.util.*

class FakeDataSource():DataSourceInterface {


    private val _eList = MutableLiveData<List<Election>>()
    val eList : LiveData<List<Election>> get() = _eList

    override suspend fun insertElections(elections: List<Election>) {
       _eList.value = elections
    }

    override fun getElections(): LiveData<List<Election>> {
        return eList
    }

    override fun getSavedElections(): LiveData<List<ElectionAndSavedElection>> {

        return MutableLiveData()
    }

    override fun getAnElection(electionId: Int): LiveData<Election> {
        TODO("Not yet implemented")
    }

    override suspend fun saveThisElection(savedElection: SavedElection) {
        TODO("Not yet implemented")
    }

    override suspend fun removeThisElection(savedElection: SavedElection) {
        TODO("Not yet implemented")
    }

    override fun getElectionIdFromSavedElection(electionId: Int): LiveData<SavedElection> {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override suspend fun callVoterInfoApi(address: String, electionId: String): VoterInfoResponse {
        TODO("Not yet implemented")
    }

    override suspend fun callElectionsInfoApi(): ElectionResponse {
        //Implement this create a dummmy ElectionResponse
        val localDate = Date(1220227200L * 1000)
        val electionsList = List<Election>(3) {
            Election(it, "Election $it", localDate,
                Division("$it-division","USA","California")
            )
        }
        return ElectionResponse("someKind",electionsList)
    }

    override suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse {
        TODO("Not yet implemented")
    }
}