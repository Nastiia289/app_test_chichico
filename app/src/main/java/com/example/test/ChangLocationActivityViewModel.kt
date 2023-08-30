package com.example.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.TravelMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangLocationActivityViewModel : ViewModel() {

    val exit: MutableLiveData<Boolean>  by lazy {
        MutableLiveData<Boolean>()
    }

    private val _drawnRoute: MutableLiveData<PolylineOptions> = MutableLiveData()
    val drawnRoute: LiveData<PolylineOptions>
        get() = _drawnRoute

    private val _userLocation: MutableLiveData<LatLng> = MutableLiveData()
    val userLocation: LiveData<LatLng>
        get() = _userLocation

    public  fun onExitClick(){
        exit.postValue(true)
    }

    fun fetchUserLocation() {
        val userLocation = LatLng(50.450001, 30.523333)
        _userLocation.postValue(userLocation)
    }

    fun drawRouteBetweenPoints(points: List<LatLng>) {
        val context = GeoApiContext.Builder()
            .apiKey("AIzaSyDW42dyZ98vB5f4g5FytC0wR412X4cPb7M")
            .build()

        val waypoints = points.subList(1, points.size - 1).map {
            "${it.latitude},${it.longitude}"
        }.toTypedArray()

        val directionsApiRequest = DirectionsApi.newRequest(context)
            .origin("${points.first().latitude},${points.first().longitude}")
            .destination("${points.last().latitude},${points.last().longitude}")
            .waypoints(*waypoints)
            .mode(TravelMode.DRIVING)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val directionsResult = directionsApiRequest.await()
                val routes = directionsResult.routes

                for (route in routes) {
                    val encodedPath = route.overviewPolyline.encodedPath
                    val decodedPath = PolylineOptions().addAll(com.google.maps.android.PolyUtil.decode(encodedPath))
                    withContext(Dispatchers.Main) {
                        _drawnRoute.value = decodedPath
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
