# AGENTS.md

## Cursor Cloud specific instructions

This is a Kotlin/Jetpack Compose Android app. See `README.md` for project overview and structure.

### Prerequisites

- JDK 17+ (JDK 21 works)
- Android SDK with `platforms;android-34` and `build-tools;34.0.0`
- `ANDROID_HOME` or `sdk.dir` in `local.properties` must point to the SDK

### Key commands

- **Build debug APK:** `./gradlew assembleDebug`
- **Lint check:** `./gradlew lint`
- Output APK is at `app/build/outputs/apk/debug/app-debug.apk`

### Notes

- No emulator or device is available in the Cloud VM, so the app cannot be run interactively. Build verification (`assembleDebug`) is the primary validation.
- The `local.properties` file (gitignored) must be created with `sdk.dir=/home/ubuntu/android-sdk` if the Android SDK is installed to that path.
- The Gradle wrapper jar is committed; no separate Gradle installation is needed.
