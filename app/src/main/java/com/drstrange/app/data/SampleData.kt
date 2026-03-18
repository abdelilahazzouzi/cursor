package com.drstrange.app.data

import com.drstrange.app.data.model.Course
import com.drstrange.app.data.model.CourseCategory
import com.drstrange.app.data.model.Difficulty
import com.drstrange.app.data.model.Simulation

object SampleData {

    val courses = listOf(
        Course(
            id = 1,
            title = "Human Anatomy Fundamentals",
            description = "Comprehensive study of the human body systems, organs, and structures.",
            category = CourseCategory.ANATOMY,
            duration = "12 weeks",
            lessons = 48,
            progress = 0.75f,
            difficulty = Difficulty.BEGINNER,
            enrolled = true
        ),
        Course(
            id = 2,
            title = "Clinical Pharmacology",
            description = "Drug mechanisms, interactions, and therapeutic applications in clinical practice.",
            category = CourseCategory.PHARMACOLOGY,
            duration = "10 weeks",
            lessons = 36,
            progress = 0.45f,
            difficulty = Difficulty.INTERMEDIATE,
            enrolled = true
        ),
        Course(
            id = 3,
            title = "Surgical Techniques",
            description = "Essential surgical procedures, suturing techniques, and operative planning.",
            category = CourseCategory.SURGERY,
            duration = "16 weeks",
            lessons = 52,
            progress = 0.2f,
            difficulty = Difficulty.ADVANCED,
            enrolled = true
        ),
        Course(
            id = 4,
            title = "Cardiovascular Medicine",
            description = "Heart and vascular system pathology, diagnostics, and treatment protocols.",
            category = CourseCategory.CARDIOLOGY,
            duration = "14 weeks",
            lessons = 42,
            progress = 0f,
            difficulty = Difficulty.INTERMEDIATE
        ),
        Course(
            id = 5,
            title = "Neurological Assessment",
            description = "Nervous system examination techniques and neurological disorder diagnosis.",
            category = CourseCategory.NEUROLOGY,
            duration = "8 weeks",
            lessons = 28,
            progress = 0f,
            difficulty = Difficulty.ADVANCED
        ),
        Course(
            id = 6,
            title = "Pediatric Care Essentials",
            description = "Child health, development milestones, and pediatric disease management.",
            category = CourseCategory.PEDIATRICS,
            duration = "10 weeks",
            lessons = 32,
            progress = 0.9f,
            difficulty = Difficulty.BEGINNER,
            enrolled = true
        ),
        Course(
            id = 7,
            title = "Emergency Response Protocol",
            description = "Critical care procedures, triage, and emergency medical interventions.",
            category = CourseCategory.EMERGENCY,
            duration = "6 weeks",
            lessons = 24,
            progress = 0.6f,
            difficulty = Difficulty.INTERMEDIATE,
            enrolled = true
        ),
        Course(
            id = 8,
            title = "Advanced Cardiac Life Support",
            description = "ACLS algorithms, arrhythmia management, and resuscitation techniques.",
            category = CourseCategory.CARDIOLOGY,
            duration = "4 weeks",
            lessons = 16,
            progress = 0f,
            difficulty = Difficulty.ADVANCED
        )
    )

    val simulations = listOf(
        Simulation(
            id = 1,
            title = "Patient Assessment Drill",
            description = "Practice systematic head-to-toe patient assessment in an ER scenario.",
            category = CourseCategory.EMERGENCY,
            difficulty = Difficulty.BEGINNER,
            duration = "25 min",
            questionsCount = 20,
            bestScore = 85,
            completed = true
        ),
        Simulation(
            id = 2,
            title = "Drug Interaction Challenge",
            description = "Identify dangerous drug interactions in complex prescription scenarios.",
            category = CourseCategory.PHARMACOLOGY,
            difficulty = Difficulty.INTERMEDIATE,
            duration = "30 min",
            questionsCount = 25,
            bestScore = 72,
            completed = true
        ),
        Simulation(
            id = 3,
            title = "Cardiac Rhythm Analysis",
            description = "Interpret ECG strips and identify arrhythmias under time pressure.",
            category = CourseCategory.CARDIOLOGY,
            difficulty = Difficulty.ADVANCED,
            duration = "20 min",
            questionsCount = 15,
            bestScore = null,
            completed = false
        ),
        Simulation(
            id = 4,
            title = "Anatomy Identification Lab",
            description = "Identify anatomical structures from cadaver images and 3D models.",
            category = CourseCategory.ANATOMY,
            difficulty = Difficulty.BEGINNER,
            duration = "35 min",
            questionsCount = 30,
            bestScore = 92,
            completed = true
        ),
        Simulation(
            id = 5,
            title = "Surgical Decision Making",
            description = "Navigate pre-op, intra-op, and post-op decisions in virtual cases.",
            category = CourseCategory.SURGERY,
            difficulty = Difficulty.ADVANCED,
            duration = "45 min",
            questionsCount = 20,
            bestScore = null,
            completed = false
        ),
        Simulation(
            id = 6,
            title = "Pediatric Symptom Triage",
            description = "Assess and prioritize pediatric patients based on presenting symptoms.",
            category = CourseCategory.PEDIATRICS,
            difficulty = Difficulty.INTERMEDIATE,
            duration = "20 min",
            questionsCount = 18,
            bestScore = 88,
            completed = true
        )
    )
}
