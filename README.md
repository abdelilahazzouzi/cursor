# Dr Strange - Medical Courses App

An Android application for medical education featuring courses, interactive simulations, progress tracking, and a comprehensive dashboard.

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose with Material 3
- **Navigation:** Navigation Compose
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)

## Features

| Screen | Description |
|--------|-------------|
| **Dashboard** | Welcome overview, stats, continue learning cards, recent activity |
| **Courses** | Browse/filter medical courses by category, enroll, track progress |
| **Simulation** | Interactive medical simulations with scoring and difficulty levels |
| **Progress** | Overall progress ring, achievements, per-course progress, weekly activity chart |

## Build

```bash
./gradlew assembleDebug
```

The debug APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

## Project Structure

```
app/src/main/java/com/drstrange/app/
├── MainActivity.kt          # Entry point
├── DrStrangeApp.kt           # Root composable with bottom navigation
├── navigation/
│   ├── Screen.kt             # Screen definitions with icons
│   └── AppNavigation.kt      # NavHost setup
├── ui/
│   ├── theme/                # Color, Typography, Theme
│   └── screens/
│       ├── dashboard/        # Dashboard screen
│       ├── courses/          # Courses list with filtering
│       ├── simulation/       # Simulation cards
│       └── progress/         # Progress tracking
└── data/
    ├── model/                # Course, Simulation data classes
    └── SampleData.kt         # Sample medical data
```
