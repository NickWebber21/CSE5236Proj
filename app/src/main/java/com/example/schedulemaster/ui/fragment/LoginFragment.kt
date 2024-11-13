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
import com.example.schedulemaster.ui.activity.CalendarActivity

class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var mEditUsernameText: EditText
    private lateinit var mEditPasswordText: EditText
    private lateinit var mLoginButton: Button
    private lateinit var auth: FirebaseAuth
    //-----use overloading below-----
    //executes upon creating the fragment and another instance exists
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }
    //executes upon creating the fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_login, container, false)
        Log.i("LoginFragment", "onCreateView fragment started.")
        //bind views
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
    //signs the user into an account if it exists on firebase auth
    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), CalendarActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e("LoginFragment", "Login failed: ${task.exception?.message}")
                    Toast.makeText(requireContext(), "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
