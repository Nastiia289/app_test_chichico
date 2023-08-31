package com.example.test.general.chang_location_activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.test.R
import com.example.test.general.restauration_activity.RestorationActivity
import com.example.test.databinding.ActivityChangLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.TravelMode
class ChangLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityChangLocationBinding
    private lateinit var exit: ImageView
    private lateinit var setUserWay: AppCompatButton
    private lateinit var map: SupportMapFragment
    private var isPermissionGranted = false
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLocation: LatLng? = null
    private lateinit var viewModel: ChangLocationActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ChangLocationActivityViewModel::class.java]

        exit = binding.imageView
        map = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        setUserWay = binding.setWay

        setUserWay.setOnClickListener {
            viewModel.fetchUserLocation()
        }

        viewModel.drawnRoute.observe(this) { polylineOptions ->
            mMap.addPolyline(polylineOptions)
        }

        viewModel.userLocation.observe(this) { location ->
            userLocation = location
            updateUserMarker()
        }
        exit.setOnClickListener{
            viewModel.onExitClick()
        }
        viewModel.exit.observe(this){
            val intent = Intent(this@ChangLocation, RestorationActivity::class.java)
            startActivity(intent)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        map.getMapAsync(this)
    }

    private fun updateUserMarker() {
        userLocation?.let { location ->
            mMap.clear()
            val markerOptions = MarkerOptions().position(location).title("Your position")
            mMap.addMarker(markerOptions)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latLng = LatLng(50.450001, 30.523333)
        val markerOptions = MarkerOptions().position(latLng).title("Your position")
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        // Add multiple points and draw routes
        val points = listOf(
            LatLng(50.451, 30.524),  // Point 1
            LatLng(50.452, 30.525),  // Point 2
            LatLng(50.453, 30.526)   // Point 3
        )

        for (point in points) {
            mMap.addMarker(MarkerOptions().position(point).title("Point"))
        }
        drawRoutesBetweenPoints(points)
    }
    private fun drawRoutesBetweenPoints(points: List<LatLng>) {
        val context = GeoApiContext.Builder()
            .apiKey("AIzaSyDW42dyZ98vB5f4g5FytC0wR412X4cPb7M")
            .build()

        val waypoints = points.subList(1, points.size - 1).map {
            "${it.latitude},${it.longitude}"
        }.toTypedArray()

        val directionsApiRequest = DirectionsApi.newRequest(context)
            .origin("${points.first().latitude},${points.first().longitude}")
            .destination("${points.last().latitude},${points.last().longitude}")
            .waypoints(*waypoints) // Use the spread operator to unpack the array
            .mode(TravelMode.DRIVING) // You can change the travel mode here

        try {
            val directionsResult = directionsApiRequest.await()
            val routes = directionsResult.routes

            for (route in routes) {
                val encodedPath = route.overviewPolyline.encodedPath
                val decodedPath = PolylineOptions().addAll(com.google.maps.android.PolyUtil.decode(encodedPath))
                mMap.addPolyline(decodedPath)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onStart() {
        super.onStart()
        map.onStart()
    }
    override fun onResume() {
        super.onResume()
        map.onResume()
    }
    override fun onPause() {
        super.onPause()
        map.onPause()
    }
    override fun onStop() {
        super.onStop()
        map.onStop()
    }
    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        map.onSaveInstanceState(outState)
    }
    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }
}


