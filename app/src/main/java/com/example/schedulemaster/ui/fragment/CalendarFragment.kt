package com.example.schedulemaster.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.schedulemaster.R
import com.example.schedulemaster.model.Task
import com.example.schedulemaster.ui.activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.schedulemaster.model.Category
import com.example.schedulemaster.model.Location
import com.example.schedulemaster.model.Priority

class CalendarFragment : Fragment(), View.OnClickListener {

    private lateinit var mHomeButton: Button
    private lateinit var calendarView: CalendarView
    private lateinit var taskContainer: LinearLayout
    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    // Dummy data with actual dates
    private val dummyTasks = listOf(
        Task(
            title = "Meeting with Bob",
            date = "12/11/2024",  // Actual date for the task
            time = "10:00",
            description = "Discuss project updates",
            location = Location(40.7128, -74.0060),
            priority = Priority.HIGH,
            category = Category.WORK
        ),
        Task(
            title = "Workout session",
            date = "12/11/2024",  // Actual date for the task
            time = "14:00",
            description = "Strength training at gym",
            location = Location(40.7128, -74.0060),
            priority = Priority.MEDIUM,
            category = Category.FITNESS
        ),
        Task(
            title = "Study for exam",
            date = "15/11/2024",  // Another actual date
            time = "17:00",
            description = "Review chapters 5-8",
            location = Location(40.7128, -74.0060),
            priority = Priority.LOW,
            category = Category.STUDY
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_calendar, container, false)

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

        mHomeButton = v.findViewById(R.id.HomeButton)
        mHomeButton.setOnClickListener(this)

        calendarView = v.findViewById(R.id.calendarView)
        taskContainer = v.findViewById(R.id.taskContainer)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            Log.d("Date sent to firebase:", date)
            loadTasksForDate(date)
        }
        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.HomeButton -> {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun loadTasksForDate(date: String) {
        taskContainer.removeAllViews() // Clear previous tasks
        // Loop through the dummy tasks and show tasks for the selected date
        for (task in dummyTasks) {
            if (task.date == date) {  // Check if the task date matches the selected date
                addTaskToView(task)
            }
        }
    }

    private fun addTaskToView(task: Task) {
        // Create a TextView for each task
        val taskView = TextView(context).apply {
            text = "${task.title} at ${task.time}"
            textSize = 16f
            setPadding(8, 8, 8, 8)
        }

        taskContainer.addView(taskView)
    }
}
