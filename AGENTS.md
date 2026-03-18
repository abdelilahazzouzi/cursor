# AGENTS.md

## Cursor Cloud specific instructions

Android app (Kotlin + Jetpack Compose). See `README.md` for features.

### Prerequisites

- JDK 17+ (JDK 21 works)
- Android SDK: `platforms;android-34`, `build-tools;34.0.0`
- Set `sdk.dir` in `local.properties` (gitignored)

### Commands

- **Build:** `./gradlew assembleDebug`
- **Lint:** `./gradlew lint`

### Notes

- No emulator in Cloud VM — build verification is primary validation.
- Room uses kapt; `annotationProcessorOptions` in `app/build.gradle.kts`.
- `local.properties` must exist with `sdk.dir=/home/ubuntu/android-sdk`.
