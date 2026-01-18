# OpenSky Flight Tracker (Jetpack Compose)

## About the Project

This Android application visualizes **live flight data** fetched from the **OpenSky Network API** on **Google Maps**, built using **Jetpack Compose** and modern Android development practices.

The app dynamically updates aircraft positions based on user interactions and **prioritizes performance**, especially when rendering a large number of map markers.

---

## Android Studio Version

If you encounter any dependency or version-related issues while running the application, please note that Android Studio Otter 3 was used during development.
The development process was carried out using this version, which was the latest Android Studio release as of January 16, 2026.

## Architecture

The project follows **Clean Architecture + MVVM** principles.

- Each layer has a **clear responsibility**
- Unnecessary dependencies between layers are **avoided**
- **UI**, **Domain**, and **Data** layers are strictly separated

### Layers Overview

#### Presentation (UI)
- Jetpack Compose
- StateFlow
- Google Maps Compose

#### Domain
- UseCases
- Domain models
- UI-specific model mappings

#### Data
- OpenSky API integration
- Repository pattern
- DTO → Domain model mapping

---

## Tech Stack

- Kotlin
- Jetpack Compose
- Google Maps Compose
- Coroutines & Flow
- Hilt (Dependency Injection)
- Retrofit & OkHttp
- Clean Architecture
- MVVM

---

## Authentication (Token Handling)

- Bearer token-based authentication for OpenSky services
- Tokens are **automatically refreshed** when expired
- All network requests are handled via a **centralized OkHttp interceptor**

---

## Application Features

- Display live flights on **Google Maps**
- Fetch aircraft based on **visible map bounds** (zoom & pan)
- Automatic data refresh after **10 seconds of inactivity**
- Country-based filtering via **dropdown menu**
- Interactive markers:
    - InfoWindow with flight details
    - Rotation based on aircraft heading
    - Smooth position updates using **linear interpolation (lerp)**

---

## Performance Considerations

Performance was a key focus during development:

- Designed to handle **hundreds of aircraft** on screen
- Avoided unnecessary recompositions and marker recreations
- Minor position changes are ignored using an **epsilon-based threshold**
- Marker animations are:
    - Optimized based on zoom level
    - Kept minimal when zoomed out

---

## Testing

- Unit Tests
- UI Tests

Core logic and UI behavior are covered with tests to ensure stability.

---

## Demo

The repository includes:

- Two emulator demo videos
    - Map interactions
    - Marker animations
    - Country filtering
    - InfoWindow usage

## Demo Videos


<table>
  <tr>
    <td align="center">
      <b>Map Interaction & Flight Updates</b><br/>
      <video src="https://github.com/user-attachments/assets/a26b96ca-794b-429e-8e35-81b35336f6e7" controls width="640" height="640></video>
    </td>
  </tr>
</table>


