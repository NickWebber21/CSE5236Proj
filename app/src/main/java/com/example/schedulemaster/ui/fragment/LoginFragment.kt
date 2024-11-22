package com.example.schedulemaster.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.schedulemaster.R
import com.example.schedulemaster.ui.activity.CalendarActivity
import com.example.schedulemaster.viewmodel.LoginViewModel

class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var mEditUsernameText: EditText
    private lateinit var mEditPasswordText: EditText
    private lateinit var mVerificationCodeText: EditText
    private lateinit var mLoginButton: Button

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_login, container, false)
        Log.i("LoginFragment", "onCreateView fragment started.")

        mEditUsernameText = v.findViewById(R.id.usernameText)
        mEditPasswordText = v.findViewById(R.id.passwordText)
        mVerificationCodeText = v.findViewById(R.id.verificationCodeText)
        mLoginButton = v.findViewById(R.id.loginButton)
        mLoginButton.setOnClickListener(this)

        observeViewModel()

        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> {
                val email = mEditUsernameText.text.toString().trim()
                val password = mEditPasswordText.text.toString().trim()
                val verificationCode = mVerificationCodeText.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty() && verificationCode.isNotEmpty()) {
                    viewModel.signInWithEmail(email, password)
                    viewModel.verifyCode(verificationCode)
                } else {
                    Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                navigateToCalendar()
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(requireContext(), "Login failed: $it", Toast.LENGTH_SHORT).show()
                Log.e("LoginFragment", "Login failed: $it")
            }
        })
    }

    private fun navigateToCalendar() {
        Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), CalendarActivity::class.java)
        startActivity(intent)
    }
}
