package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.database.ElectionDao


class VoterInfoViewModel(private val dataSource: ElectionDao) : ViewModel() {

    //TODO: Add live data to hold voter info
    private val _savedStatus = MutableLiveData<Boolean>(false)
    val savedStatus:LiveData<Boolean> = _savedStatus

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs




    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    private fun saveThisElection(){
        //save election to the database

        //updated savedStatus to true
    }

    private fun removeThisElection(){
        //remove election from the database

        //update savedStatus to false
    }
    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}