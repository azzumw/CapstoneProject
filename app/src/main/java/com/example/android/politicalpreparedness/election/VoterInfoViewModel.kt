package com.example.android.politicalpreparedness.election

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

enum class SAVE_STATUS(val str:String){
    SAVED("unfollow"),
    NOT_SAVED("follow")
}
//replace ElectionDao with ElectionRepository obj
class VoterInfoViewModel(private val dataSource: ElectionDao, private val electionId:Int) : ViewModel() {

    //TODO: Add live data to hold voter info
    private val _savedStatus = MutableLiveData<SAVE_STATUS>(SAVE_STATUS.NOT_SAVED)
    val savedStatus:LiveData<SAVE_STATUS> = _savedStatus

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    private val election = MutableLiveData<Election?>()

    val saveBtnTextState = Transformations.map(election){
        if(it == null){
            "Follow"
        }else{
            "Unfollow"
        }
    }

    init {
        viewModelScope.launch {
            election.value = getElection()
        }
    }

    private suspend fun getElection(): Election? {
        return dataSource.getAnElection(electionId)
    }



    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
//    private fun saveThisElection(){
//        if(_savedStatus.value!=true){
//            viewModelScope.launch {
//                dataSource.saveElection(election)
//            }
//        }
//        _savedStatus.value = true
//    }

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