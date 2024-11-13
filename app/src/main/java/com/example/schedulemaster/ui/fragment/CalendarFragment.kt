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
import com.example.schedulemaster.ui.activity.AddTaskActivity
import com.example.schedulemaster.ui.activity.CalendarActivity
import com.example.schedulemaster.ui.activity.HomeActivity
import com.example.schedulemaster.ui.activity.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class CalendarFragment : Fragment(), View.OnClickListener {

    private lateinit var addTaskButton: Button
    private lateinit var logoutButton: Button
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

        addTaskButton = v.findViewById(R.id.AddTaskButton)
        addTaskButton.setOnClickListener(this)
        logoutButton = v.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener(this)

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

            R.id.AddTaskButton -> {
                val intent = Intent(requireContext(), AddTaskActivity::class.java)
                startActivity(intent)
            }


            R.id.logoutButton -> {
                auth.signOut()
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun loadTasksForDate(date: String) {
        taskContainer.removeAllViews()
        val tasksRef = databaseRef.child("users").child(userId).child("tasks")
        tasksRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
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

    // This whole function could be shortened by using an XML layout instead
    private fun addTaskToView(task: Task) {
        // Create a LinearLayout to hold the task details
        val taskLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // Tier 1: Title and Time
        val titleAndTimeLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 8, 0, 4)
        }

        // Title
        val titleView = TextView(context).apply {
            text = task.title
            textSize = 18f
            setPadding(0, 8, 16, 4)
            setTextColor(resources.getColor(R.color.black))
            maxLines = Integer.MAX_VALUE
            isSingleLine = false
        }

        // Time
        val timeView = TextView(context).apply {
            text = task.time
            textSize = 16f
            setPadding(0, 8, 0, 4)
            setTextColor(resources.getColor(R.color.black))
            maxLines = Integer.MAX_VALUE
            isSingleLine = false
        }

        titleAndTimeLayout.addView(titleView)
        titleAndTimeLayout.addView(timeView)

        // Tier 2: Location (Address)
        val addressLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 8, 0, 4)
        }

        val addressView = TextView(context).apply {
            text = task.location.address
            textSize = 14f
            setPadding(0, 8, 16, 4)
            setTextColor(resources.getColor(R.color.black))
            maxLines = Integer.MAX_VALUE
            isSingleLine = false
        }

        addressLayout.addView(addressView)

        // Tier 3: Priority and Category
        val priorityCategoryLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 8, 0, 4)
        }

        val priorityView = TextView(context).apply {
            text = task.priority.toString()
            textSize = 14f
            setPadding(0, 8, 16, 4)
            setTextColor(resources.getColor(R.color.black))
            maxLines = Integer.MAX_VALUE
            isSingleLine = false
        }

        val categoryView = TextView(context).apply {
            text = task.category.toString()
            textSize = 14f
            setPadding(0, 8, 0, 4)
            setTextColor(resources.getColor(R.color.black))
            maxLines = Integer.MAX_VALUE
            isSingleLine = false
        }

        priorityCategoryLayout.addView(priorityView)
        priorityCategoryLayout.addView(categoryView)

        // Tier 4: Description
        val descriptionView = TextView(context).apply {
            text = task.description
            textSize = 14f
            setPadding(0, 8, 0, 4)
            setTextColor(resources.getColor(R.color.black))
            maxLines = Integer.MAX_VALUE
            isSingleLine = false
        }

        // Add views to the task layout
        taskLayout.addView(titleAndTimeLayout)
        taskLayout.addView(addressLayout)
        taskLayout.addView(priorityCategoryLayout)
        taskLayout.addView(descriptionView)

        taskContainer.addView(taskLayout)

        // Optional: Add a separator for each task
        val divider = View(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                2
            ).apply {
                setMargins(16, 8, 16, 8)
            }
            setBackgroundColor(resources.getColor(R.color.purple_200))
        }
        taskContainer.addView(divider)
    }
}
