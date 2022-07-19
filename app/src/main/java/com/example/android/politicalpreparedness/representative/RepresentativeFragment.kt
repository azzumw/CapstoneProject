package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.poliiicalpreparedness.representative.RepresentativeViewModel
import com.example.android.poliiicalpreparedness.representative.RepresentativeViewModelFactory
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.LocalDataSource
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.data.RemoteDataSource
import com.example.android.politicalpreparedness.repository.TheRepository
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.databinding.FragmentRep2Binding

class RepresentativeFragment : Fragment() {

    companion object {
        private const val MOTION_LAYOUT_STATE = "Motion_Layout_State"
        private const val TAG = "Representative_Fragment:"
        private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
        private const val REQUEST_PERMISSION_LOCATION = 1
        private const val FINE_LOCATION_KEY = "android.permission.ACCESS_FINE_LOCATION"
        private const val COARSE_LOCATION_KEY = "android.permission.ACCESS_COARSE_LOCATION"
    }


    private lateinit var motionLayout: MotionLayout
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mCurrentLocation: Location? = null

//    private val viewModel: RepresentativeViewModel by viewModels() {
//        RepresentativeViewModelFactory(activity!!.application, TheRepository(
//            LocalDataSource(ElectionDatabase.getInstance(requireContext()).electionDao),
//            RemoteDataSource
//        )
//        )
//    }

    private val viewModel: RepresentativeViewModel by viewModels() {
        RepresentativeViewModelFactory(activity!!.application, TheRepository(
            LocalDataSource(ElectionDatabase.getInstance(requireContext()).electionDao),
           RemoteDataSource
        ),this, Bundle()
        )
    }

    private var _binding : FragmentRep2Binding? = null
    private val binding get() = _binding!!

//    private var _binding: FragmentRepresentativeBinding? = null
//    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_rep_2,container,false)

//        _binding = DataBindingUtil.inflate(
//            layoutInflater,
//            R.layout.fragment_rep_2,
//            container,
//            false
//        )
        motionLayout = binding.motionLayout

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.buttonLocation.setOnClickListener {
            checkLocationPermissions()
        }

//        binding.buttonSearch.setOnClickListener {
//            viewModel.createAddressFromFields()
//        }

        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(binding.root, "Invalid entry", Snackbar.LENGTH_SHORT).show()
                viewModel.doneShowingSnackBar()
            }
        })

        binding.representativeRecycler.adapter = RepresentativeListAdapter()

        viewModel.representatives.observe(viewLifecycleOwner, Observer {
                motionLayout.isInteractionEnabled = it.isNotEmpty()
        })


        if(savedInstanceState!=null){
            motionLayout.transitionState = savedInstanceState.getBundle("key")
        }

        return binding.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle("key",motionLayout.transitionState)
//        outState.putInt(MOTION_LAYOUT_STATE,binding.motionLayout.currentState)
    }


    @SuppressLint("MissingPermission")
    private fun getLocation(locationRequest: LocationRequest) {

        val locationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                Log.e(TAG, "Inside on Location Result")

                val locationList = locationResult.locations
                Log.e(TAG, "Loc List: $locationList")

                Log.e(TAG, "${locationResult.locations}")

                val mostRecentLocation = locationResult.lastLocation
                mCurrentLocation = mostRecentLocation
                Log.e(TAG, "Most Recent Loc: $mostRecentLocation")
                Log.e(TAG, "mCurrentLocation: $mCurrentLocation")
                fusedLocationClient.removeLocationUpdates(this)

                viewModel.useMyLocation(mCurrentLocation!!)
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )

//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { pLocation ->
//                if (pLocation != null) {
//                    mCurrentLocation = pLocation
//                    Toast.makeText(
//                        context,
//                        "Location: Lat: ${mCurrentLocation!!.latitude}, Long:${mCurrentLocation!!.longitude}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                   viewModel.useMyLocation(mCurrentLocation!!)
////                    Log.e("Address:","${address.city}")
//
//
//                } else {
//                    //force to get new location
//                    fusedLocationClient.requestLocationUpdates(
//                        locationRequest,
//                        locationCallback,
//                        Looper.getMainLooper()
//                    )
//                }
//            }

    }


//    private fun geoCodeLocation(location: Location): Address {
//        val geocoder = Geocoder(context, Locale.getDefault())
//        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
//            .map { address ->
//                Address(
//                    address.thoroughfare,
//                    address.subThoroughfare,
//                    address.locality,
//                    address.adminArea,
//                    address.postalCode
//                )
//            }
//            .first()
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_LOCATION -> {
                var canProceed = false
                if ((grantResults.isNotEmpty() && (grantResults[0] == PERMISSION_GRANTED || grantResults[1] == PERMISSION_GRANTED))) {

                    permissions.forEachIndexed { index, s ->
                        when {
                            s == FINE_LOCATION_KEY && grantResults[index] == PERMISSION_GRANTED -> {
                                canProceed = true
                            }
                            s == COARSE_LOCATION_KEY && grantResults[index] == PERMISSION_GRANTED -> {
                                canProceed = true
                            }
                        }
                    }
                    if (canProceed) {
                        //continue...
                        checkLocationPermissions()
                    }
                } else {
                    Snackbar.make(
                        binding.root,
                        "Location permission must be granted to use this feature.",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
                return
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            checkDeviceLocationSettings()
            true

        } else {

            val resultFine =
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)

            val resultCoarse =
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)

            if (resultFine || resultCoarse) {
                Snackbar.make(
                    binding.root,
                    "To use this feature, location permissions must be granted.",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                //the response from here goes to onRequestPermissionsResult
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    REQUEST_PERMISSION_LOCATION
                )
            }
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        //GRANTED == 0
        //DENIED == -1
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PERMISSION_GRANTED

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_TURN_DEVICE_LOCATION_ON -> {
                Log.e(TAG, "onActivityResult - REQ_DEV_LOC")
                checkDeviceLocationSettings(false)
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun checkDeviceLocationSettings(resolve: Boolean = true) {
        val locationRequest = createLocationRequest()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(requireActivity())

        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnSuccessListener {
            Toast.makeText(context, "Device Location ON!", Toast.LENGTH_SHORT).show()
            //continue
            getLocation(locationRequest)

        }

        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                try {
                    Log.e("LocationSettingsResponseOnFailure", "I am here")
                    startIntentSenderForResult(
                        exception.resolution.intentSender,
                        REQUEST_TURN_DEVICE_LOCATION_ON, null, 0, 0, 0, null
                    )

                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(
                        TAG,
                        "Error getting location settings resolution: " + sendEx.message
                    )
                }
            } else {
                Snackbar.make(
                    binding.root,
                    "Device location must be on to use this feature", Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createLocationRequest(): LocationRequest {

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000L
//            fastestInterval
        }
        return locationRequest
    }


    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}