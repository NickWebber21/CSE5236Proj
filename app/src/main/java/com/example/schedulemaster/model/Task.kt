package com.example.schedulemaster.model

enum class Priority {
    LOW, MEDIUM, HIGH
}

enum class Category {
    WORK, PERSONAL, STUDY, FITNESS, OTHER
}

data class Task(
    val title: String,
    // Storing in dd/MM/yyyy format for consistency
    val date: String,
    // Storing in hh:MM format for consistency
    val time: String,
    val description: String,
    val location: String,
    // Check enum
    val priority: Priority,
    // Check enum
    val category: Category
){

}