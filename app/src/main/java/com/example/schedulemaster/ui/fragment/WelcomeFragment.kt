package com.example.schedulemaster.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.schedulemaster.R
import com.example.schedulemaster.ui.activity.AddTaskActivity
import com.example.schedulemaster.ui.activity.CalendarActivity
import com.example.schedulemaster.ui.activity.CreateAccountActivity
import com.example.schedulemaster.ui.activity.HomeActivity
import com.example.schedulemaster.ui.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class WelcomeFragment : Fragment(), View.OnClickListener {

    private lateinit var mLoginButton: Button
    private lateinit var mCreateAccountButton: Button
    // Below is for testing purposes as of right now
    private lateinit var mHomeButton: Button
    private lateinit var mCalendarButton: Button
    private lateinit var mAddTaskButton: Button

    // Firebase Auth instance
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_welcome, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if user is already logged in
        if (auth.currentUser != null) {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
        }

        // Initialize buttons and set onClick listeners
        mLoginButton = v.findViewById(R.id.loginButton)
        mLoginButton.setOnClickListener(this)
        mCreateAccountButton = v.findViewById(R.id.createAccountButton)
        mCreateAccountButton.setOnClickListener(this)

        // Buttons for testing purposes
        mHomeButton = v.findViewById(R.id.HomeButton)
        mHomeButton.setOnClickListener(this)
        mCalendarButton = v.findViewById(R.id.CalendarButton)
        mCalendarButton.setOnClickListener(this)
        mAddTaskButton = v.findViewById(R.id.AddTaskButton)
        mAddTaskButton.setOnClickListener(this)

        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.createAccountButton -> {
                val intent = Intent(requireContext(), CreateAccountActivity::class.java)
                startActivity(intent)
            }
            // Below is for testing purposes as of right now
            R.id.HomeButton -> {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
            }
            R.id.CalendarButton -> {
                val intent = Intent(requireContext(), CalendarActivity::class.java)
                startActivity(intent)
            }
            R.id.AddTaskButton -> {
                val intent = Intent(requireContext(), AddTaskActivity::class.java)
                startActivity(intent)
            }
            else -> Log.e("INSIDE WelcomeFragment.kt", "Error: Invalid button press")
        }
    }
}
