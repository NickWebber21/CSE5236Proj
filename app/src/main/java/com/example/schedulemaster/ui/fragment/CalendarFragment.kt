package com.example.schedulemaster.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

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
        taskRecyclerView = v.findViewById(R.id.taskRecyclerView)
        taskRecyclerView.layoutManager = LinearLayoutManager(context)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
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
            taskAdapter = TaskAdapter(tasks)
            taskRecyclerView.adapter = taskAdapter
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                Log.e("CalendarFragment", it)
            }
        })
    }

    private class TaskHolder(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.task_tile, parent, false)) {
        private val titleView: TextView = itemView.findViewById(R.id.titleView)
        private val timeView: TextView = itemView.findViewById(R.id.timeView)
        private val addressView: TextView = itemView.findViewById(R.id.addressView)
        private val priorityView: TextView = itemView.findViewById(R.id.priorityView)
        private val categoryView: TextView = itemView.findViewById(R.id.categoryView)
        private val descriptionView: TextView = itemView.findViewById(R.id.descriptionView)

        fun bind(task: Task) {
            titleView.text = task.title
            timeView.text = task.time
            addressView.text = task.location.address
            priorityView.text = task.priority.toString()
            categoryView.text = task.category.toString()
            descriptionView.text = task.description
        }
    }

    private inner class TaskAdapter(private val tasks: List<Task>) :
        RecyclerView.Adapter<TaskHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
            val inflater = layoutInflater
            return TaskHolder(inflater, parent)
        }

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        }

        override fun getItemCount(): Int = tasks.size
    }

}
