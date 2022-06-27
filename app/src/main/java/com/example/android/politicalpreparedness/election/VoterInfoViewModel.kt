package com.example.android.politicalpreparedness.election

import android.content.ClipData
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
import com.example.android.politicalpreparedness.repository.TheRepository
import kotlinx.coroutines.launch


//replace ElectionDao with ElectionRepository obj
class VoterInfoViewModel(private val datasource: ElectionDao, electionId: Int) :
    ViewModel() {

    //TODO: Add live data to hold voter info


    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs


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

}