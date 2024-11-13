package com.example.schedulemaster.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.schedulemaster.R
import com.example.schedulemaster.ui.activity.AddTaskActivity
import com.example.schedulemaster.ui.activity.CalendarActivity
import com.example.schedulemaster.ui.activity.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var addTaskButton: Button
    private lateinit var logoutButton: Button
    private lateinit var calendarTitle: TextView
    private lateinit var selectedDayTextView: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var weeklyCalendarLayout: LinearLayout
    private lateinit var dayButtons: MutableList<Pair<Button, TextView>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        addTaskButton = v.findViewById(R.id.AddTaskButton)
        logoutButton = v.findViewById(R.id.logoutButton)
        calendarTitle = v.findViewById(R.id.calendarTitle)
        selectedDayTextView = v.findViewById(R.id.selectedDayTextView)
        weeklyCalendarLayout = v.findViewById(R.id.weeklyCalendarLayout)
        // Set click listeners
        addTaskButton.setOnClickListener(this)
        logoutButton.setOnClickListener(this)
        calendarTitle.setOnClickListener {
            val intent = Intent(requireContext(), CalendarActivity::class.java)
            startActivity(intent)
        }

        // Initialize the day buttons and labels
        setupWeekButtons()
        return v
    }

    private fun setupWeekButtons() {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_WEEK)
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())

        // Position the week so that today is in the center
        calendar.add(Calendar.DAY_OF_MONTH, -((today - Calendar.WEDNESDAY) % 7))

        for (i in 0..6) {
            val dayButton = Button(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                text = dayFormat.format(calendar.time) // "Mon", "Tue", etc.
                setOnClickListener {
                    selectedDayTextView.text = "Tasks for $text"
                    Log.d("HomeFragment", "Selected day: $text")
                }
                if (i == 3) { // Center button for today
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
                }
            }

            // TextView for the day of the month below each button
            val dateLabel = TextView(requireContext()).apply {
                text = dateFormat.format(calendar.time) // Day of month
                textSize = 12f
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }

            // Add button and label to layout
            val dayLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = android.view.Gravity.CENTER
                addView(dayButton)
                addView(dateLabel)
            }

            weeklyCalendarLayout.addView(dayLayout)

            Log.d("HomeFragment", "Added button for day: ${dayButton.text}, date: ${dateLabel.text}")

            // Move to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.AddTaskButton -> {
                val intent = Intent(requireContext(), AddTaskActivity::class.java)
                startActivity(intent)
            }

            /*R.id.calendarButton -> {
                val intent = Intent(requireContext(), CalendarActivity::class.java)
                startActivity(intent)
            }*/

            R.id.logoutButton -> {
                auth.signOut()
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                startActivity(intent)
            }

            else -> Log.e("HomeFragment", "Error: Invalid button press")
        }
    }
}
