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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.schedulemaster.R
import com.example.schedulemaster.model.Task
import com.example.schedulemaster.ui.activity.AddTaskActivity
import com.example.schedulemaster.ui.activity.WelcomeActivity
import com.example.schedulemaster.viewmodel.CalendarViewModel
import com.google.firebase.auth.FirebaseAuth

class CalendarFragment : Fragment(), View.OnClickListener {

    private lateinit var addTaskButton: Button
    private lateinit var logoutButton: Button
    private lateinit var calendarView: CalendarView
    private lateinit var taskContainer: LinearLayout

    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_calendar, container, false)

        addTaskButton = v.findViewById(R.id.AddTaskButton)
        addTaskButton.setOnClickListener(this)
        logoutButton = v.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener(this)
        calendarView = v.findViewById(R.id.calendarView)
        taskContainer = v.findViewById(R.id.taskContainer)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
            Log.d("CalendarFragment", "Selected date: $date")
            viewModel.loadTasksForDate(date)
        }

        observeViewModel()

        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.AddTaskButton -> {
                val intent = Intent(requireContext(), AddTaskActivity::class.java)
                startActivity(intent)
            }
            R.id.logoutButton -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            updateTaskContainer(tasks)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Log.e("CalendarFragment", it)
            }
        })
    }

    private fun updateTaskContainer(tasks: List<Task>) {
        taskContainer.removeAllViews()
        if (tasks.isEmpty()) {
            val noTasksView = TextView(context).apply {
                text = "No tasks for this date."
                textSize = 16f
                setTextColor(resources.getColor(R.color.black))
            }
            taskContainer.addView(noTasksView)
        } else {
            tasks.forEach { task ->
                addTaskToView(task)
            }
        }
    }

    private fun addTaskToView(task: Task) {
        val taskLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val titleAndTimeLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 8, 0, 4)
        }

        val titleView = TextView(context).apply {
            text = task.title
            textSize = 18f
            setPadding(0, 8, 16, 4)
            setTextColor(resources.getColor(R.color.black))
            maxLines = Integer.MAX_VALUE
            isSingleLine = false
        }

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

        val descriptionView = TextView(context).apply {
            text = task.description
            textSize = 14f
            setPadding(0, 8, 0, 4)
            setTextColor(resources.getColor(R.color.black))
            maxLines = Integer.MAX_VALUE
            isSingleLine = false
        }

        taskLayout.addView(titleAndTimeLayout)
        taskLayout.addView(addressLayout)
        taskLayout.addView(priorityCategoryLayout)
        taskLayout.addView(descriptionView)

        taskContainer.addView(taskLayout)

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
