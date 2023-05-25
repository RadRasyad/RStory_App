package com.danrsy.rstoryapp.ui.story.maps

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.danrsy.rstoryapp.R
import com.danrsy.rstoryapp.data.local.auth.UserPreference
import com.danrsy.rstoryapp.databinding.FragmentMapsBinding
import com.danrsy.rstoryapp.utils.Resource
import com.danrsy.rstoryapp.utils.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding
    private var authToken: String? = null
    private val viewModel: MapsViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        userPreference = UserPreference(requireActivity())
        authToken = userPreference.getUser().token

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if(ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if(location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.warning_active_location),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun markStoryLocation() {
        authToken?.let {
            viewModel.getStoryLocation("Bearer $it").observe(requireActivity()) { it ->
                when (it) {
                    is Resource.Success -> {
                        it.data?.listStory?.forEach {
                            if (it.lat != null && it.lon != null) {
                                val latLng = LatLng(it.lat, it.lon)
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title(it.name)
                                        .snippet("Lat : ${it.lat}, Lon : ${it.lon}")
                                )
                            }
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isIndoorLevelPickerEnabled = true
            isMapToolbarEnabled = true
        }

        getMyLocation()
        markStoryLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}