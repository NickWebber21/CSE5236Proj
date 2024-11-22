package com.example.schedulemaster.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CreateAccountViewModel : AuthViewModel() {
    private val _accountCreationStatus = MutableLiveData<Result<String>>()
    val accountCreationStatus: LiveData<Result<String>> get() = _accountCreationStatus

    private val _verificationStatus = MutableLiveData<Boolean>()
    val verificationStatus: LiveData<Boolean> get() = _verificationStatus

    fun createAccount(email: String, password: String, phoneNumber: String) {
        if (email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
            _accountCreationStatus.value = Result.failure(Exception("Email, password, or phone number cannot be empty"))
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _accountCreationStatus.value = Result.success("Account created successfully")
                } else {
                    _accountCreationStatus.value = Result.failure(task.exception ?: Exception("Unknown error"))
                }
            }
    }

    fun sendEmailVerification() {
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