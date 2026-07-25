# Supporter & Patient Android Application

## Comprehensive Codebase Dependencies Report

- Target SDK Version: 33
- Compile SDK Version: 34
- Gradle Modules Analyzed:
  - `:app` (Android Application)


This document lists, analyzes, and explains all external dependencies, compiler plugins, classpaths, and frameworks declared within the Support & Patient Android App workspace.

## 1. Build Plugins & Classpaths

| Plugin | Version | Module |
|--------|---------|--------|
| com.android.application | 8.3.2 | :app |
| com.android.library | 8.1.0 | :data |
| org.jetbrains.kotlin.android | 2.0.20 | :app, :data |
| kotlin-parcelize | Implicit | :app |
| com.google.gms.google-services | 4.4.0 (classpath), 4.3.15 (plugin) | :app |
| androidx.navigation.safeargs.kotlin | 2.7.3 | :app |
| com.google.devtools.ksp | 2.0.21-1.0.27 | :app |
| com.google.dagger.hilt.android | 2.51.1 | :app |
| com.google.firebase.crashlytics | 3.0.5 | :app |

## 2. Jetpack Core & AndroidX Libraries

Key libraries include:
- androidx.core:core-ktx
- androidx.appcompat:appcompat
- com.google.android.material:material
- androidx.constraintlayout:constraintlayout
- androidx.activity:activity
- androidx.recyclerview:recyclerview
- androidx.lifecycle libraries
- androidx.navigation libraries
- androidx.work:work-runtime-ktx

## 3. Firebase Suite & Identity Providers

- Firebase BoM 33.11.0
- Firebase Analytics
- Firebase Authentication
- Firebase Realtime Database
- Firebase Firestore
- Firebase Storage
- Firebase Messaging
- Firebase In-App Messaging
- Firebase Crashlytics NDK
- FirebaseUI Authentication
- FirebaseUI Database

## 4. Room Persistence Library

- room-runtime 2.6.1
- room-ktx 2.6.1
- room-compiler 2.6.1 (KSP)

## 5. Dependency Injection

- Hilt Android 2.51.1
- Hilt Compiler 2.51.1

## 6. Media, Utilities, Networking & Visual Components

- Kotlin Coroutines 1.7.3
- ExoPlayer 2.16.1
- Glide 4.16.0
- Gson 2.8.6
- OkHttp 4.12.0
- Material Calendar View 1.4.3
- Dots Indicator 4.3

## 7. Testing Libraries

- JUnit 4.13.2
- Google Truth 1.4.1
- AndroidX Test JUnit
- Espresso Core
- AndroidX Test Core KTX

## 8. Project Architecture (Modified Clean Architecture)

The **Sanad (سند)** application is built using a highly pragmatic, modular, and modern version of **Clean Architecture**. It aligns with **Screaming Architecture** principles by organizing code package structures around distinct domain features rather than technical layers.

### Architecture Diagram

This diagram represents the structural layers, dependency direction, and data pipelines for each feature:

```text
┌─────────────────────────────────────────────────────────────────────────┐
│                           Presentation Layer                            │
│  (Fragment / Activity / RecyclerView.Adapter) ──> UI Display & Events   │
└────────────────────────────────────┬────────────────────────────────────┘
                                     │ (Observes LiveData Streams)
                                     ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                    ViewModel (State & Business Logic)                   │
│  - Extends BaseViewModel / ViewModel                                    │
│  - Uses Kotlin Coroutines (viewModelScope) for async execution          │
│  - Directly injects Repositories (Hilt DI)                              │
└────────────────────────────────────┬────────────────────────────────────┘
                                     │ (Calls Repository APIs)
                                     ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                             Data Layer (Repo)                           │
│  - Repositories (e.g., ChattingRepo, AuthRepo, LibraryContentRepo)      │
│  - Coordinates Cloud (Firebase) and Local Storage (Room DB & Prefs)     │
└──────┬─────────────────────────────┬──────────────────────────────┬─────┘
       │ (Fetch / Real-time Sync)    │ (Local Cache / Offline)      │ (Session Management)
       ▼                             ▼                              ▼
┌──────────────┐              ┌──────────────┐               ┌──────────────┐
│   Firebase   │              │   Room DB    │               │ Shared Prefs │
│  Firestore   │              │ Local SQLite │               │  Session /   │
│  Auth, FCM   │              │  DAOs & Ent. │               │ Local Config │
└──────────────┘              └──────────────┘               └──────────────┘
```

### Layer Responsibilities

The codebase organizes files inside the `:app` module using a **Feature-by-Feature packaging** strategy. Each package (e.g., `chatting`, `auth`, `getLibraryContent`, `posts`, and roles under `users`) contains the following layers internally:

#### A. Presentation Layer
*   **Components:** `Activities`, `Fragments`, `RecyclerView.Adapters`, and `ViewModels`.
*   **Responsibilities:**
    *   **UI Rendering:** Receives structured state/data streams from the `ViewModel` and displays them cleanly to the user.
    *   **Event Handling:** Listens to user inputs (such as sending a chat message, submitting mood logs, or adjusting breathing timer durations) and forwards them to the `ViewModel`.
    *   **State Observation:** Observes `LiveData` or `StateFlow` streams from the `ViewModel` to update UI elements reactively.

#### B. Modified Domain / Business Logic Layer (Pragmatic Modification)
*   **Standard Clean Architecture:** Typically includes a separate `Domain` module/package containing fine-grained "Use Cases" or "Interactors" (e.g., `GetMessagesUseCase`, `SendMessageUseCase`) for every business transaction.
*   **Sanad App Modification:** To eliminate excessive boilerplate code, **the traditional UseCase layer has been bypassed**. Business logic and coordinator operations are handled directly inside the **ViewModels** and **Repositories**. ViewModels inject Repositories directly (via Hilt), reducing class sprawl while keeping business logic clean, cohesive, and easily traceable.

#### C. Data Layer
*   **Components:** `Entities` (Data classes, Database/Firestore models), `DAOs` (Data Access Objects for Room DB), and `Repositories` (implementations ending with `Repo` or `Repository`).
*   **Responsibilities:**
    *   **Single Source of Truth:** Coordinates requests between remote services and local databases.
    *   **Local Caching (Offline Support):** Implementations like `LibraryContentRepo` check the local Room Database first. If empty, they fetch data from Firebase Firestore, cache it locally in Room, and return it. This provides an instant, offline-first experience.
    *   **Data Sources Integration:**
        *   **Firebase Firestore, Auth & Cloud Messaging:** Powers real-time capabilities (instant chatting, patient-supporter linking, community posts, notifications, and auth sessions).
        *   **Room DB:** Stores offline-first, persistent entities like behavioral activation tasks, calendar activities, and cached articles.
        *   **SharedPreferences:** Manages user configuration, preferences, and session tokens.

#### D. Dependency Injection (DI) Layer
*   **Components:** Hilt Modules (located in local `di` or `DI` packages within each feature).
*   **Responsibilities:** Decouples object instantiation from usage. Hilt modules provide singletons and scoped instances of Firebase instances, Room databases, and Repositories to ViewModels automatically, making the codebase highly testable and modular.

### Key Architectural Decisions

1.  **Screaming Architecture (Feature-by-Feature Packaging):**
    Instead of standard horizontal layers at the root level (`data`, `domain`, `presentation`), the root packages represent features: `auth`, `chatting`, `posts`, `notifications`, `getLibraryContent`, etc. This makes the architecture "scream" its domain purpose (Mental Health Support App) rather than its technical details.
2.  **Role-Based Separation:**
    The system roles are separated in the folder hierarchy under `com.app.sanad.users`.
    *   `patient/` houses modules specific to patient therapeutic tools (like `moodTracking`, behavioral `dailyprogram`, `breathing`, and `gratitude`).
    *   `supporter/` isolates tools built exclusively for the caregiver (like monitoring reports, patient profile overviews, and custom coping ideas).
    This segregation prevents role-specific code from overlapping, ensuring long-term code safety and maintenance.
3.  **Real-time Optimization (No Use Case Bloat):**
    Since Firebase operates on persistent real-time listeners, mapping individual stream updates through multiple nested Use Case classes creates unnecessary mapping layers. By binding ViewModels directly to Repositories, the application maintains peak reactivity with minimal code friction.

### Project Directory Structure

```text
app/src/main/java/com/app/sanad/
├── app/                      # Main Application, Global DB, Hilt AppModule
├── auth/                     # Authentication & onboarding flow (data, presentation, di)
├── base/                     # Core base classes for Activities, Fragments, and ViewModels
├── chatting/                 # Real-time Messaging (data, presentation, di)
├── getLibraryContent/        # Psychoeducation Library & articles (data, presentation, di)
├── interfaces/               # Common listeners and callback interfaces
├── model/                    # Shared model objects
├── notifications/            # Push notification triggers (data, presentation, di)
├── numbersHelping/           # Emergency/hotline list support
├── posts/                    # Feed posts & updates sharing (data, presentation, di)
├── users/                    # Divided by Role:
│   ├── patient/              # Patient tools (calender, dailyprogram, moodTracking, points, profile, supporters, tools)
│   └── supporter/            # Supporter tools (cofe/cafe, partner/patient status, profile)
└── util/                     # Shared utilities (SessionManager, SharedPreferencesManager, etc.)
```

## Firestore Schema

### daily_program
Contains:
- behaviorActivation
- educational
- spiritual

Each item stores:
- ageGroup
- arDescription
- arTitle
- enDescription
- enTitle
- gender
- id
- image
- link
- religion
- type

### supplications
- User-created supplications
- Application supplications

Fields:
- name
- number

### profiles_users
Fields include:
- email
- id
- name
- partnerEmail
- partnerId
- imageUser
- gender
- ageGroup
- religion
- token
- invitationCode
- supportersNumber
- hasPartner
- status
- currentDay
- privacy settings

### posts
Contains:
- posts array
- supports
- seen_list
- gratitude objects

### images
- imageUrl

### chats
Messages include:
- seenByCaregiver
- seenByPatient
- senderId
- timeStamp
- text

Metadata includes:
- idCaregiver
- idPatient
- caregiver and patient names
- profile images

## Realtime Database

### library_contents
Stores:
- titles
- descriptions
- text content
- media URLs
- ratings
- view counts
- religious filtering metadata

## Room Database

### Task Entity

| Field | Type |
|-------|------|
| day | String |
| nameTask | String |
| image | Integer |
| isCompleted | Boolean |
| description | String |

## Build Requirements

- Compile SDK: 34
- Target SDK: 33
- Kotlin: 2.0.20
- Android Gradle Plugin: 8.3.2
