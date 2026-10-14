# TECHNICAL REQUIREMENTS DOCUMENT (TRD)
# Scroll Battle — Personal Anti-Doom-Scrolling Android App

**Document Version:** 1.0  
**Status:** Proposed — Pre-Development  
**Based On:** Scroll Battle Product Requirements Document v1.0  
**Platform:** Android  
**Primary Language:** Kotlin  
**UI Framework:** Jetpack Compose  
**Architecture:** Single-module, offline-first native Android application  
**Distribution:** Private APK installation for personal use  
**Backend:** None  
**Cloud Database:** None  
**Authentication:** None  
**Primary Daily Limit:** 50 Instagram Reels

---

# 1. TECHNICAL OVERVIEW

## 1.1 Purpose

This document defines the technical architecture, libraries, APIs, storage design, security requirements, development workflow, and deployment strategy for Scroll Battle.

The system will track Instagram scrolling activity, maintain a daily Reel counter, provide analytics, enforce a configurable daily limit where technically feasible, and display progress through Android home-screen widgets.

The application must work independently of internet connectivity and keep all personal usage data on the device.

## 1.2 Core Technical Principles

1. Native Android development.
2. Offline-first operation.
3. Local-only data persistence.
4. Minimal external dependencies.
5. Event-driven tracking instead of continuous polling.
6. No account system or backend.
7. Clear separation between detection, counting, storage, and presentation.
8. Testing before claiming accurate Reel detection.
9. Minimal battery and storage consumption.
10. User-controlled permissions and history deletion.

## 1.3 System Overview

The proposed application consists of the following components:

- Android user interface.
- Reel detection engine.
- Usage tracking component.
- Daily limit and policy engine.
- Local database.
- Analytics engine.
- Home-screen widget.
- Settings and permission management.
- Local notification or blocking mechanism.

All components will execute on the user's Android device.

---

# 2. FRONTEND STACK

## 2.1 Language: Kotlin

**Decision:** Use Kotlin.

Reasons:

- Officially supported for modern Android development.
- Integrates directly with Android APIs.
- Supports coroutines and structured asynchronous work.
- Works naturally with Jetpack Compose.
- Avoids the overhead of a JavaScript runtime or cross-platform bridge.

## 2.2 UI Framework: Jetpack Compose

**Decision:** Use Jetpack Compose for the main application interface.

Responsibilities:

- Dashboard.
- Daily and weekly analytics.
- Settings.
- Limit warnings.
- Permission instructions.
- Tracking status.
- History views.

The interface will use a dark theme with warm orange and gold accents, based on the supplied reference screenshots.

Suggested navigation:

1. You — personal dashboard and current daily progress.
2. Battle — daily limit and habit-control experience.
3. Progress — daily and weekly analytics.

The initial release should prioritize the dashboard, progress screen, and settings. Additional navigation destinations should only be added when they serve a clear purpose.

## 2.3 UI State Management

**Decision:** Use ViewModel, StateFlow, and immutable UI state.

Responsibilities:

- ViewModel exposes screen state.
- Repository supplies persisted values.
- StateFlow publishes updates to the UI.
- Compose observes state and redraws affected components.

The UI must never be the source of truth for the Reel count.

The authoritative count will reside in the local data layer.

## 2.4 Navigation

**Decision:** Use a lightweight navigation implementation.

Use Navigation Compose if it simplifies the application's screen flow. For a very small application, a simple state-driven navigation approach may be sufficient.

Avoid a complicated multi-module navigation framework.

## 2.5 Charts

**Decision:** Prefer lightweight custom Compose charts for the first release.

Required charts:

- Hourly scrolling activity.
- Daily Reel counts.
- Weekly comparison.

Use an external charting library only if it materially reduces implementation effort without adding excessive dependencies or maintenance burden.

---

# 3. BACKEND STACK

## 3.1 Backend Decision

**Decision: No backend in Version 1.**

Scroll Battle is a personal application that does not require accounts, shared data, or server-side computation.

The following services are not required:

- Firebase.
- Supabase.
- Node.js backend.
- REST API server.
- Cloud-hosted PostgreSQL.
- User authentication provider.
- Remote analytics platform.

## 3.2 Why No Backend?

A backend would introduce:

- Network dependencies.
- Additional security risks.
- Infrastructure and maintenance work.
- Potential recurring costs.
- A larger privacy surface.
- Unnecessary complexity.

All essential computation can be performed locally.

## 3.3 Local Business Logic

The Android application will perform the following tasks locally:

- Count detected Reel transitions.
- Calculate daily remaining allowance.
- Track scrolling sessions.
- Record timestamps.
- Calculate daily and weekly summaries.
- Evaluate limit thresholds.
- Refresh widget data.
- Maintain settings and history.

## 3.4 Future Backend Conditions

A backend should only be considered if a future version genuinely requires:

- Multi-device synchronization.
- Shared social challenges.
- Remote backups.
- Cross-device accounts.

These features are explicitly outside Version 1.

---

# 4. DATABASE AND LOCAL STORAGE

## 4.1 Primary Storage Strategy

Use two storage mechanisms according to the type of data.

| Data type | Technology | Reason |
|---|---|---|
| User settings | DataStore | Simple, typed local preferences |
| Reel event history | Room with SQLite | Structured records and date queries |
| Derived analytics | Calculated locally or cached selectively | Avoid unnecessary duplicated data |
| Temporary UI state | ViewModel | Not permanent storage |
| Widget display | Read from shared repository/database | One source of truth |

Room should only be introduced if detailed event history is required. If the implementation only stores small daily summaries, a simpler storage model may be enough.

## 4.2 Proposed Database Entities

### ReelEvent

Fields:

- id: Long
- timestamp: Long
- localDate: String
- detectionSource: String
- confidence: String
- sessionId: String, optional

Purpose:

Store detected Reel-transition events and the information required to audit counting behavior.

The confidence or detection-source fields should only be included if the detection engine can meaningfully populate them.

### DailySummary

Fields:

- localDate: String, primary key
- reelCount: Int
- scrollingDurationMs: Long
- sessionCount: Int
- trackingStatus: String
- lastUpdatedAt: Long

Purpose:

Provide efficient access to daily statistics and support historical analytics.

### Settings

Stored in DataStore:

- dailyLimit: Int, default 50
- warningThresholds: list of integers
- blockingEnabled: Boolean
- onboardingCompleted: Boolean
- trackingEnabled: Boolean
- optional retentionDays: Int

Permission state should be checked against Android system settings rather than permanently trusted from a stored Boolean.

## 4.3 Data Consistency

The application must prevent duplicate counting.

Recommended approach:

1. The detector emits a candidate event.
2. The tracking layer validates and deduplicates it.
3. The counting engine applies the counting rules.
4. The repository persists the accepted event and updates the daily summary transactionally where appropriate.
5. The UI and widget read the latest persisted state.

The implementation should avoid maintaining independent, conflicting counters in the UI, widget, and background service.

## 4.4 Date and Time Handling

Daily limits will use the device's local calendar day.

Requirements:

- Correct handling of local midnight.
- Correct daily-summary selection.
- Clear behavior when the time zone changes.
- No deletion of historical records when the counter resets.
- Consistent interpretation of event timestamps.

Store event timestamps as epoch milliseconds and retain the appropriate local date or enough information to calculate it consistently.

When the time zone changes, historical reporting must follow a documented rule rather than silently reassigning past events.

## 4.5 Storage Retention

The initial version should use compact records and avoid storing screenshots, videos, or raw screen content.

A default retention period is optional. If a retention policy is introduced, users must understand that older records will be deleted automatically.

Provide a manual option to delete all history.

---

# 5. AUTHENTICATION AND USER MANAGEMENT

## 5.1 Authentication

**Decision: No authentication.**

There will be no:

- Email/password login.
- Google sign-in.
- Account creation.
- Session tokens.
- User profile server.
- Password reset workflow.

The application is designed for one person on one device.

## 5.2 Identity Model

The Android installation itself represents the personal application instance.

Data is stored in the application's private sandbox and is not shared with other users or devices.

## 5.3 Optional Device Authentication

Biometric or device-credential protection may be considered in a future version if the user wants additional protection when opening the application.

It is not necessary for the initial MVP because the app stores behavioral statistics rather than authentication secrets.

---

# 6. ANDROID APIs AND PLATFORM INTEGRATIONS

## 6.1 UsageStatsManager

**Purpose:** Measure Instagram foreground usage and application activity.

Official reference:

https://developer.android.com/reference/android/app/usage/UsageStatsManager

Requirements:

- Use usage statistics to estimate Instagram usage duration.
- Use supported system settings to obtain usage access.
- Handle missing or revoked access.
- Avoid interpreting total Instagram usage as exact Reel-watching time.

Limitation:

UsageStatsManager does not expose Instagram's internal Reel counter.

## 6.2 AccessibilityService

**Purpose:** Investigate and, if feasible, detect relevant Instagram UI transitions and support an interruption mechanism.

Official reference:

https://developer.android.com/reference/android/accessibilityservice/AccessibilityService

Requirements:

- Request explicit user enablement.
- Explain the purpose of the service before directing the user to system settings.
- Inspect only information necessary for the declared functionality.
- Avoid collecting unrelated screen content.
- Avoid sending accessibility data externally.
- Deduplicate events.
- Handle changes in Instagram's UI.
- Detect service failure and permission revocation.

Technical caveat:

Accessibility events do not guarantee one event per Reel. A reliable counting strategy must be validated experimentally.

The application must not assume that every vertical swipe represents a Reel transition or that every Instagram screen is a Reels feed.

## 6.3 App Usage and Foreground Detection

Use supported Android mechanisms to determine whether Instagram is active and whether a tracking session should be paused or resumed.

Do not assume that a foreground service is necessary for every tracking task.

The final implementation should use the least resource-intensive supported approach that meets the reliability requirements.

## 6.4 Notifications

Use Android notifications only when they provide a meaningful warning or status message.

Potential uses:

- Approaching the daily limit.
- Limit reached.
- Tracking permission unavailable.

Request notification permission when required by the Android version.

Do not generate excessive notifications.

## 6.5 Android Home-Screen Widgets

**Preferred technology:** Jetpack Glance, subject to widget requirements.

Official references:

https://developer.android.com/develop/ui/compose/glance

https://developer.android.com/develop/ui/views/appwidgets

Requirements:

- Display the latest locally available count.
- Display the configured daily limit.
- Show the remaining allowance.
- Provide a progress indicator.
- Open the relevant app screen when tapped.
- Refresh after relevant data changes where practical.
- Tolerate delayed updates caused by system scheduling.

Widgets must not require a continuously running service.

## 6.6 WorkManager

Official reference:

https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started

Use WorkManager only for work that needs deferrable, persistent execution.

Potential uses include non-urgent maintenance or history cleanup.

Do not use WorkManager as a real-time Reel counter or assume that periodic work executes at exact times.

## 6.7 Network APIs

**Decision:** No application networking in Version 1.

The application should not require HTTP APIs, WebSockets, cloud endpoints, or remote analytics.

If no feature needs internet access, do not request the INTERNET permission in the application manifest.

Development tools may access the internet during dependency downloads and builds, but this does not require the installed app itself to transmit data.

---

# 7. SYSTEM ARCHITECTURE

## 7.1 Architecture Style

Use a single Android application module with clear internal layers.

Recommended structure:

- Presentation layer.
- Domain layer.
- Data layer.
- Platform integration layer.

Do not split the project into multiple Gradle modules until the codebase becomes large enough to justify the extra complexity.

## 7.2 Architecture Diagram

User Interface
↓
ViewModels and UI State
↓
Domain Use Cases
↓
Repository Interfaces
↓
Local Data Sources
↓
DataStore / Room / SQLite

Platform Integrations:

- AccessibilityService.
- UsageStatsManager.
- Android notification APIs.
- Widget APIs.

The tracking engine should send candidate events to the domain layer. The domain layer validates the events and updates local state through the repository.

The UI must not directly manipulate AccessibilityService internals or database tables.

## 7.3 Proposed Package Structure

com.scrollbattle
- app
- presentation
  - dashboard
  - progress
  - settings
  - components
- domain
  - model
  - repository
  - usecase
  - tracking
- data
  - local
  - database
  - datastore
  - repository
- tracking
  - accessibility
  - usage
  - detector
- blocking
- widget
- notifications
- permissions
- analytics

The package structure is logical rather than a requirement to create dozens of separate classes immediately.

## 7.4 Core Domain Components

### ReelDetectionEngine

Receives platform signals and identifies candidate Reel transitions.

### EventDeduplicator

Prevents multiple accessibility events from creating duplicate counts.

### ReelCountingPolicy

Applies counting rules and determines whether a candidate event should increment the daily total.

### DailyLimitPolicy

Determines:

- Current daily count.
- Remaining allowance.
- Warning thresholds.
- Whether the configured limit has been reached.

### SessionTracker

Tracks session start, pause, resume, and end times.

### AnalyticsCalculator

Calculates daily and weekly statistics from local records.

### LocalRepository

Persists events, summaries, and settings.

### WidgetUpdater

Refreshes widget presentation when relevant state changes and system constraints permit.

## 7.5 Source of Truth

Room or the selected local persistence mechanism will be the authoritative source for usage history.

DataStore will be the authoritative source for user preferences.

ViewModels and widgets will consume data from the shared data layer.

Do not create separate, independently maintained counters for each screen.

---

# 8. DAILY LIMIT AND BLOCKING LOGIC

## 8.1 Limit Evaluation

The daily limit engine should evaluate the accepted count after each valid event.

Example:

- Daily limit: 50
- Current count: 39
- Remaining allowance: 11
- Warning threshold: 40
- Blocking threshold: 50

The limit engine must be deterministic and independently unit-tested.

## 8.2 Blocking Mechanism

The application must investigate the most reliable supported mechanism for interrupting Instagram after the configured limit.

Potential mechanisms include:

- An accessibility-driven interruption.
- A transparent overlay, only where technically appropriate and permitted.
- A notification asking the user to stop.
- A system-supported application restriction mechanism, if available and suitable.

The final mechanism must be selected after reviewing Android API behavior, device compatibility, and relevant platform policies.

An overlay is not automatically available or appropriate on every device. Accessibility-based interruption is also not guaranteed to be tamper-proof.

## 8.3 Failure Behavior

If tracking stops, required permissions are revoked, or the service becomes unavailable:

- Mark tracking as unavailable.
- Explain the issue in the application.
- Do not silently claim that the daily limit is being enforced.
- Resume normal tracking only when the required conditions are restored.

The application must distinguish between a count of 50 and an inability to observe further activity.

---

# 9. SECURITY AND PRIVACY REQUIREMENTS

## 9.1 Data Protection

- Store data in Android's private application storage.
- Do not expose the database through public content providers.
- Do not log detailed personal usage histories in production.
- Avoid unnecessary exported components.
- Restrict component access through explicit Android manifest declarations.
- Do not transmit tracking information to external services.

## 9.2 Network Security

Version 1 should have no application-level networking.

If future features introduce networking, the team must explicitly review transport security, authentication, and data minimization before adding network permissions.

## 9.3 Accessibility Security

Accessibility access is highly sensitive.

Requirements:

- Explain exactly what the service does.
- Use only the minimum information required to identify relevant UI transitions.
- Avoid storing unrelated UI text or screenshots.
- Do not expose accessibility events through external interfaces.
- Do not transmit accessibility information.
- Allow the user to disable tracking.
- Detect when the service is disabled.

If Google Play distribution is pursued, review the applicable Accessibility API policies, disclosure requirements, and declaration requirements before release.

## 9.4 Backup and Data Deletion

Review Android backup behavior for the application's private data.

For this privacy-focused MVP, exclude usage history from cloud backup unless the user explicitly chooses a future backup feature.

History deletion should remove the relevant local records and cached summaries. Widget data must be refreshed so that deleted information is not left displayed unnecessarily.

## 9.5 Dependency Security

- Prefer maintained AndroidX libraries.
- Avoid unnecessary third-party dependencies.
- Review transitive dependencies.
- Avoid analytics SDKs and advertising SDKs.
- Keep dependency versions controlled.
- Run available static analysis and dependency vulnerability checks.

## 9.6 Secrets

The application should not require API keys, service-account credentials, or backend secrets.

Do not introduce secrets merely to support local tracking.

---

# 10. TESTING REQUIREMENTS

## 10.1 Unit Tests

Test:

- Daily count calculations.
- Event deduplication.
- Warning thresholds.
- Daily reset.
- Date-boundary handling.
- Session-duration calculations.
- Analytics calculations.
- History retention.
- Settings validation.

## 10.2 Integration Tests

Test:

- Room persistence.
- DataStore settings.
- Repository behavior.
- Widget data retrieval.
- Daily summary updates.
- Recovery after application restarts.

## 10.3 Real-Device Tests

Use the target Android phone to test:

- Instagram Reels transitions.
- Different Instagram feed types.
- Accessibility event behavior.
- Background and foreground transitions.
- Screen lock and unlock.
- Permission revocation.
- Force-stop and restart.
- Device reboot.
- Battery usage.
- Widget refresh behavior.
- Local-midnight reset.

## 10.4 Counting Accuracy

Before relying on the counter for blocking, test against a manually verified set of Reel transitions.

Record:

- Correct detections.
- Missed transitions.
- False positives.
- Duplicate events.
- Tracking failures.

A suggested initial acceptance threshold is 95% accuracy on a defined test set. If this threshold is not met, the app must present the counter as an estimate and avoid claiming reliable strict enforcement.

---

# 11. PERFORMANCE REQUIREMENTS

## 11.1 Application Size

Target a release APK below 15 MB if feasible.

This is a goal, not a guaranteed result. The final size depends on dependencies, Android resources, and packaging.

## 11.2 Database Size

Avoid media storage and unnecessary raw-event retention.

A small personal usage-history database should remain compact, but actual size must be measured over a defined retention period.

## 11.3 Battery

- Avoid constant polling.
- Avoid unnecessary wake locks.
- Avoid continuous high-frequency widget refreshes.
- Avoid keeping the UI process alive unnecessarily.
- Prefer event-driven mechanisms.
- Measure incremental battery consumption during controlled tests.

## 11.4 Responsiveness

- Dashboard should load from local state.
- Database operations should not block the main thread.
- Charts should use efficient queries and bounded datasets.
- UI updates should be limited to relevant state changes.

---

# 12. DEVELOPMENT ENVIRONMENT

## 12.1 Recommended Tools

- Android Studio.
- Kotlin.
- Gradle with Kotlin DSL.
- Android SDK.
- Jetpack Compose.
- AndroidX libraries.
- Git for version control.
- A physical Android phone for integration testing.

An emulator can support general UI and database testing, but it cannot fully replace a real phone for Instagram accessibility behavior, OEM-specific battery restrictions, and widget testing.

## 12.2 Code Quality

- Use Kotlin coroutines where appropriate.
- Use clear naming.
- Keep functions focused.
- Avoid unnecessary abstractions.
- Enable Kotlin and Android lint checks.
- Add tests for business-critical logic.
- Document platform limitations.

## 12.3 Build Configuration

Use separate debug and release configurations.

The debug build may include diagnostic logging and testing utilities.

The release build must remove unnecessary diagnostics, avoid development-only permissions, and use optimized packaging where appropriate.

---

# 13. DEPLOYMENT PLAN

## 13.1 Initial Distribution: Private APK

Because the app is for personal use, begin with a locally built APK.

Process:

1. Build the debug APK for early testing.
2. Install it on the target phone.
3. Test permissions and Reel detection.
4. Fix issues.
5. Build a signed release APK when ready.
6. Install and test the release build.
7. Retain the signing key securely for future updates.

No public hosting or server deployment is necessary.

## 13.2 Application Signing

Use a stable application ID and a consistent signing key.

Back up the signing key securely. Losing it may prevent straightforward in-place updates to an existing installation.

Do not commit signing credentials or private keys to source control.

## 13.3 Updates

For personal use, distribute updates by installing a newly signed APK with the same application ID and signing identity.

Test database migrations and settings compatibility before replacing an installed version.

## 13.4 Google Play Distribution

Public distribution is outside the initial scope.

Before any future Play Store release:

- Review Accessibility API policies.
- Complete required declarations.
- Provide the necessary disclosures.
- Meet target API requirements.
- Prepare privacy disclosures.
- Test installation and updates through the intended distribution channel.

Private sideloading simplifies distribution but does not remove Android's system restrictions.

---

# 14. TECHNICAL DECISIONS AND REASONS

| Decision | Selected approach | Reason |
|---|---|---|
| Platform | Native Android | Direct access to platform capabilities |
| Language | Kotlin | Modern Android development |
| UI | Jetpack Compose | Declarative UI with low implementation overhead |
| Architecture | Simple layered MVVM | Maintainable without excessive complexity |
| Backend | None | No shared or remote functionality required |
| Authentication | None | Single-user personal app |
| Settings storage | DataStore | Lightweight preference storage |
| Structured history | Room/SQLite, if needed | Local queries and historical analytics |
| Reel tracking | Validated accessibility-based approach plus usage stats where useful | No official Reel counter is exposed through standard usage statistics |
| Analytics | Local calculations | Privacy and offline operation |
| Widgets | Jetpack Glance or Android widget APIs | Native home-screen integration |
| Background work | Event-driven; WorkManager only when appropriate | Reduce unnecessary battery consumption |
| Networking | None in Version 1 | Smaller privacy and security surface |
| Deployment | Signed private APK | Simple personal installation |
| Monitoring | Local diagnostics and testing | Avoid third-party telemetry |
| Code structure | Single module initially | Simpler development and maintenance |

---

# 15. IMPLEMENTATION MILESTONES

## Milestone 1: Feasibility Prototype

- Confirm the target device and Android version.
- Test Reel detection.
- Evaluate available accessibility signals.
- Verify usage statistics access.
- Investigate blocking options.
- Measure preliminary battery impact.

Deliverable: a documented feasibility decision.

## Milestone 2: Application Foundation

- Create the Android project.
- Establish the Compose theme and navigation.
- Add settings storage.
- Add the repository layer.
- Create the dashboard.

Deliverable: an offline application shell.

## Milestone 3: Tracking Engine

- Implement event processing.
- Add deduplication.
- Persist history.
- Calculate daily counts and durations.
- Add daily-limit logic.

Deliverable: a working tracking prototype.

## Milestone 4: Enforcement

- Implement warnings.
- Implement the selected interruption mechanism.
- Handle permission revocation.
- Test failure and recovery behavior.

Deliverable: a validated personal limit workflow.

## Milestone 5: Analytics and Widgets

- Add daily and weekly views.
- Add hourly charts.
- Implement the home-screen widget.
- Verify refresh and rollover behavior.

Deliverable: a complete user-facing MVP.

## Milestone 6: Optimization and Release

- Measure APK size.
- Measure storage and battery usage.
- Run automated and real-device tests.
- Audit permissions and networking.
- Build and sign the release APK.

Deliverable: a privately installable Android application.

---

# 16. FINAL ARCHITECTURAL RECOMMENDATION

Build Scroll Battle as a small, native Android application using Kotlin, Jetpack Compose, local storage, and Android platform APIs.

Do not build a backend, authentication system, cloud database, or network-dependent analytics pipeline.

Keep the tracking engine independent from the UI so that Instagram-specific detection can be improved without rewriting the rest of the application.

The first technical milestone must validate whether Reel transitions can be detected accurately enough on the target device. If the detection mechanism is unreliable, the architecture should support an honest fallback to estimated counts and Instagram usage-duration tracking.

The application's success depends on reliable measurement, transparent enforcement, offline privacy, and low resource consumption—not on the number of frameworks or services it uses.