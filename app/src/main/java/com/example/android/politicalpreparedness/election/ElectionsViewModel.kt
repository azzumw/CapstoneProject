package com.example.android.politicalpreparedness.election

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionAndSavedElection
import com.example.android.politicalpreparedness.repository.TheRepository
import kotlinx.coroutines.launch

enum class ElectionsApiStatus {
    LOADING, ERROR, DONE
}

class ElectionsViewModel(private val repository: TheRepository) : ViewModel() {

    val allElections = repository.elections

    val savedElections:LiveData<List<ElectionAndSavedElection>> = repository.savedElections


    private val _status = MutableLiveData<ElectionsApiStatus>()
    val status: LiveData<ElectionsApiStatus>
        get() = _status

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage : LiveData<String> get() = _statusMessage

    private val _navToSingleElectionVoterInfo = MutableLiveData<ElectionAndSavedElection?>()
    val navToSingleElectionVoterInfo: LiveData<ElectionAndSavedElection?>
        get() = _navToSingleElectionVoterInfo

    private var filter: MutableLiveData<Int> = MutableLiveData()
    val filteredElections: LiveData<List<ElectionAndSavedElection>> = filter.switchMap {
        when (it) {
            1 -> {
                allElections
            }
            2 -> {
                savedElections
            }
            else -> {
                allElections
            }
        }
    }


    init {
        getElectionsInfo()
    }

    fun selectFilter(selectedFilter: Int) {
        filter.value = selectedFilter
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

    fun displayElectionVoterInfo(singleElectionInfo: ElectionAndSavedElection) {
        _navToSingleElectionVoterInfo.value = singleElectionInfo
    }

    //done navigating
    fun onNavComplete() {
        _navToSingleElectionVoterInfo.value = null
    }

}