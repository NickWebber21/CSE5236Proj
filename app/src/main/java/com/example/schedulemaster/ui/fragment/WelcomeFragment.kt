package com.example.schedulemaster.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.schedulemaster.R

class WelcomeFragment : Fragment(), View.OnClickListener {
    private lateinit var mLoginButton: Button
    private lateinit var mCreateAccountButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_welcome, container, false)

        mLoginButton.setOnClickListener(this)
        mCreateAccountButton.setOnClickListener(this)

        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
           // login button stuff and create account button stuff
        }
    }
}