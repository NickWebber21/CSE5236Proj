package com.example.schedulemaster.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class CreateAccountViewModel : ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _accountCreationStatus = MutableLiveData<Result<String>>()
    val accountCreationStatus: LiveData<Result<String>> get() = _accountCreationStatus

    protected val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

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