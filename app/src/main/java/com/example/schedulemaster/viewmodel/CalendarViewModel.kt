package com.example.schedulemaster.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schedulemaster.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CalendarViewModel : ViewModel() {

    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage


    fun loadTasksForDate(date: String) {
        if (userId.isEmpty()) {
            _errorMessage.value = "User is not authenticated."
            return
        }

        val tasksRef = databaseRef.child("users").child(userId).child("tasks").child(date)
        tasksRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskList = mutableListOf<Task>()
                if (snapshot.exists()) {
                    for (taskSnapshot in snapshot.children) {
                        val task = taskSnapshot.getValue(Task::class.java)
                        if (task != null) {
                            taskList.add(task)
                        }
                    }
                    _tasks.value = taskList
                } else {
                    _tasks.value = emptyList()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = "Error fetching tasks: ${error.message}"
            }
        })
    }
}