package com.example.schedulemaster

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
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