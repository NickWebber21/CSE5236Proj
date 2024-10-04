package com.example.schedulemaster

import android.os.Bundle
import android.util.Log // used to log errors
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/*private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"*/

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var mEditUsernameText: EditText
    private lateinit var mEditPasswordText: EditText
    private lateinit var mLoginButton: Button
    private lateinit var mCreateAccountButton: Button

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View
        val tag = "OnCreateView"
        Log.i(tag, "-----------------------NEW LOG---------------------")
        Log.i(tag, "onCreateView fragment started.")
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false)

        // references to the ui elements specified in XML
        mEditUsernameText = v.findViewById(R.id.usernameText)
        mEditPasswordText = v.findViewById(R.id.passwordText)
        mLoginButton = v.findViewById(R.id.loginButton)
        mCreateAccountButton = v.findViewById(R.id.createAccountButton)

        // on click listener for login button using lambda
        mLoginButton.setOnClickListener {
            val username = mEditUsernameText.text.toString().trim()
            val password = mEditPasswordText.text.toString().trim()

            // for later: might want to check if valid username and password
            // login action
            Log.d("INSIDE LoginFragment.kt", "logging in with the username entered: $username")
            Toast.makeText(requireContext(), "login successful!!!", Toast.LENGTH_SHORT).show()
        }

        // add create account button listener HERE

        return view
    }

    /*companion object {
        *//**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         *//*
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}