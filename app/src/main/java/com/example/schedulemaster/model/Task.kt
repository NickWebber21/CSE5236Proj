package com.example.schedulemaster.model

enum class Priority {
    LOW, MEDIUM, HIGH
}

enum class Category {
    WORK, PERSONAL, STUDY, FITNESS, OTHER
}

data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null
){
    constructor() : this(0.0, 0.0)
}

data class Task(
    val title: String,
    //storing in dd/MM/yyyy format for consistency
    val date: String,
    //storing in hh:MM format for consistency
    val time: String,
    val description: String,
    //check model above
    val location: Location,
    //check enum
    val priority: Priority,
    //check enum
    val category: Category
){
    constructor() : this("", "", "", "", Location(), Priority.LOW, Category.OTHER)
}