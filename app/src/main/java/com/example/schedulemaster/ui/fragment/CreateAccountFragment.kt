package com.example.schedulemaster.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.schedulemaster.R
import com.example.schedulemaster.ui.activity.CalendarActivity
import com.example.schedulemaster.viewmodel.CreateAccountViewModel
import kotlinx.coroutines.*

class CreateAccountFragment : Fragment(), View.OnClickListener {

    private lateinit var mEditUsernameText: EditText
    private lateinit var mEditPasswordText: EditText
    private lateinit var mCreateAccountButton: Button
    private lateinit var mWaitingText: TextView

    private val viewModel: CreateAccountViewModel by viewModels()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_create_account, container, false)

        mEditUsernameText = v.findViewById(R.id.usernameText)
        mEditPasswordText = v.findViewById(R.id.passwordText)
        mCreateAccountButton = v.findViewById(R.id.createAccountButton)
        mWaitingText = v.findViewById(R.id.waitingText)
        mCreateAccountButton.setOnClickListener(this)

        viewModel.accountCreationStatus.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                mWaitingText.text = "Waiting on email verification"
                mWaitingText.visibility = View.VISIBLE
                waitForEmailVerification()
            }
            result.onFailure { error ->
                Log.e("CreateAccountFragment", "Account creation failed: ${error.message}")
                Toast.makeText(requireContext(), "Account creation failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                Log.e("CreateAccountFragment", it)
            }
        })

        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.createAccountButton -> {
                val email = mEditUsernameText.text.toString().trim()
                val password = mEditPasswordText.text.toString().trim()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.createAccount(email, password)
                } else {
                    Toast.makeText(requireContext(), "Please enter your email and password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun waitForEmailVerification() {
        coroutineScope.launch {
            while (true) {
                val user = viewModel.auth.currentUser
                user?.reload()
                if (user?.isEmailVerified == true) {
                    val intent = Intent(requireContext(), CalendarActivity::class.java)
                    startActivity(intent)
                    break
                }
                delay(3000) // Check every 3 seconds
            }
        }
    }
}