package com.example.schedulemaster.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.schedulemaster.R
import com.example.schedulemaster.ui.activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var mEditUsernameText: EditText
    private lateinit var mEditPasswordText: EditText
    private lateinit var mLoginButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_login, container, false)
        Log.i("LoginFragment", "onCreateView fragment started.")

        // UI element references
        mEditUsernameText = v.findViewById(R.id.usernameText)
        mEditPasswordText = v.findViewById(R.id.passwordText)
        mLoginButton = v.findViewById(R.id.loginButton)
        mLoginButton.setOnClickListener(this)

        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> {
                val username = mEditUsernameText.text.toString().trim()
                val password = mEditPasswordText.text.toString().trim()

                if (username.isNotEmpty() && password.isNotEmpty()) {
                    signIn(username, password)
                } else {
                    Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
                }
            }
            else -> Log.e("LoginFragment", "Error: Invalid button press")
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    // Login failed, show error message
                    Log.e("LoginFragment", "Login failed: ${task.exception?.message}")
                    Toast.makeText(requireContext(), "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
