package com.example.android.poliiicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.launch

class RepresentativeViewModel: ViewModel() {

    //TODO: Establish live data for representatives and address

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    init {
        getRepresentativesFromApi()
    }

    private fun getRepresentativesFromApi(){
        viewModelScope.launch {
            val result = CivicsApi.retrofitService.getRepresentativesInfo("Ampitheatre Parkway 1600 Mountain View California 94043")
            Log.e("RepresentativesViewModel: ", result.officials[0].name)
        }
    }

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

}
