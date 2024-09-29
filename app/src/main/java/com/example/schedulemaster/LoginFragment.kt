package com.example.schedulemaster

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var mUsernameEditText: EditText
    private lateinit var mPasswordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var exitButton: Button
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var outputWrongCred: TextView
    private var passwordAttempts = 3
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout for fragment
        val v = inflater.inflate(R.layout.fragment_login, container, false)
        mUsernameEditText = v.findViewById(R.id.username_text)
        mPasswordEditText = v.findViewById (R.id.password_text)
        val loginButton = v.findViewById(R.id.login_button)
        loginButton.setOnClickListener(this)
        val cancelButton = v.findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener(this)
        val newUserButton = v.findViewById(R.id.new_user_button)
        newUserButton.setOnClickListener(this)
        return v
        // Initialize buttons, text input and output
        /*loginButton = v.findViewById(R.id.button)
        usernameInput = v.findViewById(R.id.editText)
        passwordInput = v.findViewById(R.id.editText2)
        exitButton = v.findViewById(R.id.button2)
        outputWrongCred = v.findViewById(R.id.textView3)
        outputWrongCred.visibility = View.GONE

        // Button click listeners
        loginButton.setOnClickListener {
            if (usernameInput.text.toString() == "a" && passwordInput.text.toString() == "a") {
                Toast.makeText(requireActivity(), "Loading Homepage", Toast.LENGTH_SHORT).show()
            } else if(usernameInput.text.toString() == "admin"){
                Toast.makeText(requireActivity(), "Wrong Password for username", Toast.LENGTH_SHORT).show()
                outputWrongCred.visibility = View.VISIBLE
                outputWrongCred.setBackgroundColor(Color.RED)
                passwordAttempts--
                outputWrongCred.text = passwordAttempts.toString()

                if (passwordAttempts == 0) {
                    loginButton.isEnabled = false
                }
            } else {
                Toast.makeText(requireActivity(), "Wrong Credentials", Toast.LENGTH_SHORT).show()
                outputWrongCred.visibility = View.VISIBLE
                outputWrongCred.setBackgroundColor(Color.RED)
                passwordAttempts--
                outputWrongCred.text = passwordAttempts.toString()

                if (passwordAttempts == 0) {
                    loginButton.isEnabled = false
                }
            }
        }

        exitButton.setOnClickListener {
            requireActivity().finish()
        }

        return v*/
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.login_button -> checkLogin()
            R.id.cancel_button ->
                activity?.finish()
            R.id.new_user_button ->
                val fm = fragmentManager
            fm?.beginTransaction()
                ?.replace(/* container */, fragment)
                ?.addToBackStack(. . .)
                ?.commit()
        }
    }
}
