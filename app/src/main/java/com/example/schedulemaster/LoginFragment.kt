package com.example.schedulemaster

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.NoSuchAlgorithmException
import java.util.concurrent.CopyOnWriteArrayList
import timber.log.Timber


class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var mUsernameEditText: EditText
    private lateinit var mPasswordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var exitButton: Button
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var outputWrongCred: TextView
    private var mUserAccountList = CopyOnWriteArrayList<UserAccount>()

    private var passwordAttempts = 3
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout for fragment
        val v = inflater.inflate(R.layout.fragment_login, container, false)
        //TO-DO get data store application make
        //mDataStore = TicTacToeApplication.getDataStore()

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
        val activity = requireActivity()
        when (view.id) {
            R.id.login_button -> checkLogin()
            R.id.cancel_button -> activity.finish()
            TODO(reason = "Add account manager fragment")
            /*R.id.new_user_button -> {
                val rotation = activity.windowManager.defaultDisplay.rotation
                val fm = parentFragmentManager
                val fragment = AccountFragment()
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
                    fm.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack("account_fragment")
                        .commit()
                } else {
                    fm.beginTransaction()
                        .add(R.id.account_fragment_container, fragment)
                        .addToBackStack("account_fragment")
                        .commit()
                }
            }*/
        }
    }

    private fun checkLogin() {
        val username = mUsernameEditText.text.toString()
        val password = mPasswordEditText.text.toString()
        try {
            val activity: Activity = requireActivity()
            val userAccount = UserAccount(username, password)
            if (mUserAccountList.contains(userAccount)) {
                CoroutineScope(Dispatchers.IO).launch {
                    //mDataStore.putString(Settings.Keys.OPT_NAME, username)
                    Timber.tag(classTag).d("Wrote username successfully to DataStore")
                }

                // Bring up the GameOptions screen
                startActivity(Intent(activity, GameOptionsActivity::class.java))
                activity.finish()
            } else {
                showLoginErrorFragment()
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        /*val digest: MessageDigest
        try {
            digest = MessageDigest.getInstance("SHA-256")
            val sha256HashBytes = digest.digest(password.toByteArray(StandardCharsets.UTF_8))
            val sha256HashStr = StringUtils.bytesToHex(sha256HashBytes)
            val activity: Activity = requireActivity()

            val userAccount = UserAccount(username, sha256HashStr)

            // if (accountList.size > 0 && hasMatchingAccount) {
            if (mUserAccountList.contains(userAccount)) {
                CoroutineScope(Dispatchers.IO).launch {
                    mDataStore.putString(Settings.Keys.OPT_NAME, username)
                    Timber.tag(classTag).d("Wrote username successfully to DataStore")
                }

                // Bring up the GameOptions screen
                startActivity(Intent(activity, GameOptionsActivity::class.java))
                activity.finish()
            } else {
                showLoginErrorFragment()
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }*/
    }
}
