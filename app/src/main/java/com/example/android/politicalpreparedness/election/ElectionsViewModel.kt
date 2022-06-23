package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

enum class ElectionsApiStatus{
    LOADING,ERROR,DONE
}
//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel: ViewModel() {

    private val _elections = MutableLiveData<List<Election>>()
    val elections : LiveData<List<Election>>
    get() = _elections

    //TODO: Create live data val for upcoming elections
    private val _status = MutableLiveData<ElectionsApiStatus>()
    val status : LiveData<ElectionsApiStatus>
    get() = _status

    init {
        getElectionsInfo()
        Log.e("ElectionsViewModel",elections.value?.size.toString())
    }
    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info
    private fun getElectionsInfo(){
        viewModelScope.launch {
            _status.value = ElectionsApiStatus.LOADING
            try {
                 _elections.value = CivicsApi.retrofitService.getElections().elections
                _status.value = ElectionsApiStatus.DONE
            }catch (e: Exception){
                _status.value = ElectionsApiStatus.ERROR
                _elections.value = emptyList()
            }
        }
    }

}