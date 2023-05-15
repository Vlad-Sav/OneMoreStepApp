package kg.android.onemorestepapp.ui.routedetails

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.databinding.FragmentRouteDetailsBinding
import kg.android.onemorestepapp.databinding.FragmentRouteRecordBinding
import kg.android.onemorestepapp.models.responses.RoutesResponse
import kg.android.onemorestepapp.utils.ExtensionFunctions.hide
import kg.android.onemorestepapp.utils.ExtensionFunctions.show
import kg.android.onemorestepapp.viewmodels.RouteRecordViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RouteDetailsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    private var _binding: FragmentRouteDetailsBinding? = null
    private val binding get() = _binding!!

    private var markerList = mutableListOf<Marker>()
    private var polylineList = mutableListOf<Polyline>()
    private lateinit var map: GoogleMap

    private var routeData: RoutesResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRouteDetailsBinding.inflate(inflater, container, false)
        val bundle = arguments ?: return binding.root
        val args = RouteDetailsFragmentArgs.fromBundle(bundle)
        routeData = args.routeData
        binding.routeDescriptionTv.text = routeData?.description
        binding.routeTitleTv.text = routeData?.title
        binding.userNameTv.text = routeData?.userName
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_details) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMyLocationButtonClickListener(this)
        map.isMyLocationEnabled = true

        map.uiSettings.isRotateGesturesEnabled = true
        map.uiSettings.isTiltGesturesEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isScrollGesturesEnabled = true
        map.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true
        map.uiSettings.isZoomGesturesEnabled = true

        if (routeData?.locationList?.isNotEmpty() == true) {
            showBiggerPicture()
            drawPolyline()
        }
    }

    private fun showBiggerPicture() {
        val bounds = LatLngBounds.Builder()
        for (location in routeData?.locationList ?: emptyList()) {
            bounds.include(location)
        }
        map.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(), 100
            ), 2000, null
        )
    }

    private fun addMarker(position: LatLng) {
        val marker = map.addMarker(MarkerOptions().position(position))
        markerList.add(marker!!)
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    private fun drawPolyline(){
        val polyline = map.addPolyline(
            PolylineOptions().apply {
                width(10f)
                color(Color.BLUE)
                jointType(JointType.ROUND)
                startCap(ButtCap())
                endCap(ButtCap())
                addAll(routeData?.locationList ?: emptyList())
            }
        )
        polylineList.add(polyline)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}