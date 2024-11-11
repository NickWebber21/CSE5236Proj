package com.example.schedulemaster.ui.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.schedulemaster.R
import com.example.schedulemaster.model.Category
import com.example.schedulemaster.model.Priority
import com.example.schedulemaster.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import com.example.schedulemaster.ui.activity.HomeActivity

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)

        // Initialize Firebase ref
        databaseRef = FirebaseDatabase.getInstance().reference

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
        // Do we need these?
        submitButton.setOnClickListener(this)
        homeButton.setOnClickListener(this)

        // Set up DatePicker for date input
        // Do we need this?
        dateInput.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up TimePicker for time input
        // Do we need this?
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
            val task = createTask()
            // If task creation was successful
            task?.let {
                // Push the task to Firebase Realtime Database under the user's node with a unique ID
                databaseRef.child("users").child(userId).child("tasks").push().setValue(it)
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
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createTask(): Task? {
        // Retrieve inputs
        val title = titleInput.text.toString().trim()
        val date = dateInput.text.toString().trim()
        val time = timeInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val location = locationInput.text.toString().trim()

        // Validate inputs (simple checks)
        if (title.isEmpty() || date.isEmpty() || time.isEmpty() || description.isEmpty() || location.isEmpty()) {
            Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show()
            return null
        }

        // Retrieve and map priority
        val priorityString = prioritySpinner.selectedItem.toString()
        val priority = when (priorityString) {
            "Low" -> Priority.LOW
            "Medium" -> Priority.MEDIUM
            "High" -> Priority.HIGH
            else -> Priority.LOW
        }

        // Retrieve and map category
        val categoryString = categorySpinner.selectedItem.toString()
        val category = when (categoryString) {
            "Work" -> Category.WORK
            "Personal" -> Category.PERSONAL
            "Study" -> Category.STUDY
            "Fitness" -> Category.FITNESS
            "Other" -> Category.OTHER
            else -> Category.OTHER
        }

        // Return the constructed Task object
        return Task(title, date, time, description, location, priority, category)
    }

    // Used to help input the date
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
    // Used to help input th time
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
            // We can possibly change this to 12 hr time?
            hour, minute, true
        )
        timePickerDialog.show()
    }

    private fun navigateToHome() {
        // Navigate to the home page
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
    }

}
