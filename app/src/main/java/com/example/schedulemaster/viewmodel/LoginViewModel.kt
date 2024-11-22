package com.example.schedulemaster.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoginViewModel : AuthViewModel() {
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    // Handle sign-in logic
    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        _loginStatus.value = true
                    } else {
                        _loginStatus.value = false
                        _errorMessage.value = "Email not verified. Please verify your email before logging in."
                        auth.signOut()
                    }
                } else {
                    _loginStatus.value = false
                    _errorMessage.value = task.exception?.message
                }
            }
    }
}