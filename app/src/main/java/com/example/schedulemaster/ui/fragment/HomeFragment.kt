package com.example.schedulemaster.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.schedulemaster.R

class HomeFragment : Fragment(), View.OnClickListener  {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        return v
    }

    override fun onClick(v: View) {
        TODO("Not yet implemented")
    }
}