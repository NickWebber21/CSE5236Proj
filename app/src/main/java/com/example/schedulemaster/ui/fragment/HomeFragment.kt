package com.example.schedulemaster.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.schedulemaster.R

class HomeFragment : Fragment(), View.OnClickListener  {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        // Set up button to open CalendarFragment
        val openCalendarButton: Button = v.findViewById(R.id.openCalendarButton)
        openCalendarButton.setOnClickListener {
            // Replace HomeFragment with CalendarFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CalendarFragment())
                .addToBackStack(null) // Optional: adds the transaction to the back stack
                .commit()
        }
        return v
    }

    override fun onClick(v: View) {
        TODO("Not yet implemented")
    }
}