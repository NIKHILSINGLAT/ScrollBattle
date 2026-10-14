# PRODUCT REQUIREMENTS DOCUMENT (PRD)
# Scroll Battle — Personal Anti-Doom-Scrolling Android App

**Document Version:** 1.0  
**Status:** Planning / Pre-Development  
**Platform:** Android  
**Product Type:** Personal productivity and digital well-being application  
**Primary Goal:** Limit Instagram Reels scrolling to 50 Reels per day  
**Privacy Model:** Offline-first, local-only data storage  
**Development Approach:** Native Android application  
**Working App Name:** Scroll Battle

---

## 1. APP OVERVIEW

Scroll Battle is a personal Android application designed to help users reduce compulsive Instagram Reels scrolling by tracking their daily usage, enforcing a configurable daily Reel limit, and providing useful insights into their scrolling habits.

The application will operate locally on the user's Android phone. It will not require an account, cloud database, backend server, or internet connection for its core functionality.

The app will use a simple, modern, dark-themed interface inspired by the supplied reference screenshots. The design will feature warm orange and golden-yellow accents, readable typography, progress indicators, and minimal visual clutter.

The primary daily limit will be 50 Reels. Users should be able to see how many Reels they have consumed, how much time they have spent scrolling, and when they are approaching or have reached their limit.

The application will also provide daily and weekly analytics, Android home-screen widgets, and an optional blocking mechanism.

### Product Vision

Help users regain control of their attention by making their scrolling behavior visible, measurable, and easier to limit.

### Product Principles

1. Privacy first: personal usage data stays on the device.
2. Offline first: core functionality works without internet access.
3. Lightweight: minimize storage, battery consumption, and background processing.
4. Simple UX: the main screen should communicate progress immediately.
5. Honest tracking: distinguish verified Reel counts from estimates or uncertain detections.
6. User-controlled: provide clear settings and transparent permission requirements.
7. Reliable limits: do not claim perfect enforcement unless testing demonstrates it.

---

## 2. PROBLEM STATEMENT

Instagram Reels uses an endless vertical feed that makes it easy to continue watching short videos without noticing how much time has passed.

Users may open Instagram for a few minutes and end up scrolling through hundreds of Reels. This can interrupt studying, work, sleep, exercise, and other important activities.

Existing screen-time tools generally focus on total application usage rather than the number of individual Reels consumed.

The user needs a lightweight tool that answers five questions:

- How many Instagram Reels have I watched today?
- How much time have I spent scrolling?
- How close am I to my daily limit?
- At what times of day do I scroll the most?
- Can I prevent myself from continuing once I reach my limit?

### Core Problem

The user lacks a reliable, private, and convenient way to measure and control Instagram Reels consumption at the individual-Reel level.

### Important Technical Constraint

Android can report application usage, but it does not directly expose Instagram's internal Reel counter. Exact Reel detection may require accessibility-event analysis or another on-device detection strategy.

Instagram UI changes, app updates, different feed types, and system restrictions may affect counting accuracy. The implementation must validate its counting method on a real Android device before claiming that every Reel is counted correctly.

---

## 3. TARGET USERS

### Primary User

A single Android phone owner who wants to reduce personal Instagram Reels consumption and prefers keeping behavioral data private.

### Secondary Potential Users

- Students who want to reduce distractions while studying.
- Professionals who lose time to short-form video feeds.
- Content creators who need Instagram for work but want to limit passive consumption.
- People who want to understand their digital habits without creating online accounts.

### Initial Product Focus

Version 1 is designed for one person and one Android device.

The initial release will not require multiple users, social accounts, cloud synchronization, or a public community.

---

## 4. PRODUCT GOALS

### Primary Goals

1. Track Instagram scrolling activity on the user's device.
2. Support a default daily limit of 50 Reels.
3. Warn users as they approach their limit.
4. Provide a blocking or interruption mechanism when the limit is reached, subject to Android's technical capabilities and permissions.
5. Display daily and weekly scrolling statistics.
6. Provide useful Android home-screen widgets.
7. Keep usage history local and private.
8. Minimize application size and unnecessary battery consumption.

### Secondary Goals

- Encourage users to recognize their scrolling patterns.
- Make daily progress easy to understand.
- Help users gradually develop healthier digital habits.
- Make the application easy to maintain and extend.

### Non-Goals

The application is not intended to:

- Replace Instagram.
- Monitor other people's phones.
- Collect personal data for advertising.
- Upload behavioral data to a server.
- Provide medical diagnoses or treatment.
- Guarantee that users cannot bypass Android restrictions.

---

## 5. CORE FEATURES

### Feature 1: On-Device Reel Tracking

**Priority:** P0 — Critical

The app should detect relevant Instagram activity and estimate or count individual Reels viewed using a technically validated on-device approach.

Requirements:

- Detect when Instagram becomes active.
- Identify relevant Reel-viewing sessions.
- Count Reel transitions where reliable signals are available.
- Avoid counting repeated accessibility events for the same Reel transition.
- Record timestamps for detected Reel events where useful.
- Track Instagram foreground usage duration.
- Avoid counting unrelated Instagram activities, such as profile browsing, messaging, and static image posts, as Reels.
- Handle application pauses, background transitions, screen locking, and returning to Instagram.
- Persist tracking data locally.
- Recover gracefully from application restarts and Android process termination.
- Clearly communicate uncertainty when the system cannot distinguish Reels reliably.

The tracking implementation must be tested against the actual Instagram version installed on the user's phone.

**Acceptance Criteria:**

- Relevant Reel transitions increment the counter according to the validated counting rules.
- Duplicate accessibility events do not automatically create duplicate counts.
- Switching to another application pauses Reel-session tracking.
- Tracking resumes when the user returns to a relevant Instagram feed.
- Tracking survives normal app restarts without unnecessarily losing recorded history.

### Feature 2: Daily Reel Limit

**Priority:** P0 — Critical

The application must enforce a default daily target of 50 Reels.

Requirements:

- Set the default daily limit to 50 Reels.
- Display the current count and the remaining allowance.
- Warn the user at configurable milestones, initially 40 and 45 Reels.
- Trigger the limit experience when the count reaches 50.
- Provide an option to enable or disable blocking.
- Allow the user to change the daily limit through Settings.
- Reset the daily counter according to the device's local calendar day.
- Preserve historical daily records after the counter resets.

When the limit is reached, the application should present a clear blocking or interruption screen. The implementation must use a supported Android mechanism and request only the necessary permissions.

The blocking mechanism must be tested for reliability, including permission revocation, app restarts, and device reboots.

The app should not claim to provide an unbreakable block. The user may be able to bypass restrictions by disabling permissions, force-stopping the app, or changing system settings.

**Acceptance Criteria:**

- The default limit is 50.
- The remaining allowance is calculated correctly.
- The limit screen activates at the configured threshold when tracking is operating correctly.
- The daily count resets on the next local calendar day.
- Previous days' statistics remain available.

### Feature 3: Personal Dashboard

**Priority:** P0 — Critical

The dashboard is the primary screen of the application.

It should display:

- Reels watched today.
- Daily limit.
- Remaining Reels.
- Daily progress bar or circular progress indicator.
- Time spent in the tracked scrolling session.
- Current limit status.
- A shortcut to daily and weekly analytics.
- A shortcut to settings.

Example:

Reels Watched: 32 / 50  
Remaining: 18 Reels  
Time Spent: 24 minutes  
Daily Progress: 64%

The interface should remain simple, with the most important information visible without excessive scrolling.

**Acceptance Criteria:**

- The user can understand today's status within a few seconds.
- The dashboard updates when new tracking events are recorded.
- The dashboard clearly differentiates observed values from estimates.

### Feature 4: Daily and Weekly Analytics

**Priority:** P0 — Critical

The application should help the user understand when and how they scroll.

Daily analytics:

- Total Reels watched.
- Total tracked Instagram scrolling time.
- Number of sessions.
- Average session duration.
- Hourly activity distribution.
- Daily limit completion status.

Weekly analytics:

- Total Reels watched during the week.
- Total scrolling time.
- Daily averages.
- Highest-scrolling day.
- Lowest-scrolling day.
- Daily limit comparison.
- Trend compared with the previous week, when sufficient history exists.

All analytics must be calculated locally.

The app must clearly distinguish time spent in Instagram from time spent specifically watching Reels if only total app-usage data is available.

**Acceptance Criteria:**

- Daily and weekly totals are calculated from stored local records.
- Changing the selected date updates the displayed statistics.
- Historical records remain available after daily resets.
- Missing or uncertain data is not silently represented as exact.

### Feature 5: Local-Only Data Storage

**Priority:** P0 — Critical

All personal tracking information must remain on the user's device.

Requirements:

- Store usage history in private application storage.
- Store configuration locally.
- Avoid authentication and account creation.
- Avoid cloud databases and remote analytics.
- Avoid advertising SDKs.
- Do not send usage events to external services.
- Do not require internet access for core functionality.
- Provide an option to delete all stored history.
- Handle application updates without unnecessarily losing history.

Recommended storage strategy:

- Use Android DataStore for simple settings.
- Use Room with SQLite if detailed Reel-event history and historical analytics require structured records.
- Avoid introducing both complex and redundant storage mechanisms.

**Acceptance Criteria:**

- Core tracking, dashboard, analytics, and widgets work offline.
- Stored history remains available after normal application restarts.
- Deleting application data or uninstalling the app is understood to remove local history unless explicitly exported.
- No usage data is transmitted by the app's own networking or analytics code.

### Feature 6: Android Home-Screen Widgets

**Priority:** P1 — High

The application should provide a compact widget that allows the user to check their progress without opening the app.

Widget content:

- Reels watched today.
- Daily limit.
- Remaining allowance.
- Progress indicator.
- Last-updated information where appropriate.

Example:

SCROLL BATTLE

32 / 50 Reels  
18 remaining

Requirements:

- Support standard Android home-screen widget placement.
- Use the same local data as the dashboard.
- Update after relevant tracking changes where practical.
- Refresh appropriately after the daily reset.
- Avoid continuous background polling.
- Remain useful when the device is offline.

Widget updates may be delayed by Android launcher and background-execution constraints. The design must tolerate occasional stale values.

**Acceptance Criteria:**

- The widget can be added to the home screen.
- The displayed values match the latest locally available records.
- The widget updates without requiring continuous high-frequency background processing.

### Feature 7: Minimal Settings

**Priority:** P1 — High

Settings should include:

- Daily Reel limit.
- Blocking enabled or disabled.
- Warning thresholds.
- Permission setup and status.
- Daily reset behavior.
- Delete history.
- App version and basic information.

The app should explain why Accessibility or usage-access permissions are required before opening Android's relevant settings screens.

**Acceptance Criteria:**

- The user can change the daily limit without reinstalling the app.
- Permission status is checked before dependent functionality is presented as active.
- Missing permissions produce clear instructions rather than silent failure.

---

## 6. USER STORIES

### Tracking

- As a user, I want the app to count my Instagram Reels so that I know how much short-form content I consume.
- As a user, I want tracking to work without an internet connection so that my activity remains private.
- As a user, I want my count to survive normal app restarts so that I do not lose progress.
- As a user, I want the app to avoid counting unrelated Instagram screens so that my statistics are meaningful.

### Daily Limit

- As a user, I want a default limit of 50 Reels so that I have a clear daily boundary.
- As a user, I want warnings before reaching the limit so that I can stop voluntarily.
- As a user, I want a blocking or interruption screen at the limit so that continuing requires a deliberate action.
- As a user, I want the daily counter to reset automatically so that each day begins with a fresh allowance.

### Analytics

- As a user, I want to see my total scrolling time so that I understand how much time I spend on Instagram.
- As a user, I want hourly analytics so that I can identify when I scroll most.
- As a user, I want weekly comparisons so that I can determine whether my habits are improving.
- As a user, I want previous days' records to remain available so that I can review my progress.

### Widgets and Privacy

- As a user, I want a home-screen widget so that I can check my remaining allowance quickly.
- As a user, I want all data stored locally so that nobody else receives my usage history.
- As a user, I want to delete my data whenever I choose.
- As a user, I want the app to consume minimal battery and storage so that it remains practical for everyday use.

---

## 7. MVP SCOPE

The MVP should deliver one reliable personal workflow rather than attempting to build a complete digital-wellness platform.

### Must Have — P0

1. Native Android application.
2. Local-only operation.
3. Permission setup and status checks.
4. Instagram activity detection.
5. Validated Reel-counting prototype.
6. Default limit of 50 Reels.
7. Remaining-count calculation.
8. Daily reset.
9. Local persistence.
10. Basic dashboard.
11. Daily scrolling time.
12. Daily analytics.
13. Basic blocking or interruption mechanism, if technically feasible and validated.

### Should Have — P1

1. Weekly analytics.
2. Hourly activity chart.
3. Home-screen widget.
4. Customizable daily limit.
5. Warning thresholds.
6. History deletion.
7. Lightweight settings screen.

### Could Have — P2

1. Historical trend charts.
2. Streak tracking.
3. Optional manual correction of inaccurate counts.
4. Exporting personal history to a local file.
5. Additional widgets.

### Explicitly Outside the Initial MVP

- Cloud synchronization.
- Social accounts and friend battles.
- Public leaderboards.
- Multi-device synchronization.
- Subscription plans.
- Advertisements.
- AI-generated coaching.
- Multi-platform support.
- Complex gamification systems.

The core Reel-detection and enforcement feasibility test must happen before investing heavily in visual polish.

---

## 8. TECHNICAL REQUIREMENTS

### Recommended Technology Stack

- Language: Kotlin.
- UI: Jetpack Compose.
- Architecture: simple MVVM with a repository layer.
- Settings: Android DataStore.
- Structured history: Room and SQLite, if needed.
- Home-screen widgets: Jetpack Glance or the appropriate Android widget APIs.
- Background tasks: WorkManager only for work that genuinely needs scheduled execution.
- Reel tracking: a carefully tested Android AccessibilityService implementation, potentially complemented by UsageStatsManager.
- Charts: lightweight custom Compose charts or a small maintained chart library.

The final selection must be validated against the minimum Android version, current Android restrictions, and the installed Instagram version.

### Reel Detection Strategy

Do not assume that UsageStatsManager can count individual Reels.

It can help measure foreground application usage, while accessibility events may expose relevant UI transitions. Neither provides a guaranteed official Reel-view count.

The development team must:

1. Prototype detection against Instagram's real interface.
2. Identify usable and stable accessibility signals.
3. Deduplicate repeated events.
4. Test Reel transitions across relevant Instagram feeds.
5. Measure false positives and missed transitions.
6. Define an explicit accuracy threshold before enabling hard blocking.
7. Provide a fallback if reliable detection is not possible.

### Battery and Storage

Requirements:

- No continuous polling loop when event-driven tracking is possible.
- No unnecessary wake locks.
- No persistent network connection.
- No large media assets or bundled machine-learning models.
- Minimal database records.
- Sensible history retention.
- Efficient chart rendering.
- Measured battery and storage use on a real device.

### Privacy and Permissions

The app should request only permissions necessary for the features the user enables.

Accessibility access must be explicitly enabled by the user. Its purpose and behavior must be explained clearly.

Usage-access permission, if required, must also be requested through Android's supported settings flow.

The application must not collect screen contents unrelated to the stated purpose, transmit accessibility information, or introduce unnecessary telemetry.

If the app is distributed through Google Play in the future, its Accessibility API use must comply with the applicable Play policies and disclosures. Personal sideloading does not eliminate Android platform restrictions.

---

## 9. NON-FUNCTIONAL REQUIREMENTS

### Privacy

- Core functionality must not depend on a network connection.
- Personal usage history must remain local.
- No third-party analytics or advertising SDKs.
- Clear permission explanations.
- User-controlled history deletion.

### Performance

- Fast dashboard loading.
- Responsive navigation.
- Efficient database queries.
- Minimal background execution.
- No unnecessary continuous wake-ups.

### Reliability

- Correct daily rollover.
- Duplicate-event prevention.
- Graceful recovery after process termination.
- Clear handling of disabled permissions.
- Data consistency across app restarts.
- Explicit handling of uncertain Reel detection.

### Usability

- Clear typography.
- High contrast.
- Large touch targets.
- Minimal settings.
- Consistent navigation.
- Accessible labels and controls.
- No unnecessary onboarding steps.

### Maintainability

- Small number of dependencies.
- Clear separation of UI, tracking, storage, and analytics.
- Unit tests for counting rules and date calculations.
- Integration tests for persistence.
- Real-device tests for accessibility behavior and widgets.

---

## 10. SUCCESS METRICS

The following are proposed targets for the initial testing phase. They are not claims about existing performance.

### Tracking Metrics

- Target: at least 95% Reel-transition counting accuracy in a predefined test set before treating the count as suitable for strict blocking.
- Target: fewer than 1% duplicate counts in controlled tests.
- Target: no unexplained loss of persisted records during normal app restarts.
- Measure: false positives and missed Reel transitions separately.

The test set should include at least 500 manually verified Reel transitions across repeated sessions and relevant Instagram feed types.

### Daily Limit Metrics

- Target: correct daily-limit calculations in all automated boundary tests.
- Target: correct reset behavior across local midnight, time-zone changes, and device restarts.
- Measure: the percentage of test sessions in which blocking activates at the intended threshold.
- Measure: the percentage of sessions in which restrictions are bypassed or tracking stops unexpectedly.

### Performance Metrics

- Target: release APK size below 15 MB, if achievable without sacrificing required functionality.
- Target: local history for one year below 20 MB under the defined retention policy.
- Target: no unnecessary continuous polling.
- Measure: incremental battery drain over a controlled 24-hour test.
- Measure: cold-start time and memory usage on the target phone.

These targets must be validated on the actual device rather than assumed from development-machine tests.

### Product Metrics

- Number of days the user stays at or below 50 Reels.
- Average daily Reel count.
- Average daily scrolling duration.
- Percentage change in weekly Reel consumption.
- Percentage of days the limit is reached.
- Number of times the user voluntarily stops after a warning.

Because the app is designed for one person, success should be measured by improved personal habits and reliable behavior, not user acquisition or engagement maximization.

---

## 11. FEATURES TO AVOID IN VERSION 1

Do not build the following until the core tracking and limiting workflow is reliable.

### Avoid Social Features

- Friends lists.
- Multiplayer battles.
- Leaderboards.
- Friend invitations.
- Public profiles.
- Social sharing requirements.

These introduce complexity without solving the primary problem.

### Avoid Cloud Infrastructure

- User registration.
- Login systems.
- Firebase authentication.
- Cloud database synchronization.
- Remote analytics.
- Backend APIs.

The app must remain useful without a server.

### Avoid Excessive Gamification

- Virtual currencies.
- Complex rewards.
- Achievement marketplaces.
- Avatars and character customization.
- Daily missions unrelated to scrolling control.

A simple progress indicator is sufficient initially.

### Avoid Heavy Features

- AI coaching models.
- Large on-device language models.
- Video recording or screenshot archives.
- Continuous screen recording.
- Unnecessary background polling.
- Complicated reporting dashboards.

### Avoid Unsupported Enforcement Claims

- Claiming 100% Reel-counting accuracy without evidence.
- Claiming that Android restrictions cannot be bypassed.
- Assuming all Instagram feeds expose identical accessibility events.
- Assuming that screen-time data equals individual Reel counts.

### Avoid Premature Distribution Work

- Public launch campaigns.
- Subscription billing.
- Advertising integrations.
- Multi-language support beyond what is initially needed.
- Complex CI/CD infrastructure for a personal prototype.

---

## 12. KEY RISKS AND MITIGATIONS

### Risk 1: Instagram UI changes

Instagram may change its accessibility structure or feed behavior.

Mitigation: isolate detection logic, test after Instagram updates, and detect when tracking signals become unreliable.

### Risk 2: Inaccurate Reel counting

Some transitions may not produce stable or unique accessibility events.

Mitigation: build a detection prototype and measure accuracy before relying on the count for enforcement.

### Risk 3: Android background restrictions

The operating system may restrict background execution, and users can revoke permissions.

Mitigation: use supported event-driven mechanisms, detect missing permissions, and provide clear recovery instructions.

### Risk 4: Battery consumption

Continuous polling or frequent background work could increase battery use.

Mitigation: avoid unnecessary polling, minimize wake-ups, and measure real-device consumption.

### Risk 5: Blocking can be bypassed

Users can potentially disable the service or force-stop the app.

Mitigation: make restrictions transparent and useful without claiming that they are impossible to bypass.

### Risk 6: Widget data becomes stale

Android may delay widget updates.

Mitigation: refresh on relevant events and scheduled opportunities, and tolerate temporarily stale data.

### Risk 7: Privacy expectations are violated

A dependency could introduce unexpected networking or telemetry.

Mitigation: audit dependencies and manifest permissions, avoid analytics SDKs, and verify that the app makes no unnecessary network requests.

---

## 13. DEVELOPMENT PHASES

### Phase 1: Feasibility Research

- Confirm target Android version.
- Inspect the installed Instagram UI.
- Prototype Reel detection.
- Test AccessibilityService and UsageStatsManager behavior.
- Assess permission and enforcement constraints.
- Decide whether the 50-Reel limit can be enforced reliably enough.

Deliverable: a documented technical feasibility decision.

### Phase 2: Core Foundation

- Create the native Android project.
- Implement the basic UI and navigation.
- Implement local settings storage.
- Implement structured local history if required.
- Add unit tests for the counting model and daily reset.

Deliverable: a functional offline application shell.

### Phase 3: Tracking and Limit

- Implement validated event detection.
- Deduplicate transitions.
- Persist counts.
- Implement daily-limit calculations.
- Add warnings and the interruption or blocking mechanism.
- Test app restarts and permission loss.

Deliverable: a working tracking and limiting prototype.

### Phase 4: Analytics and Widgets

- Add daily statistics.
- Add weekly statistics.
- Add hourly activity charts.
- Implement the home-screen widget.
- Validate refresh behavior.

Deliverable: a complete personal dashboard and analytics experience.

### Phase 5: Reliability and Optimization

- Run real-device tests.
- Measure APK size, memory, and battery consumption.
- Test local-midnight behavior.
- Test offline operation.
- Audit network requests and dependencies.
- Fix critical defects.

Deliverable: a reliable personal APK suitable for daily use.

---

## 14. ACCEPTANCE CRITERIA FOR MVP RELEASE

The first version is ready for personal use only when:

1. The app installs and launches successfully on the target Android phone.
2. Core features work offline.
3. The app records usage history locally.
4. Reel detection meets the predefined test accuracy threshold, or the app clearly labels counts as estimates.
5. The default limit is 50 Reels.
6. Daily counters reset correctly without deleting history.
7. Daily scrolling time is displayed with appropriate caveats.
8. The blocking mechanism behaves as documented when the limit is reached.
9. Missing permissions are clearly communicated.
10. Daily analytics work correctly.
11. The home-screen widget displays locally stored information.
12. The app does not transmit personal usage data.
13. Storage and battery consumption have been measured.
14. No known critical defect remains in the main tracking and limiting workflow.

If accurate Reel counting proves infeasible with available Android signals, release a clearly labeled usage-tracking prototype rather than presenting unreliable counts as exact.

---

## 15. FINAL PRODUCT DECISION

Build Scroll Battle as a small, native, offline-first Android application focused on three outcomes:

1. **Awareness:** know how much Instagram Reels content is being consumed.
2. **Control:** work toward a daily limit of 50 Reels.
3. **Insight:** understand personal scrolling patterns through simple analytics.

Use Kotlin, Jetpack Compose, local persistence, and Android's supported accessibility and widget mechanisms.

Prioritize feasibility testing, accurate counting, privacy, and low resource consumption before adding advanced features.

The MVP is successful when the user can trust the available measurements, understand their daily behavior, and use the app consistently without unnecessary network access, storage, or battery consumption.