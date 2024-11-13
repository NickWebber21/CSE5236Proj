package com.example.schedulemaster.ui.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.schedulemaster.R
import com.example.schedulemaster.model.Category
import com.example.schedulemaster.model.Location
import com.example.schedulemaster.model.Priority
import com.example.schedulemaster.model.Task
import com.example.schedulemaster.ui.activity.HomeActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException
import java.util.*

class AddTaskFragment : Fragment(), View.OnClickListener {

    private lateinit var titleInput: EditText
    private lateinit var dateInput: EditText
    private lateinit var timeInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var locationInput: EditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var categorySpinner: Spinner
    private lateinit var submitButton: Button
    private lateinit var homeButton: Button
    private lateinit var databaseRef: DatabaseReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private lateinit var currentLocation: Location

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)

        // Initialize Firebase ref and location services
        databaseRef = FirebaseDatabase.getInstance().reference
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        requestLocationPermission()

        // Bind views
        titleInput = view.findViewById(R.id.TitleInput)
        dateInput = view.findViewById(R.id.editTextDate)
        timeInput = view.findViewById(R.id.editTextTime)
        descriptionInput = view.findViewById(R.id.editTextDescription)
        locationInput = view.findViewById(R.id.editTextLocation)
        prioritySpinner = view.findViewById(R.id.prioritySpinner)
        categorySpinner = view.findViewById(R.id.categorySpinner)
        submitButton = view.findViewById(R.id.submitButton)
        homeButton = view.findViewById(R.id.HomeButton)

        // Set up onClick listeners for buttons
        submitButton.setOnClickListener(this)
        homeButton.setOnClickListener(this)

        // Set up DatePicker for date input
        dateInput.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up TimePicker for time input
        timeInput.setOnClickListener {
            showTimePickerDialog()
        }

        // Set up the adapter for the priority spinner
        val priorityAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priority_options,
            android.R.layout.simple_spinner_item
        )
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = priorityAdapter

        // Set up the adapter for category spinner
        val categoryAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_options,
            android.R.layout.simple_spinner_item,
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        // Set default location text to "Current Address"
        locationInput.setText("Current Address")

        // Get and store the actual current location details
        getCurrentLocation { location ->
            currentLocation = location
        }

        return view
    }

    // Override onClick -> Set Functions
    override fun onClick(v: View) {
        when (v.id) {
            R.id.submitButton -> submitTask()
            R.id.HomeButton -> navigateToHome()
        }
    }

    private fun submitTask() {
        // Get the currently logged-in user
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val title = titleInput.text.toString().trim()
            val date = dateInput.text.toString().trim()
            val time = timeInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            val locationText = locationInput.text.toString().trim()

            if (title.isEmpty() || date.isEmpty() || time.isEmpty() || description.isEmpty() || locationText.isEmpty()) {
                Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show()
                return
            }

            val priorityString = prioritySpinner.selectedItem.toString()
            val priority = when (priorityString) {
                "Low" -> Priority.LOW
                "Medium" -> Priority.MEDIUM
                "High" -> Priority.HIGH
                else -> Priority.LOW
            }

            val categoryString = categorySpinner.selectedItem.toString()
            val category = when (categoryString) {
                "Work" -> Category.WORK
                "Personal" -> Category.PERSONAL
                "Study" -> Category.STUDY
                "Fitness" -> Category.FITNESS
                "Other" -> Category.OTHER
                else -> Category.OTHER
            }

            // Use currentLocation regardless of locationInput field for actual data
            createTask(userId, title, date, time, description, currentLocation, priority, category)
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createTask(userId: String, title: String, date: String, time: String, description: String, location: Location, priority: Priority, category: Category) {
        val task = Task(title, date, time, description, location, priority, category)

        // Push the task to Firebase Realtime Database under the user's node with a unique ID
        databaseRef.child("users").child(userId).child("tasks").push().setValue(task)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Task added successfully", Toast.LENGTH_SHORT).show()
                // Navigate to the home page after task is added
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to add task: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getCurrentLocation(callback: (Location) -> Unit) {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val addressList: List<Address>? = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                    val address = if (!addressList.isNullOrEmpty()) {
                        addressList[0].getAddressLine(0)  // Full address line
                    } else {
                        "Address not available"
                    }

                    val location = Location(loc.latitude, loc.longitude, address)
                    currentLocation = location // Store for use in database
                    callback(location)
                } else {
                    callback(Location(0.0, 0.0, "Location unavailable"))
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(requireContext(), "Location permission is required to access your current location", Toast.LENGTH_SHORT).show()
        }
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission granted, retrieving location...", Toast.LENGTH_SHORT).show()
                getCurrentLocation { location -> }
            } else {
                Toast.makeText(requireContext(), "Permission denied, unable to get location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dateInput.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timeInput.setText(formattedTime)
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }

    private fun navigateToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
    }
}
