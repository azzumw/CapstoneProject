package com.example.android.politicalpreparedness.election

import android.content.ClipData
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
import com.example.android.politicalpreparedness.repository.TheRepository
import kotlinx.coroutines.launch

enum class SAVE_STATUS(val str: String) {
    SAVED("unfollow"),
    NOT_SAVED("follow")
}

//replace ElectionDao with ElectionRepository obj
class VoterInfoViewModel(private val datasource: ElectionDao, private val electionId: Int) :
    ViewModel() {

    //TODO: Add live data to hold voter info
    private val _savedStatus = MutableLiveData<SAVE_STATUS>(SAVE_STATUS.NOT_SAVED)
    val savedStatus: LiveData<SAVE_STATUS> = _savedStatus

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

//    private val _election = MutableLiveData<Election>()
//    val election : LiveData<Election> get() = _election

    val election: LiveData<Election> = datasource.getAnElection(electionId).asLiveData()

    private val isElectionSaved  = datasource.getElectionIdFromSavedElection(electionId)

    val saveBtnTextState = Transformations.map(isElectionSaved){
        if(it == null){
            "Follow"
        }else{
            "Unfollow"
        }
    }

    init {
        Log.e("VoterInfoViewModel", electionId.toString())

    }




//    private suspend fun getElection() {
//        _election.value = datasource.getAnElection(electionId)
//    }


    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    private fun saveThisElection(){
            val savedElection = SavedElection(electionId)
            viewModelScope.launch {
                datasource.saveElection(savedElection)
            }
    }

//    private fun removeThisElection(){
//        if(_savedStatus.value ==true){
//            dataSource.deleteElection(election)
//        }
//        _savedStatus.value = false
//    }
    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}