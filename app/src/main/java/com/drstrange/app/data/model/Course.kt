package com.drstrange.app.data.model

data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val category: CourseCategory,
    val duration: String,
    val lessons: Int,
    val progress: Float,
    val difficulty: Difficulty,
    val enrolled: Boolean = false
)

enum class CourseCategory(val displayName: String) {
    ANATOMY("Anatomy"),
    PHARMACOLOGY("Pharmacology"),
    SURGERY("Surgery"),
    CARDIOLOGY("Cardiology"),
    NEUROLOGY("Neurology"),
    PEDIATRICS("Pediatrics"),
    EMERGENCY("Emergency Medicine")
}

enum class Difficulty(val displayName: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced")
}
