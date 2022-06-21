package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel: ViewModel() {

    //TODO: Create live data val for upcoming elections
    private val _status = MutableLiveData<String>()
    val status : LiveData<String>
    get() = _status

    init {
        getElectionsInfo()
    }
    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info
    fun getElectionsInfo(){
        viewModelScope.launch {
            try {
                val listResult = CivicsApi.retrofitService.getElections()
                _status.value = "Success ${listResult.elections.size} elections available"
            }catch (e: Exception){
                _status.value = "Failure: ${e.message}"
            }
        }
    }

}