package com.example.android.politicalpreparedness.election

import android.content.ClipData
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
import com.example.android.politicalpreparedness.repository.TheRepository
import kotlinx.coroutines.launch
import java.lang.Exception


//replace ElectionDao with ElectionRepository obj
class VoterInfoViewModel(private val datasource: ElectionDao, electionId: Int, val division: Division) :
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

        getVoterInformation(electionId)
    }

    private fun getVoterInformation( electId:Int){
        val country = division.country
        val state = division.state
        val address = if(state.isEmpty()) country else "$country,$state"
        //make api call to voters information
        Log.e("VoterModel:","country: $country")
        Log.e("VoterModel:","state: $state")
        Log.e("VoterModel:","address: $address")

        viewModelScope.launch {

            try {
                val voterInfoFromApi = CivicsApi.retrofitService.getVoterInfo(address,electId.toString())
                Log.e("VoterModel",voterInfoFromApi.state.toString())
            }catch (e:Exception){
                Log.e("VoterModel: ","error: ${e.cause}")
                Log.e("VoterModel: ","error: ${e.message}")
                Log.e("VoterModel: ","error: ${e.localizedMessage}")
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

}