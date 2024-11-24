package com.example.schedulemaster.viewmodel

import com.example.schedulemaster.model.Location
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.schedulemaster.model.Category
import com.example.schedulemaster.model.Priority
import com.example.schedulemaster.model.Task
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException
import java.util.*

class AddTaskViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)

    private val _location = MutableLiveData<Location?>()
    val location: LiveData<Location?> get() = _location

    fun getCurrentLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                loc?.let {
                    _location.value = Location(
                        it.latitude,
                        it.longitude,
                        "Current Location"
                    )
                } ?: run {
                    _location.value = null
                }
            }.addOnFailureListener {
                _location.value = null
            }
        } else {
            _location.value = null
        }
    }

    fun geocodeAddress(context: Context, address: String): Location? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addressList: MutableList<Address>? = geocoder.getFromLocationName(address, 1)
            if (!addressList.isNullOrEmpty()) {
                val latLngAddress = addressList[0]
                val latitude = latLngAddress.latitude
                val longitude = latLngAddress.longitude
                Location(latitude, longitude, address)
            } else {
                null
            }
        } catch (e: IOException) {
            null
        }
    }

    fun submitTask(title: String, date: String, time: String, description: String, location: Location, priority: Priority, category: Category) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val task = Task(title, date, time, description, location, priority, category)
            databaseRef.child("users").child(userId).child("tasks").push().setValue(task)
        }
    }
}
