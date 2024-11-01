package com.example.schedulemaster.ui.activity

import androidx.fragment.app.Fragment
import com.example.schedulemaster.ui.fragment.LoginFragment

class LoginActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return LoginFragment()
    }
}