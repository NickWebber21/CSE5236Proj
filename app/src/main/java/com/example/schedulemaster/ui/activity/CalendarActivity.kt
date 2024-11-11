package com.example.schedulemaster.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.schedulemaster.ui.fragment.CalendarFragment
import com.google.android.gms.location.FusedLocationProviderClient

class CalendarActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        return CalendarFragment()
    }
}