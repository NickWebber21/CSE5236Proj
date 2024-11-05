package com.example.schedulemaster.ui.activity

import androidx.fragment.app.Fragment
import com.example.schedulemaster.ui.fragment.AddTaskFragment

class AddTaskActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return AddTaskFragment()
    }
}