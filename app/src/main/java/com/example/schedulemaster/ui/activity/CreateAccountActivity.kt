package com.example.schedulemaster.ui.activity

import androidx.fragment.app.Fragment
import com.example.schedulemaster.ui.fragment.CreateAccountFragment

class CreateAccountActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return CreateAccountFragment()
    }
}