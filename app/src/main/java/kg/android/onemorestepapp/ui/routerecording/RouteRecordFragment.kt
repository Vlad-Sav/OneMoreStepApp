package kg.android.onemorestepapp.ui.routerecording

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.databinding.FragmentRouteRecordBinding
import kg.android.onemorestepapp.service.foregroundservice.TrackerService
import kg.android.onemorestepapp.ui.routerecording.RouteUtil.calculateElapsedTime
import kg.android.onemorestepapp.ui.routerecording.RouteUtil.calculateTheDistance
import kg.android.onemorestepapp.ui.routerecording.RouteUtil.setCameraPosition
import kg.android.onemorestepapp.utils.Constants.ACTION_SERVICE_START
import kg.android.onemorestepapp.utils.Constants.ACTION_SERVICE_STOP
import kg.android.onemorestepapp.utils.ExtensionFunctions.disable
import kg.android.onemorestepapp.utils.ExtensionFunctions.enable
import kg.android.onemorestepapp.utils.ExtensionFunctions.hide
import kg.android.onemorestepapp.utils.ExtensionFunctions.show
import kg.android.onemorestepapp.utils.Permissions.hasBackgroundLocationPermission
import kg.android.onemorestepapp.utils.Permissions.requestBackgroundLocationPermission
import kg.android.onemorestepapp.viewmodels.ProfileViewModel
import kg.android.onemorestepapp.viewmodels.RouteRecordViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RouteRecordFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, EasyPermissions.PermissionCallbacks {
    private lateinit var viewModel: RouteRecordViewModel

    private var _binding: FragmentRouteRecordBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

    val started = MutableLiveData(false)

    private var isSaveDialogOpened = false
    private var startTime = 0L
    private var stopTime = 0L
    private var markerList = mutableListOf<Marker>()
    private var polylineList = mutableListOf<Polyline>()
    private var locationList = mutableListOf<LatLng>()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteRecordBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(RouteRecordViewModel::class.java)
        binding.tracking = this
        binding.startButton.setOnClickListener { onStartButtonClicked() }
        binding.stopButton.setOnClickListener { onStopButtonClicked() }
        binding.resetButton.setOnClickListener { onResetButtonClicked() }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMyLocationButtonClickListener(this)
        map.isMyLocationEnabled = true
        map.uiSettings.isZoomControlsEnabled = true

        map.uiSettings.isRotateGesturesEnabled = true
        map.uiSettings.isTiltGesturesEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isScrollGesturesEnabled = true
        map.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true
        map.uiSettings.isZoomGesturesEnabled = true

        observeTrackerService()
    }

    private fun onStartButtonClicked(){
        if(hasBackgroundLocationPermission(requireContext())){
            startCountDown()
            binding.startButton.disable()
            binding.startButton.hide()
            binding.stopButton.show()
        } else{
            requestBackgroundLocationPermission(this)
        }
    }

    private fun observeTrackerService(){
        TrackerService.locationList.observe(viewLifecycleOwner, {
            if (it != null) {
                locationList = it
                if(locationList.size > 0){
                    binding.stopButton.enable()
                }
                drawPolyline()
                followPolyline()
            }
        })
        TrackerService.started.observe(viewLifecycleOwner) {
            started.value = it
        }
        TrackerService.startTime.observe(viewLifecycleOwner) {
            startTime = it
        }
        TrackerService.stopTime.observe(viewLifecycleOwner) {
            stopTime = it
            if (stopTime != 0L) {
                if (locationList.isNotEmpty() && !isSaveDialogOpened) {
                    showBiggerPicture()
                    displayResults()
                }
            }
        }
    }

    private fun showBiggerPicture() {
        val bounds = LatLngBounds.Builder()
        for (location in locationList) {
            bounds.include(location)
        }
        map.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(), 100
            ), 2000, null
        )
        addMarker(locationList.first())
        addMarker(locationList.last())
    }

    private fun onResetButtonClicked() {
        mapReset()
    }

    @SuppressLint("MissingPermission")
    private fun  mapReset() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            val lastKnownLocation = LatLng(
                it.result.latitude,
                it.result.longitude
            )
            map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    setCameraPosition(lastKnownLocation)
                )
            )
            for (polyLine in polylineList) {
                polyLine.remove()
            }
            for (marker in markerList) {
                marker.remove()
            }
            locationList.clear()
            markerList.clear()
            binding.resetButton.hide()
            binding.startButton.show()
            drawPolyline()
        }
    }

    private fun addMarker(position: LatLng) {
        val marker = map.addMarker(MarkerOptions().position(position))
        markerList.add(marker!!)
    }

    private fun displayResults() {
        lifecycleScope.launch {
            binding.startButton.apply {
                hide()
                enable()
            }
            delay(2500)
            viewModel.coordinates.postValue(locationList)
            val directions = RouteRecordFragmentDirections.actionRouteRecordFragmentToRouteSaveFragment()
            findNavController().navigate(directions)
            isSaveDialogOpened = true
            binding.stopButton.hide()
            binding.resetButton.show()
        }
    }

    private fun followPolyline(){
        if(locationList.isNotEmpty()){
            map.animateCamera(
                (CameraUpdateFactory.newCameraPosition(
                    setCameraPosition(
                        locationList.last()
                    )
                )), 1000, null
            )
        }
    }

    private fun drawPolyline(){
        val polyline = map.addPolyline(
            PolylineOptions().apply {
                width(10f)
                color(Color.BLUE)
                jointType(JointType.ROUND)
                startCap(ButtCap())
                endCap(ButtCap())
                addAll(locationList)
            }
        )
        polylineList.add(polyline)
    }

    private fun startCountDown() {
        binding.timerTextView.show()
        binding.stopButton.disable()
        val timer: CountDownTimer = object : CountDownTimer(4000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                val currentSecond = millisUntilFinished / 1000
                if(currentSecond.toString() == "0"){
                    binding.timerTextView.text = "GO"
                    binding.timerTextView.setTextColor(
                        ContextCompat.getColor(requireContext(),
                            R.color.black
                        ))
                } else{
                    binding.timerTextView.text = currentSecond.toString()
                    binding.timerTextView.setTextColor(
                        ContextCompat.getColor(requireContext(),
                            R.color.colorPrimary
                        ))
                }
            }
            override fun onFinish() {
                sendActionCommandToService(ACTION_SERVICE_START)
                binding.timerTextView.hide()
            }
        }
        timer.start()
    }

    private fun onStopButtonClicked() {
        stopForegroundService()
        binding.stopButton.hide()
        binding.startButton.show()
    }

    private fun stopForegroundService() {
        binding.startButton.disable()
        sendActionCommandToService(ACTION_SERVICE_STOP)
    }

    private fun sendActionCommandToService(action: String){
        Intent(
            requireContext(),
            TrackerService::class.java
        ).apply {
            this.action = action
            requireContext().startService(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            SettingsDialog.Builder(requireActivity()).build().show()
        }else{
            requestBackgroundLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        onStartButtonClicked()
    }

    override fun onMyLocationButtonClick(): Boolean {
        binding.hintTextView.animate().alpha(0f).duration = 1500
        lifecycleScope.launch {
            delay(2500)
            binding.hintTextView.hide()
            binding.startButton.show()
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}