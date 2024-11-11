package com.example.schedulemaster.model

data class Task(
    val title: String,
    //storing in dd/MM/yyyy format for consistency
    val date: String,
    val time: String,
    val description: String,
    val location: String,
    val priority: String,
    val category: String
){

}