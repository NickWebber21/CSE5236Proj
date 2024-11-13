package com.example.schedulemaster.model

enum class Priority {
    LOW, MEDIUM, HIGH
}

enum class Category {
    WORK, PERSONAL, STUDY, FITNESS, OTHER
}

data class Location(
    val latitude: Double,
    val longitude: Double
){
    constructor() : this(0.0, 0.0)
}

data class Task(
    val title: String,
    // Storing in dd/MM/yyyy format for consistency
    val date: String,
    // Storing in hh:MM format for consistency
    val time: String,
    val description: String,
    // Check model above
    val location: Location,
    // Check enum
    val priority: Priority,
    // Check enum
    val category: Category
){
    constructor() : this("", "", "", "", Location(), Priority.LOW, Category.OTHER)
}