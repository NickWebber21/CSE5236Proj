package com.example.schedulemaster.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authStatus = MutableLiveData<Boolean>()
    val authStatus: LiveData<Boolean> get() = _authStatus

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _verificationId = MutableLiveData<String?>()
    val verificationId: LiveData<String?> get() = _verificationId

    fun sendVerificationCode(phoneNumber: String, activity: Activity) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _errorMessage.value = e.message
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    _verificationId.value = verificationId
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // Verify the code entered by the user
    fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(_verificationId.value!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authStatus.value = true
                } else {
                    _authStatus.value = false
                    _errorMessage.value = task.exception?.message
                }
            }
    }

    // Handle email sign-in
    fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authStatus.value = true
                } else {
                    _authStatus.value = false
                    _errorMessage.value = task.exception?.message
                }
            }
    }
}