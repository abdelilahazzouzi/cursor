package com.drstrange.app.data.model

data class Simulation(
    val id: Int,
    val title: String,
    val description: String,
    val category: CourseCategory,
    val difficulty: Difficulty,
    val duration: String,
    val questionsCount: Int,
    val bestScore: Int?,
    val completed: Boolean = false
)
