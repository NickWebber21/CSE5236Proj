package com.example.schedulemaster.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.schedulemaster.ui.fragment.LoginFragment
import com.example.schedulemaster.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val tag = "OnCreate"
        Log.i(tag, "-----------------------NEW LOG---------------------")
        Log.i(tag, "onCreate activity started.")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        val fm = supportFragmentManager // get instance of fragment manager to create fragment transaction
        var fragment = fm.findFragmentById(R.id.fragment_container)
        // add login fragment to container
        if (fragment == null) {
            fragment = LoginFragment()
            fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
}