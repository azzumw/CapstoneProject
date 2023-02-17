package com.example.android.politicalpreparedness.election

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.ServiceLocator.repository
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.repository.RepositoryInterface
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.coroutineContext

private const val FOLLOW_BUTTON_TEXT = "Follow"
private const val UNFOLLOW_BUTTON_TEXT = "Unfollow"

class VoterInfoViewModel(
    private val repository: RepositoryInterface,
    electionId: Int,
    private val division: Division
) :
    ViewModel() {

    private val _showSnackBarEvent = MutableLiveData(false)
    val showSnackBarEvent: LiveData<Boolean> = _showSnackBarEvent

    private val _voterLocationUrl = MutableLiveData<String?>(null)
    val voterLocationUrl: LiveData<String?> get() = _voterLocationUrl

    private val _ballotInfoUrl = MutableLiveData<String?>(null)
    val ballotInfoUrl: LiveData<String?> get() = _ballotInfoUrl

    val isVoterAndBallotInfoNull: Boolean =
        (voterLocationUrl.value.isNullOrEmpty() && ballotInfoUrl.value.isNullOrEmpty())

    private val _correspondenceAddress = MutableLiveData<Address?>(null)
    val correspondenceAddress: LiveData<Address?> get() = _correspondenceAddress

    private val _state = MutableLiveData<List<State>?>(null)
    val state: LiveData<List<State>?> get() = _state

    val election: LiveData<Election> =   repository.getAnElection(electionId)

    private val isElectionSaved =  repository.getSavedElectionByElectionID(electionId)

    val saveBtnTextState = Transformations.map(isElectionSaved) {
        if (it == null) {
            FOLLOW_BUTTON_TEXT
        } else {
            UNFOLLOW_BUTTON_TEXT
        }
    }

    private var savedElection: SavedElection

    init {
        savedElection = SavedElection(electionId)
        getVoterInformation(electionId)
    }


    private fun getVoterInformation(electId: Int) {
        val country = division.country
        val state = division.state
        val address = if (state.isEmpty()) country else "$country,$state"

        viewModelScope.launch {

            try {
                val voterInfoFromApi = repository.callVoterInfoApi(address, electId.toString())

                if (!voterInfoFromApi.state.isNullOrEmpty()) {
                    _state.value = voterInfoFromApi.state

                    _voterLocationUrl.value =
                        voterInfoFromApi.state[0].electionAdministrationBody.votingLocationFinderUrl

                    _ballotInfoUrl.value =
                        voterInfoFromApi.state[0].electionAdministrationBody.ballotInfoUrl

                    _correspondenceAddress.value =
                        voterInfoFromApi.state[0].electionAdministrationBody.correspondenceAddress

                }

            } catch (e: Exception) {

                _showSnackBarEvent.value = true
            }
        }
    }

    fun followOrUnFollowElection() {
        viewModelScope.launch {
            if (saveBtnTextState.value == "Follow") {
                repository.saveThisElection(savedElection)
            } else {
                repository.removeThisElection(savedElection)
            }
        }
    }

    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }
}