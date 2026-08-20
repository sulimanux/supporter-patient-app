# Tracking Features Documentation

This document describes the tracking strategy and implementation details for the Android application. 

## Tools We Use
* **Firebase Analytics**: We use Firebase Analytics as our primary tool to log custom events, session data, and user interactions across the application. 
* **BigQuery**: We export our raw Firebase Analytics event data to Google BigQuery. We use BigQuery to extract, query, and analyze this data to gain insights into user behavior and app performance.

## How We Track
* **Centralized Logging**: Tracking is implemented centrally through `BaseActivity` and `BaseFragment`. These classes provide a `logEvent(eventName: String, params: ParametersBuilder.() -> Unit)` wrapper method that passes data directly to `FirebaseAnalytics`.
* **User Properties**: User identity is attached to events by setting the User ID (`setUserId`) and user name (`setUserProperty`) upon providing the Firebase Analytics instance (configured in `AppModule.kt`).
* **Session Management**: We capture user session lifecycles using a `SessionManager` which logs `start_session`, `end_session`, and calculates `SESSION_DURATION`.

## Features We Track
We track user engagement and interaction across various core features in the app:

### 1. App Usage & Sessions
* **Session Tracking**: Start session, end session, and total session duration.
* **Text to Speech**: Usage of the text-to-speech accessibility feature across educational and spiritual content (`TEXT_TO_SPEECH`).

### 2. Daily Program 
* **Content Engagement**: Tracking when content is viewed, completed, and the total time spent (`CONTENT_ENGAGEMENT`, `CONTENT_COMPLETED`).
* **Challenges**: Tracking challenge selection, engagement time, and challenge completion (`CHALLENGE_SELECTED`, `CHALLENGES_ENGAGEMENT`, `CHALLENGING_COMPLETED`).
* **Program Engagement**: Overall daily program engagement, including at which points users stop (`DailyProgramEngagement`, `STOPPED_AT`).

### 3. Coffee Ideas / Thoughts Restructuring Tool
* **Intro & Paths**: Viewing intros, overall intro engagement, and specific paths chosen by the user (`INTRO_VIEWED`, `INTRO_ENGAGEMENT`, `PATH_CHOSEN`, `CHOOSE_PATH_ENGAGEMENT`).
* **Thought Logging**: Providing thoughts, selecting thought types, and time spent on logging (`THOUGHT_PROVIDED`, `THOUGHT_TYPE_SELECTED`, `THOUGHT_LOGGING_ENGAGEMENT`).
* **Reframing & Reflecting**: Answering questions, skipping optional inputs, reflecting, and rewriting thoughts (`QUESTION_ANSWERED`, `USER_SKIP_INPUT`, `REFLECT_ENGAGEMENT`, `REWRITE_THOUGHT`, `REFRAME_ENGAGEMENT`).
* **Friend Idea Editing**: Viewing responses, checking if there is a response, and response engagement (`RESPONSE_VIEWED`, `IS_THERE_RESPONSE`, `RESPONSE_ENGAGEMENT`).
* **Activity Scheduling**: Tracking when users schedule activities (`ACTIVITY_SCHEDULED`).

### 4. Mood Tracking
* **Pre-Mood Selection**: Tracking initial mood selection or when the user skips it.
* **Post-Assessment**: Engagement and time spent on post-assessment activities (`POST_ASSESSMENT_ENGAGEMENT`).
