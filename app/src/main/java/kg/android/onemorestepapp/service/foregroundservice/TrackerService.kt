package kg.android.onemorestepapp.service.foregroundservice

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kg.android.onemorestepapp.ui.routerecording.RouteUtil.calculateTheDistance
import kg.android.onemorestepapp.utils.Constants.ACTION_SERVICE_START
import kg.android.onemorestepapp.utils.Constants.ACTION_SERVICE_STOP
import kg.android.onemorestepapp.utils.Constants.LOCATION_FASTEST_UPDATE_INTERVAL
import kg.android.onemorestepapp.utils.Constants.LOCATION_UPDATE_INTERVAL
import kg.android.onemorestepapp.utils.Constants.NOTIFICATION_CHANNEL_ID
import kg.android.onemorestepapp.utils.Constants.NOTIFICATION_CHANNEL_NAME
import kg.android.onemorestepapp.utils.Constants.NOTIFICATION_ID
import javax.inject.Inject

@AndroidEntryPoint
class TrackerService: LifecycleService() {
    @Inject
    lateinit var notification: NotificationCompat.Builder
    @Inject
    lateinit var notificationManager: NotificationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val started = MutableLiveData<Boolean>()
        val startTime = MutableLiveData<Long>()
        val stopTime = MutableLiveData<Long>()

        val locationList = MutableLiveData<MutableList<LatLng>>()
    }

    private fun setInitialValues(){
        started.postValue(false)
        startTime.postValue(0L)
        stopTime.postValue(0L)
        locationList.postValue(mutableListOf())
    }

    private fun updateLocationList(location: Location){
        val newLatLng = LatLng(location.latitude, location.longitude)
        locationList.value?.apply {
            add(newLatLng)
            locationList.postValue(this)
        }
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            result?.locations?.let{ locations ->
                for(location in locations){
                    updateLocationList(location)
                    updateNotificationPeriodically()
                }
            }
        }
    }

    private fun updateNotificationPeriodically() {
        notification.apply {
            setContentTitle("Distance Travelled")
            setContentText(locationList.value?.let { calculateTheDistance(it) } + "km")
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    override fun onCreate() {
        setInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let{
            when(it.action){
                ACTION_SERVICE_START ->{
                    started.postValue(true)
                    startForegroundService()
                    startLocationUpdates()
                }
                ACTION_SERVICE_STOP ->{
                    started.postValue(false)
                    stopForegroundService()
                }
                else -> {

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(){
        val locationRequest = LocationRequest().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = LOCATION_FASTEST_UPDATE_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        startTime.postValue(System.currentTimeMillis())
    }

    private fun startForegroundService(){
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notification.build())
    }

    private fun stopForegroundService() {
        removeLocationUpdates()
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            NOTIFICATION_ID
        )
        stopForeground(true)
        stopSelf()
        stopTime.postValue(System.currentTimeMillis())
    }

    private fun removeLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}