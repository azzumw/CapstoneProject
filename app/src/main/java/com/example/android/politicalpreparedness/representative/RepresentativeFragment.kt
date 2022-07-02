package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.poliiicalpreparedness.representative.RepresentativeViewModel
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class DetailFragment : Fragment() {

    companion object {
        //TODO: Add Constant for Location request
        private const val REQUEST_PERMISSION_LOCATION = 1
    }

    //TODO: Declare ViewModel
    private val viewModel: RepresentativeViewModel by viewModels()

    private var _binding: FragmentRepresentativeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO: Establish bindings
        _binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_representative,
            container,
            false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //TODO: Define and assign Representative adapter

        //TODO: Populate Representative adapter
        binding.buttonLocation.setOnClickListener {
//            Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show()
            checkLocationPermissions()
        }


        //TODO: Establish button listeners for field and location search
        return binding.root

    }


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
                            s == "android.permission.ACCESS_FINE_LOCATION" && grantResults[index] == PERMISSION_GRANTED -> {
                                canProceed = true
                            }
                            s == "android.permission.ACCESS_COARSE_LOCATION" && grantResults[index] == PERMISSION_GRANTED -> {
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
            Toast.makeText(context, "GRANTED", Toast.LENGTH_SHORT).show()
            //continue...
            getLocation()
            true

        } else {
            Toast.makeText(context, "DENIED", Toast.LENGTH_SHORT).show()

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

    private fun getLocation() {
        //TODO: Get location from LocationServices
        Toast.makeText(context, "getLocation", Toast.LENGTH_SHORT).show()
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
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