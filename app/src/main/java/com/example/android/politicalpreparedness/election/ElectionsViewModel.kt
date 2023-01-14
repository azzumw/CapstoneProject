package com.example.android.politicalpreparedness.election

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.RepositoryInterface
import kotlinx.coroutines.launch



enum class ApiStatus {
    LOADING, ERROR, DONE
}

class ElectionsViewModel(private val repository: RepositoryInterface) : ViewModel() {

    private val allElections = repository.getElectionsFromLocalDataBase()

    private val savedElections = repository.getSavedElectionsFromLocalDataSource()

    var filter: MutableLiveData<Int> = MutableLiveData()

    val filteredElections: LiveData<List<Election>> = filter.switchMap { filter ->
        when (filter) {
            1 -> {
                allElections
            }
            2 -> {
                savedElections.map { it ->
                    it.map {
                        it.election
                    }
                }
            }
            else -> {
                allElections
            }
        }
    }

    fun selectFilter(selectedFilter: Int) {
        filter.value = selectedFilter
    }

    private val _showSnackBarEvent = MutableLiveData<Boolean>(false)
    val showSnackBarEvent: LiveData<Boolean> = _showSnackBarEvent

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> get() = _statusMessage

    private val _showNoDataInDatabaseMessage = MutableLiveData<Boolean>(false)
    val showNoDataInDatabaseMessage: LiveData<Boolean> get() = _showNoDataInDatabaseMessage

    private val _navToSingleElectionVoterInfo = MutableLiveData<Election?>()
    val navToSingleElectionVoterInfo: LiveData<Election?>
        get() = _navToSingleElectionVoterInfo


    init {
        getElectionsInfo()
        selectFilter(1)

    }


    private fun getElectionsInfo() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {

                repository.getElections()
                _status.value = ApiStatus.DONE

            } catch (e: Exception) {
                _showSnackBarEvent.value = true
                _status.value = ApiStatus.ERROR
                _statusMessage.value = e.message
            }
        }
    }

    fun displayElectionVoterInfo(singleElectionInfo: Election) {
        _navToSingleElectionVoterInfo.value = singleElectionInfo
    }

    fun onNavComplete() {
        _navToSingleElectionVoterInfo.value = null
    }

    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }
}