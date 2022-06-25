package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

enum class ElectionsApiStatus {
    LOADING, ERROR, DONE
}

class ElectionsViewModel(val database: ElectionDao) : ViewModel() {

    //TODO: Create live data val for upcoming elections
//    private val _elections = MutableLiveData<List<Election>>()
//    val elections : LiveData<List<Election>>
//    get() = _elections

    val electionsFromDataBase = database.getAllElections()

    private val _status = MutableLiveData<ElectionsApiStatus>()
    val status: LiveData<ElectionsApiStatus>
        get() = _status

    private val _navToSingleElectionVoterInfo = MutableLiveData<Election>()
    val navToSingleElectionVoterInfo: LiveData<Election>
        get() = _navToSingleElectionVoterInfo

    init {
        getElectionsInfo()
    }
    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database


    private fun getElectionsInfo() {

        viewModelScope.launch {
            _status.value = ElectionsApiStatus.LOADING
            try {
                val electionsFromApi = CivicsApi.retrofitService.getElections().elections
                database.insertAllElections(electionsFromApi)
                _status.value = ElectionsApiStatus.DONE

            } catch (e: Exception) {
                _status.value = ElectionsApiStatus.ERROR

            }
        }
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info
    fun displayElectionVoterInfo(singleElectionInfo: Election) {
        _navToSingleElectionVoterInfo.value = singleElectionInfo
    }

    //done navigating
    fun onNavComplete() {
        _navToSingleElectionVoterInfo.value = null
    }

}