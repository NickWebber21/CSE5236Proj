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
import com.example.schedulemaster.ui.activity.CreateAccountActivity

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var mEditUsernameText: EditText
    private lateinit var mEditPasswordText: EditText
    private lateinit var mLoginButton: Button
    private lateinit var mCreateAccountButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // dont have any params at the moment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_login, container, false) // view
        val tag = "OnCreateView"
        Log.i(tag, "-----------------------NEW LOG---------------------")
        Log.i(tag, "onCreateView fragment started.")

        // references to the ui elements specified in XML
        mEditUsernameText = v.findViewById(R.id.usernameText)
        mEditPasswordText = v.findViewById(R.id.passwordText)
        mLoginButton = v.findViewById(R.id.loginButton)
        mLoginButton.setOnClickListener(this)
        mCreateAccountButton = v.findViewById(R.id.createAccountButton) // wip
        mCreateAccountButton.setOnClickListener(this)

        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> {
                // do stuff with FireBase when login button is clicked
                val username = mEditUsernameText.text.toString().trim()
                val password = mEditPasswordText.text.toString().trim()
                Log.d("INSIDE LoginFragment.kt", "logging in with the username entered: $username and password: $password")
                Toast.makeText(requireContext(), "login button clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.createAccountButton -> {
                // do stuff with FireBase for when create account button is clicked
                Log.d("INSIDE LoginFragment.kt", "creating account")
                Toast.makeText(requireContext(), "create account button clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), CreateAccountActivity::class.java)
                startActivity(intent);
            }
            else -> Log.e("INSIDE LoginFragment.kit", "Error: Invalid button press")
        }
    }
}