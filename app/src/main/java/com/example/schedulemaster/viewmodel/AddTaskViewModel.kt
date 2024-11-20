package com.example.schedulemaster.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.schedulemaster.model.Category
import com.example.schedulemaster.model.Location
import com.example.schedulemaster.model.Priority
import com.example.schedulemaster.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.lifecycle.ViewModel

class AddTaskViewModel : ViewModel() {

    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun submitTask(title: String, date: String, time: String, description: String, location: Location, priority: Priority, category: Category) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val task = Task(title, date, time, description, location, priority, category)
            databaseRef.child("users").child(userId).child("tasks").push().setValue(task)
        }
    }
}
