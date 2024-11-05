package com.example.schedulemaster.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.schedulemaster.R
import com.example.schedulemaster.ui.activity.HomeActivity
import com.example.schedulemaster.ui.activity.LoginActivity

class CalendarFragment : Fragment(), View.OnClickListener  {
    private lateinit var mHomeButton: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_calendar, container, false)
        mHomeButton = v.findViewById(R.id.HomeButton)
        mHomeButton.setOnClickListener(this)
        return v
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.HomeButton -> {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent);
            }
        }
    }
}