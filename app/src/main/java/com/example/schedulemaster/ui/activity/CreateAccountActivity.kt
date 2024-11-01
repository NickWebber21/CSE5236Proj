package com.example.schedulemaster.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.schedulemaster.ui.fragment.CreateAccountFragment
import com.example.schedulemaster.R

class CreateAccountActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return CreateAccountFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val tag = "OnCreate"
        Log.i(tag, "-----------------------NEW LOG---------------------")
        Log.i(tag, "onCreate activity started.")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)
        // add login fragment to container
        if (fragment == null) {
            fragment = CreateAccountFragment()
            fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
}