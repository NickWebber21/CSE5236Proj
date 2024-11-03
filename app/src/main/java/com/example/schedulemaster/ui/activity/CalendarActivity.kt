package com.example.schedulemaster.ui.activity

import androidx.fragment.app.Fragment
import com.example.schedulemaster.ui.fragment.CalendarFragment

class CalendarActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return CalendarFragment()
    }
}