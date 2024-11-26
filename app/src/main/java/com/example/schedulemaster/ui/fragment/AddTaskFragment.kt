package com.example.schedulemaster.ui.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.schedulemaster.R
import com.example.schedulemaster.model.Category
import com.example.schedulemaster.model.Priority
import com.example.schedulemaster.ui.activity.CalendarActivity
import com.example.schedulemaster.viewmodel.AddTaskViewModel

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
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private lateinit var taskViewModel: AddTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)

        taskViewModel = ViewModelProvider(this).get(AddTaskViewModel::class.java)

        titleInput = view.findViewById(R.id.TitleInput)
        dateInput = view.findViewById(R.id.editTextDate)
        timeInput = view.findViewById(R.id.editTextTime)
        descriptionInput = view.findViewById(R.id.editTextDescription)
        locationInput = view.findViewById(R.id.editTextLocation)
        prioritySpinner = view.findViewById(R.id.prioritySpinner)
        categorySpinner = view.findViewById(R.id.categorySpinner)
        submitButton = view.findViewById(R.id.submitButton)

        submitButton.setOnClickListener(this)

        dateInput.setOnClickListener { showDatePickerDialog() }
        timeInput.setOnClickListener { showTimePickerDialog() }

        setupSpinners()

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            taskViewModel.getCurrentLocation(requireContext())
        } else {
            requestLocationPermission()
        }

        taskViewModel.location.observe(viewLifecycleOwner, Observer { location ->
            location?.let {
                locationInput.setText("Lat: ${it.latitude}, Lon: ${it.longitude}")
            }
        })

        return view
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.submitButton -> submitTask()
        }
    }

    private fun submitTask() {
        val title = titleInput.text.toString().trim()
        val date = dateInput.text.toString().trim()
        val time = timeInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val locationText = locationInput.text.toString().trim()

        val priority = when (prioritySpinner.selectedItem.toString()) {
            "Low" -> Priority.LOW
            "Medium" -> Priority.MEDIUM
            "High" -> Priority.HIGH
            else -> Priority.LOW
        }

        val category = when (categorySpinner.selectedItem.toString()) {
            "Work" -> Category.WORK
            "Personal" -> Category.PERSONAL
            "Study" -> Category.STUDY
            "Fitness" -> Category.FITNESS
            "Other" -> Category.OTHER
            else -> Category.OTHER
        }

        val validationResult = taskViewModel.validateInputs(title, date, time, description, locationText)
        if (validationResult.isFailure) {
            Toast.makeText(requireContext(), validationResult.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
            return
        }

        val prepareResult = taskViewModel.prepareTaskDetails(validationResult.getOrNull()!!, priority, category, requireContext())
        if (prepareResult.isFailure) {
            Toast.makeText(requireContext(), prepareResult.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
            return
        }

        taskViewModel.submitTask(prepareResult.getOrNull()!!, date)

        taskViewModel.taskSubmissionStatus.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                Toast.makeText(requireContext(), result.getOrNull(), Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), CalendarActivity::class.java))
            } else {
                Toast.makeText(requireContext(), result.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinners() {
        val priorityAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priority_options,
            android.R.layout.simple_spinner_item
        )
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = priorityAdapter

        val categoryAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_options,
            android.R.layout.simple_spinner_item,
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                taskViewModel.getCurrentLocation(requireContext())
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
}