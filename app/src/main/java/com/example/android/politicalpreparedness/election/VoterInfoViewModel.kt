package com.example.android.politicalpreparedness.election

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.launch
import java.lang.Exception


//replace ElectionDao with ElectionRepository obj
class VoterInfoViewModel(
    private val datasource: ElectionDao,
    electionId: Int,
    val division: Division
) :
    ViewModel() {

    //TODO: Add live data to hold voter info


    //TODO: Add var and methods to populate voter info
    private val _showSnackBarEvent = MutableLiveData<Boolean>(false)
    val showSnackBarEvent: LiveData<Boolean> = _showSnackBarEvent

    private val _voterLocationUrl = MutableLiveData<String?>()
    val voterLocationUrl: LiveData<String?> get() = _voterLocationUrl

    private val _ballotInfoUrl = MutableLiveData<String?>()
    val ballotInfoUrl: LiveData<String?> get() = _ballotInfoUrl

    val isVoterAndBallotInfoNull: Boolean =
        (voterLocationUrl.value.isNullOrEmpty() && ballotInfoUrl.value.isNullOrEmpty())

    private val _correspondenceAddress = MutableLiveData<Address?>()
    val correspondenceAddress: LiveData<Address?> get() = _correspondenceAddress

    private val _state = MutableLiveData<List<State>?>()
    val state :LiveData<List<State>?> get() = _state


    val election: LiveData<Election> = datasource.getAnElection(electionId).asLiveData()

    private val isElectionSaved = datasource.getElectionIdFromSavedElection(electionId)

    val saveBtnTextState = Transformations.map(isElectionSaved) {
        if (it == null) {
            "Follow"
        } else {
            "Unfollow"
        }
    }

    private var savedElection: SavedElection

    init {
        Log.e("VoterInfoViewModel", electionId.toString())
        savedElection = SavedElection(electionId)

        getVoterInformation(electionId)
    }

    private fun getVoterInformation(electId: Int) {
        val country = division.country
        val state = division.state
        val address = if (state.isEmpty()) country else "$country,$state"
        //make api call to voters information
        Log.e("VoterModel:", "country: $country")
        Log.e("VoterModel:", "state: $state")
        Log.e("VoterModel:", "address: $address")

        viewModelScope.launch {

            try {
                val voterInfoFromApi =
                    CivicsApi.retrofitService.getVoterInfo(address, electId.toString())


                if (!voterInfoFromApi.state.isNullOrEmpty()) {
//                    datasource.insertState(voterInfoFromApi.state)
                    _state.value = voterInfoFromApi.state

                    _voterLocationUrl.value = voterInfoFromApi.state[0].electionAdministrationBody.votingLocationFinderUrl
                    _ballotInfoUrl.value = voterInfoFromApi.state[0].electionAdministrationBody.ballotInfoUrl
                    _correspondenceAddress.value = voterInfoFromApi.state[0].electionAdministrationBody.correspondenceAddress

                    Log.e("VoterModel", voterInfoFromApi.state.toString())
                    Log.e(
                        "VoterModel",
                        voterInfoFromApi.state[0].electionAdministrationBody.votingLocationFinderUrl.toString()
                    )
                } else {
                    Log.e("VoterModel", voterInfoFromApi.state.toString())
//                    _state.value = emptyList()

                }

            } catch (e: Exception) {
                Log.e("VoterModel: ", "error: ${e.cause}")
                Log.e("VoterModel: ", "error: ${e.message}")
                Log.e("VoterModel: ", "error: ${e.localizedMessage}")
                _showSnackBarEvent.value = true
            }
        }


    }


    private fun saveThisElection() {

        viewModelScope.launch {
            datasource.saveElection(savedElection)
        }
    }

    private fun removeThisElection() {

        viewModelScope.launch {
            datasource.deleteElection(savedElection)
        }
    }

    fun followOrUnFollowElection() {
        if (saveBtnTextState.value == "Follow") {
            saveThisElection()
        } else {
            removeThisElection()
        }
    }

    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }

}