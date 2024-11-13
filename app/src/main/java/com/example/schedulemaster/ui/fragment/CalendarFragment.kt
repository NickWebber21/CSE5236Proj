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
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_calendar, container, false)

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
        userId = auth.currentUser?.uid ?: ""

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

        // Query tasks for the logged-in user and the selected date
        val tasksRef = databaseRef.child("users").child(userId).child("tasks")
        tasksRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Loop through tasks and check if the date matches
                    for (taskSnapshot in snapshot.children) {
                        val task = taskSnapshot.getValue(Task::class.java)
                        if (task != null && task.date == date) {
                            addTaskToView(task)
                        }
                    }
                } else {
                    Log.d("CalendarFragment", "No tasks found for this user.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("CalendarFragment", "Error fetching tasks: ${error.message}")
            }
        })
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
