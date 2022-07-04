package com.example.android.poliiicalpreparedness.representative

import android.app.Application
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.*

class RepresentativeViewModel(val app:Application): ViewModel() {

    //TODO: Establish live data for representatives and address
    private val _address = MutableLiveData<Address>()
    val address : LiveData<Address> get() = _address

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */


    private fun getRepresentativesFromApi(address: Address){
        //"Ampitheatre Parkway 1600 Mountain View California 94043"
        viewModelScope.launch {
            val result = CivicsApi.retrofitService.getRepresentativesInfo(address)
            Log.e("RepresentativesViewModel: ", result.officials[0].name)
        }
    }

    //TODO: Create function get address from geo location



     private fun geoCodeLocation(location: Location) :Address?{
        val geocoder = Geocoder(app, Locale.getDefault())
        _address.value =  geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()

         return address.value
    }

    fun useMyLocation(location: Location){
        val add = geoCodeLocation(location)
        findMyRepresentatives(add!!)
    }


    private fun findMyRepresentatives(address: Address){
        Log.e("RepresentativeViewMode:" , address.toFormattedString())
        getRepresentativesFromApi(address)
    }




    //TODO: Create function to get address from individual fields
    fun createAddressFromFields():Address?{
        return null
    }

}


class RepresentativeViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RepresentativeViewModel::class.java)){
            return RepresentativeViewModel(app) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }

}
