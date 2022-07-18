package com.example.android.poliiicalpreparedness.representative

import android.app.Application
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.savedstate.SavedStateRegistryOwner
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.ApiStatus
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.TheRepository
import com.example.android.politicalpreparedness.representative.adapter.apiStatus
import com.example.android.politicalpreparedness.representative.model.Representative
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*


class RepresentativeViewModel(private val savedStateHandle: SavedStateHandle,
                              val app: Application,
                              private val repository: TheRepository) :
    ViewModel() {
    private val KEY = "saved_data"
    private val KEY_LIST = "list_data"

    private val _showSnackBarEvent = MutableLiveData<Boolean>(false)
    val showSnackBarEvent: LiveData<Boolean> = _showSnackBarEvent

    private val _representatives : MutableLiveData<List<Representative>> = savedStateHandle.getLiveData(KEY_LIST,
        emptyList())
    val representatives: LiveData<List<Representative>> get() = _representatives

    val textViewVisbility = Transformations.map(representatives){
        if(it.isEmpty()){
            View.VISIBLE
        }else{
            View.GONE
        }
    }

    private val _address: MutableLiveData<Address> = savedStateHandle.getLiveData(KEY)
        val address: LiveData<Address>
        get() = _address


//    private val addressObserver = Observer<Address> { address ->
//        _address.value = address
//        updateAddressFields()
//        findMyRepresentatives()
//    }


    val selectedItem = MutableLiveData<Int>()
    val line1 = MutableLiveData<String>("")
    val line2 = MutableLiveData<String>("")
    val city = MutableLiveData<String>("")
    val state = MediatorLiveData<String>()
    val zip = MutableLiveData<String>("")

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus> = _status


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
         _status.value = ApiStatus.DONE
        _representatives.value = emptyList()

//        _address.value = savedStateHandle
//            .getLiveData<Address>(KEY).value
        //            .observeForever(addressObserver)

        if(address.value!=null){
            updateAddressFields()
            findMyRepresentatives()
        }

    }


    private fun getRepresentativesFromApi(address: Address) {
        //"Ampitheatre Parkway 1600 Mountain View California 94043"
        viewModelScope.launch {
            try{

                _status.value = ApiStatus.LOADING

                val result = repository.callRepresentativeInfoApi(address)

                _status.value = ApiStatus.DONE

                val officials = result.officials
                val offices = result.offices

                val rList = mutableListOf<Representative>()

                offices.forEach {
                    val representative = it.getRepresentatives(officials)
                    rList.addAll(representative)
                    Log.e("REPVIEWM ", "${representative.size}")
                }

                _representatives.value = rList
                savedStateHandle.set(KEY_LIST,_representatives.value)

                Log.e("RepresentativesViewModel: ", result.officials[0].name)
                Log.e("RepresentativesViewModel: Officials: ", result.officials.size.toString())
                Log.e("RepresentativesViewModel: Offices: ", result.offices.size.toString())
            }catch (e:Exception){
                _status.value = ApiStatus.ERROR
                _representatives.value = emptyList()
            }
        }
    }


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

//        savedStateHandle[KEY] = _address.value
        updateAddressFields()
    }

    private fun updateAddressFields() {
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
        savedStateHandle.set(KEY,_address.value)
        getRepresentativesFromApi(address.value!!)
        // Save the Address once available
    }


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

    override fun onCleared() {
        super.onCleared()
//        savedStateHandle
//            .getLiveData<Address>(KEY)
//            .removeObserver(addressObserver)
    }
}

class RepresentativeViewModelFactory(
    val app: Application, val repository: TheRepository,
    owner: SavedStateRegistryOwner, defaultArgs: Bundle?
) :
    AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            return RepresentativeViewModel(handle, app, repository) as T
        }
        throw IllegalArgumentException("Not a viewmodel")
    }
}
