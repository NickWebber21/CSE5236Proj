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
import com.google.firebase.auth.FirebaseAuth
import com.example.schedulemaster.ui.activity.HomeActivity

class CreateAccountFragment : Fragment(), View.OnClickListener {

    private lateinit var mEditUsernameText: EditText
    private lateinit var mEditPasswordText: EditText
    private lateinit var mCreateAccountButton: Button
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
        val v = inflater.inflate(R.layout.fragment_create_account, container, false)
        Log.i("CreateAccountFragment", "onCreateView fragment started.")

        // Initialize UI elements
        mEditUsernameText = v.findViewById(R.id.usernameText)
        mEditPasswordText = v.findViewById(R.id.passwordText)
        mCreateAccountButton = v.findViewById(R.id.createAccountButton)
        mCreateAccountButton.setOnClickListener(this)

        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.createAccountButton -> {
                val username = mEditUsernameText.text.toString().trim()
                val password = mEditPasswordText.text.toString().trim()

                if (username.isNotEmpty() && password.isNotEmpty()) {
                    createAccount(username, password)
                } else {
                    Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
                }
            }
            else -> Log.e("CreateAccountFragment", "Error: Invalid button press")
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Account creation successful
                    Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent);
                } else {
                    // If sign up fails, display a message to the user.
                    Log.e("CreateAccountFragment", "Account creation failed: ${task.exception?.message}")
                    Toast.makeText(requireContext(), "Account creation failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
