package com.example.schedulemaster

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

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