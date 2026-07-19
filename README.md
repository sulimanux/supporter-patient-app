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

## Project Structure

```text
app/
├── auth/
├── chatting/
├── getLibraryContent/
├── notifications/
├── posts/
├── users/
└── util/
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
