package data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.repository.DataSourceInterface
import java.util.*

class FakeDataSource():DataSourceInterface {


    val eList = mutableListOf<Election>()

    override suspend fun insertElections(elections: List<Election>) {
       eList.addAll(elections)
    }

    override fun getElections(): LiveData<List<Election>> {
        TODO("Not yet implemented")
    }

    override fun getSavedElections(): LiveData<List<ElectionAndSavedElection>> {
        TODO("Not yet implemented")
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
        val electionsList = List<Election>(3) {
            Election(it, "Election $it", Date("2022-07-26"),
                Division("$it-division","USA","California")
            )
        }
        return ElectionResponse("someKind",electionsList)
    }

    override suspend fun callRepresentativeInfoApi(address: Address): RepresentativeResponse {
        TODO("Not yet implemented")
    }
}