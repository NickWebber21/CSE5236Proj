package com.example.schedulemaster.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class CreateAccountViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

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
                    _accountCreationStatus.value = Result.success("Account created successfully")
                } else {
                    _accountCreationStatus.value = Result.failure(task.exception ?: Exception("Unknown error"))
                }
            }
    }
}