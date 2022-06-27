package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionAndSavedElection
import com.example.android.politicalpreparedness.network.models.SavedElection
import com.example.android.politicalpreparedness.repository.TheRepository
import kotlinx.coroutines.launch

enum class ElectionsApiStatus {
    LOADING, ERROR, DONE
}

class ElectionsViewModel(private val repository: TheRepository) : ViewModel() {

    val electionsFromDataBase = repository.elections

    val savedElections:LiveData<List<ElectionAndSavedElection>> = repository.savedElections


    private val _status = MutableLiveData<ElectionsApiStatus>()
    val status: LiveData<ElectionsApiStatus>
        get() = _status

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage : LiveData<String> get() = _statusMessage

    private val _navToSingleElectionVoterInfo = MutableLiveData<Election?>()
    val navToSingleElectionVoterInfo: LiveData<Election?>
        get() = _navToSingleElectionVoterInfo

    init {
        getElectionsInfo()
    }

    private fun getElectionsInfo() {

        viewModelScope.launch {
            _status.value = ElectionsApiStatus.LOADING
            try {

                repository.getElections()
                _status.value = ElectionsApiStatus.DONE

            } catch (e: Exception) {
                _status.value = ElectionsApiStatus.ERROR
                _statusMessage.value = e.message
            }
        }
    }

    fun displayElectionVoterInfo(singleElectionInfo: Election) {
        _navToSingleElectionVoterInfo.value = singleElectionInfo
    }

    //done navigating
    fun onNavComplete() {
        _navToSingleElectionVoterInfo.value = null
    }

}