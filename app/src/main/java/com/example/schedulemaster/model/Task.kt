package com.example.schedulemaster.model

data class Task(
    val title: String,
    // Storing in dd/MM/yyyy format for consistency
    val date: String,
    // Storing in hh:MM format for consistency
    val time: String,
    val description: String,
    val location: String,
    val priority: String,
    val category: String
){

}