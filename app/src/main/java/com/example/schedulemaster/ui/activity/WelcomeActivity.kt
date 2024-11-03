package com.example.schedulemaster.ui.activity

import androidx.fragment.app.Fragment
import com.example.schedulemaster.ui.fragment.LoginFragment
import com.example.schedulemaster.ui.fragment.WelcomeFragment

class WelcomeActivity : SingleFragmentActivity {
    override fun createFragment(): Fragment {
        return WelcomeFragment()
    }
}