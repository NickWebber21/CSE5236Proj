package com.example.schedulemaster.ui.activity

import androidx.fragment.app.Fragment
import com.example.schedulemaster.ui.fragment.HomeFragment

class HomeActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return HomeFragment()
    }
}