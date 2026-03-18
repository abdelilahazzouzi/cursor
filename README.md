# Banner Calculator

Android app for banner price calculation — designed for printing and graphics businesses. Calculates banner cost using dimensions, quantity, and price per square foot.

**Package:** `com.azizgraphics.clcltr`

## Features

| Screen | Description |
|--------|-------------|
| **Calculate** | Glass-style summary card, dynamic item cards with width/height (ft+in), quantity dropdown, material selector, price/sqft, real-time totals in PKR |
| **History** | Saved orders in expandable cards, dotted-grid dimension view, share as text/image, delete |
| **Settings** | Default unit/price, theme (System/Light/Dark), currency (PKR, USD, etc.), manage materials & customers, AZIZ-GRAPHICS about section |

## Tech Stack

- Kotlin 1.9.22, Jetpack Compose, Material 3
- Room (SQLite), DataStore Preferences
- Navigation Compose, Gson, FileProvider

## Build

```bash
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`
