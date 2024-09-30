package com.example.schedulemaster

import android.os.Bundle
import androidx.fragment.app.Fragment

class LoginActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        // Add the fragment to the activity
        return LoginFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()

        }

    }

}