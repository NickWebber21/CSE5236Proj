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
import com.example.schedulemaster.ui.activity.CalendarActivity
import com.example.schedulemaster.ui.activity.CreateAccountActivity
import com.example.schedulemaster.ui.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class WelcomeFragment : Fragment(), View.OnClickListener {
    private lateinit var mLoginButton: Button
    private lateinit var mCreateAccountButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_welcome, container, false)
        //setup firebase auth
        auth = FirebaseAuth.getInstance()
        //check if user is logged in already
        if (auth.currentUser != null) {
            val intent = Intent(requireContext(), CalendarActivity::class.java)
            startActivity(intent)
        }
        //bind views
        mLoginButton = v.findViewById(R.id.loginButton)
        mLoginButton.setOnClickListener(this)
        mCreateAccountButton = v.findViewById(R.id.createAccountButton)
        mCreateAccountButton.setOnClickListener(this)

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
            else -> Log.e("WelcomeFragment.kt", "Error: Invalid button press")
        }
    }
}
