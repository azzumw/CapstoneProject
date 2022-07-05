package com.example.android.poliiicalpreparedness.representative

import android.app.Application
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.*

class RepresentativeViewModel(val app: Application) : ViewModel() {

    private val _showSnackBarEvent = MutableLiveData<Boolean>(false)
    val showSnackBarEvent: LiveData<Boolean> = _showSnackBarEvent

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives : LiveData<List<Representative>> get() = _representatives

    //TODO: Establish live data for representatives and address
    val _address = MutableLiveData<Address>()

    val selectedItem = MutableLiveData<Int>()
    val line1 = MutableLiveData<String>("")
    val line2 = MutableLiveData<String>("")
    val city = MutableLiveData<String>("")
    val state = MediatorLiveData<String>()
    val zip = MutableLiveData<String>("")


    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */
    init {

        state.addSource(selectedItem) {
            state.value = app.resources.getStringArray(R.array.states)[it]
        }
    }


    private fun getRepresentativesFromApi(address: Address) {
        //"Ampitheatre Parkway 1600 Mountain View California 94043"
        viewModelScope.launch {
            val result = CivicsApi.retrofitService.getRepresentativesInfo(address)
            val officials = result.officials
            val offices = result.offices
            val rList = mutableListOf<Representative>()
            offices.forEach {
                val representative = it.getRepresentatives(officials)
                rList.addAll(representative)
                Log.e("REPVIEWM ", "${representative.size}")
            }

            _representatives.value = rList

            Log.e("RepresentativesViewModel: ", result.officials[0].name)
            Log.e("RepresentativesViewModel: Officials: ", result.officials.size.toString())
            Log.e("RepresentativesViewModel: Offices: ", result.offices.size.toString())
        }
    }

    //TODO: Create function get address from geo location

    private fun geoCodeLocation(location: Location) {
        val geocoder = Geocoder(app, Locale.getDefault())
        _address.value = geocoder.getFromLocation(location.latitude, location.longitude, 1)
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

        updateAddressFields()
    }

    private fun updateAddressFields(){
        line1.value = _address.value!!.line1
        line2.value = _address.value!!.line2!!
        city.value = _address.value!!.city
        state.value = _address.value!!.state
        zip.value = _address.value!!.zip
    }

    fun useMyLocation(location: Location) {
        geoCodeLocation(location)
        findMyRepresentatives()
    }


    fun findMyRepresentatives() {
//        Log.e("RepresentativeViewMode:", address.toFormattedString())
            getRepresentativesFromApi(_address.value!!)

    }

//    private fun getAddress(): Address = Address(
//        line1.value!!,
//        line2.value,
//        city.value!!,
//        state.value!!,
//        zip.value!!
//    )


    //TODO: Create function to get address from individual fields
    fun createAddressFromFields() {
        if (isNotValidEntry()) {
            _showSnackBarEvent.value = true

        } else {
            _address.value = Address(
                line1.value!!,
                line2.value,
                city.value!!,
                state.value!!,
                zip.value!!
            )
            findMyRepresentatives()
        }

    }

    private fun isNotValidEntry(): Boolean {
        return line1.value.isNullOrBlank() ||
                city.value.isNullOrBlank() ||
                zip.value.isNullOrBlank()
    }

    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }
}


class RepresentativeViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            return RepresentativeViewModel(app) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}
