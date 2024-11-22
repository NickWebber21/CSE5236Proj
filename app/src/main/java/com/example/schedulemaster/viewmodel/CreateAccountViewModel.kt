package com.example.schedulemaster.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CreateAccountViewModel : AuthViewModel() {
    private val _accountCreationStatus = MutableLiveData<Result<String>>()
    val accountCreationStatus: LiveData<Result<String>> get() = _accountCreationStatus

    fun createAccount(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _accountCreationStatus.value = Result.failure(Exception("Email or password cannot be empty"))
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendEmailVerification()
                } else {
                    _accountCreationStatus.value = Result.failure(task.exception ?: Exception("Unknown error"))
                }
            }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _accountCreationStatus.value = Result.success("Verification email sent")
                } else {
                    _accountCreationStatus.value = Result.failure(task.exception ?: Exception("Failed to send verification email"))
                }
            }
    }
}