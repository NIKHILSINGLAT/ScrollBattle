# SCROLL BATTLE
# COMPLETE UX FLOW AND SCREEN SPECIFICATION

**Document Type:** UX Flow Document  
**Version:** 1.0  
**Status:** Ready for Implementation Planning  
**Platform:** Android  
**Application Type:** Personal, Offline-First Anti-Doom-Scrolling App  
**Primary Framework:** Kotlin + Jetpack Compose  
**Default Daily Limit:** 50 Instagram Reels  
**Data Storage:** Local device storage only  
**Backend:** None  
**Authentication:** None

---

# 1. DOCUMENT PURPOSE

This document defines every required screen, user action, button behavior, navigation path, success state, error state, and empty state for Scroll Battle.

The implementation agent must follow these specifications rather than inventing additional flows or features.

If a behavior depends on Android permissions, Instagram UI detection, or operating-system restrictions, the agent must implement the documented fallback rather than assume that the operation always succeeds.

## 1.1 Product Goal

Help a single Android user understand and control their Instagram Reels consumption through:

- A default limit of 50 Reels per day.
- On-device tracking.
- Daily and weekly analytics.
- A clear daily progress indicator.
- A blocking or interruption experience after reaching the limit, if the chosen mechanism is technically feasible.
- Android home-screen widgets.
- Completely local usage history.

## 1.2 Global UX Rules

1. No login, registration, or onboarding account creation.
2. No internet connection required for core features.
3. No advertisements or third-party analytics.
4. No cloud synchronization.
5. Never silently reset historical data.
6. Never display an unverified Reel count as guaranteed accurate.
7. Never indicate that blocking is active when the required tracking mechanism is unavailable.
8. Always provide a clear explanation when a permission is missing.
9. Never use a fake success state after an Android system action fails.
10. All daily statistics use a documented local-calendar-day policy.
11. Destructive actions require confirmation.
12. All buttons must have defined behavior.

---

# 2. GLOBAL APPLICATION NAVIGATION

The application uses three primary destinations:

1. **You** — dashboard and current daily status.
2. **Battle** — daily limit, warnings, and limit-reached experience.
3. **Progress** — historical statistics and trends.

Settings and permission setup are secondary destinations accessed from the dashboard.

## 2.1 Navigation Structure

```text
App Launch
    |
    +-- First Launch
    |      |
    |      +-- Welcome
    |             |
    |             +-- Privacy and Tracking Explanation
    |                    |
    |                    +-- Permission Setup
    |                           |
    |                           +-- Tracking Readiness
    |                                  |
    |                                  +-- Dashboard
    |
    +-- Returning Launch
           |
           +-- Dashboard
                  |
                  +-- You
                  |    +-- Tracking Status
                  |    +-- Settings
                  |    +-- Permission Setup
                  |
                  +-- Battle
                  |    +-- Daily Limit
                  |    +-- Limit Reached
                  |
                  +-- Progress
                       +-- Daily Analytics
                       +-- Weekly Analytics
                       +-- Day Details
```

The daily-limit screen is a normal application destination. A system-supported interruption or overlay, if implemented, is a separate enforcement mechanism.

## 2.2 Bottom Navigation

The bottom navigation bar appears on the three primary destinations.

### Tab 1: You

- Icon: person or home-style icon.
- Label: You.
- Action: Opens the dashboard.
- Selected state: Highlighted icon and label.
- Repeat tap: Returns to the top of the dashboard without resetting any data.

### Tab 2: Battle

- Icon: target or shield icon.
- Label: Battle.
- Action: Opens the daily-limit screen.
- Selected state: Highlighted icon and label.
- Repeat tap: Returns to the top of the Battle screen.

### Tab 3: Progress

- Icon: chart icon.
- Label: Progress.
- Action: Opens analytics.
- Selected state: Highlighted icon and label.
- Repeat tap: Returns to the default analytics view.

### Navigation Persistence

- Switching tabs must not reset counters.
- Switching tabs must not discard settings changes already saved.
- The application must preserve the selected date while navigating between analytics subviews when appropriate.
- Android Back returns to the previous screen or previous selected destination according to the navigation stack.
- Android Back from a primary destination follows the application's normal exit behavior; it must not clear history or stop tracking.

---

# 3. DESIGN SYSTEM AND COMMON COMPONENTS

## 3.1 Visual Direction

Use a minimal dark interface inspired by the supplied screenshots.

Design characteristics:

- Dark charcoal or near-black background.
- Warm orange primary accent.
- Golden-yellow secondary accent.
- White primary text.
- Muted gray secondary text.
- Rounded cards.
- Clear spacing.
- Readable numeric statistics.
- Minimal decoration.
- No excessive animations.

The exact colors must be defined as reusable theme tokens rather than repeated as hard-coded values.

## 3.2 Typography Hierarchy

- Display text: Daily Reel count.
- Heading: Screen titles.
- Body: Supporting information.
- Caption: Dates, status explanations, and secondary metrics.
- Button text: Short, action-oriented labels.

Text must remain readable at increased Android font sizes.

## 3.3 Shared UI Components

The implementation should reuse the following components.

### DailyProgressCard

Displays:

- Reels watched.
- Daily limit.
- Remaining allowance.
- Progress indicator.
- Current status.

### TrackingStatusCard

Displays one of:

- Tracking active.
- Tracking limited.
- Permission required.
- Tracking unavailable.
- Detection accuracy not validated.

### EmptyState

Displays:

- A short explanation.
- An optional relevant icon.
- A clear next action where one exists.

### ErrorState

Displays:

- What failed.
- Whether tracking or enforcement is affected.
- A recovery action.

### ConfirmationDialog

Used for:

- Deleting history.
- Resetting settings.
- Disabling blocking, when confirmation is warranted.

### LoadingState

Used only while real asynchronous work is pending.

The application must not display indefinite loading indicators for local operations that have already failed.

---

# 4. SCREEN 01 — WELCOME SCREEN

**Screen ID:** SCR-001  
**Route:** `/welcome`  
**Entry:** First launch only, if onboarding has not been completed.

## Purpose

Explain what Scroll Battle does and establish trust before requesting sensitive Android permissions.

## Layout

1. App icon.
2. App name: Scroll Battle.
3. Heading: Take control of your scrolling.
4. Description explaining the 50-Reel daily limit.
5. Three short benefits:
   - Track your daily scrolling.
   - Understand your habits.
   - Keep your data on your phone.
6. Primary button: Get Started.
7. Small privacy statement: No account or cloud sync required.

## User Actions

### Get Started

**Behavior:**

- Navigate to the Privacy and Tracking Explanation screen.
- Do not request permissions automatically.
- Do not initialize a network connection.

### Android Back

- Exit the welcome screen according to normal Android behavior.
- Do not create an account or request permissions.

## States

### Success

The user reaches the explanation screen.

### Error

If local initialization fails, show a message that the app could not initialize and provide Retry.

### Empty

Not applicable.

---

# 5. SCREEN 02 — PRIVACY AND TRACKING EXPLANATION

**Screen ID:** SCR-002  
**Route:** `/onboarding/privacy`

## Purpose

Explain local data handling and the limitations of Reel detection before the user enables tracking.

## Layout

Heading: Your data stays on your phone.

Sections:

**Local Storage**

Your usage history and settings are stored on this device.

**Tracking**

Android usage access and/or accessibility access may be required to track Instagram activity.

**Accuracy**

Instagram does not expose an official public Reel counter through ordinary Android usage statistics. Reel counts may be estimates unless the detection mechanism has been validated.

**Blocking**

Blocking requires an available, supported mechanism and the necessary permissions. It cannot be guaranteed against all bypasses.

Buttons:

- Continue to Setup.
- Not Now.

## User Actions

### Continue to Setup

Navigate to Permission Setup.

### Not Now

Navigate to the dashboard in limited mode.

The app must display tracking as not configured, not as active.

### Android Back

Return to Welcome.

## States

### Success

Privacy explanation acknowledged; proceed to setup.

### Error

No system permission error is possible on this screen.

### Empty

Not applicable.

---

# 6. SCREEN 03 — PERMISSION SETUP

**Screen ID:** SCR-003  
**Route:** `/onboarding/permissions`

## Purpose

Guide the user through the minimum Android permissions required by the selected tracking implementation.

## Layout

Heading: Set up tracking.

Permission cards:

1. Usage access, if required.
2. Accessibility tracking, if required.
3. Notifications, if enabled and needed by the implementation.
4. Blocking capability, if separately required.

Each card contains:

- Permission name.
- Why it is needed.
- Current status.
- Setup button.
- A status indicator.

The screen must not request permissions that the final implementation does not use.

## User Actions

### Enable Usage Access

1. Open the relevant Android system settings screen.
2. Wait for the user to return.
3. Recheck actual permission status.
4. Update the permission card.

Do not assume success simply because the settings screen was opened.

### Enable Accessibility Tracking

1. Explain what the service observes.
2. Direct the user to the appropriate Android Accessibility settings.
3. Let the user enable the service.
4. Recheck service status when the app resumes.

Do not enable or simulate permission approval programmatically.

### Enable Notifications

If required and supported:

1. Request permission through the appropriate Android flow.
2. Recheck the result.
3. Explain if notification functionality is unavailable.

### Continue

Enabled only when the app's minimum required configuration has been assessed.

If the user has not enabled required permissions, Continue may still be available in limited mode.

### Skip Setup

Navigate to the dashboard with an explicit limited-tracking state.

## States

### Success

Required permissions are enabled and verified.

### Partial Success

Some permissions are available but one or more optional capabilities are not.

The app must state which features are affected.

### Denied

Show a clear explanation and the appropriate system-settings action.

### Permanently or Repeatedly Denied

Do not repeatedly open system settings automatically. Explain how to enable the permission manually.

### System Settings Unavailable

Display an error and allow the user to retry or continue in limited mode.

### Empty

Not applicable.

---

# 7. SCREEN 04 — TRACKING READINESS

**Screen ID:** SCR-004  
**Route:** `/onboarding/readiness`

## Purpose

Explain whether the app is ready to track Instagram activity and whether Reel counting has been validated.

## Layout

Heading: Check your setup.

Status sections:

- Usage access.
- Accessibility service.
- Detection validation.
- Blocking readiness.
- Local storage.

Possible statuses:

- Ready.
- Needs permission.
- Not tested.
- Limited.
- Unavailable.

Primary button: Open Dashboard.

Secondary button: Review Setup.

## User Actions

### Open Dashboard

Navigate to the dashboard.

### Review Setup

Return to Permission Setup.

### Run Detection Test

If a detection test is implemented, open a controlled testing screen that records the user's observed test results.

Do not automatically claim a pass without evidence.

## States

### Success

All required setup checks pass.

### Partial Success

The app can track Instagram usage time, but individual Reel detection has not yet been validated.

Display a warning that Reel counts are estimates or unavailable.

### Error

A required platform service is unavailable.

Show the reason and recovery action.

### Empty

Not applicable.

---

# 8. SCREEN 05 — MAIN DASHBOARD

**Screen ID:** SCR-005  
**Route:** `/you`  
**Entry:** Default screen on normal launch.

## Purpose

Show the user's current daily status immediately.

## Layout

### Header

- App name or logo.
- Current date.
- Settings icon button.

### Main Daily Progress Card

Example:

REELS WATCHED

32 / 50

18 remaining

64% of daily limit used.

The card must show a count only when a valid count exists.

### Time Card

Displays:

- Tracked Instagram time.
- Label describing the measurement.
- Optional session duration.

If the system can only measure Instagram foreground time, label it Instagram time rather than Reel-watching time.

### Tracking Status Card

Examples:

- Tracking active.
- Reel detection not validated.
- Permission required.
- Tracking unavailable.

### Quick Actions

- View Battle.
- View Progress.
- Fix Tracking.

### Bottom Navigation

You / Battle / Progress.

## User Actions

### Settings Icon

Navigate to Settings.

### View Battle

Navigate to the Battle screen.

### View Progress

Navigate to Progress.

### Fix Tracking

Navigate to the relevant permission or readiness screen.

### Tap Daily Progress Card

Navigate to Battle.

### Tap Time Card

Navigate to daily analytics, with time metrics selected.

### Pull to Refresh

If supported, reread local state and recheck permission status.

Do not run an aggressive tracking loop.

## States

### Normal Success

Display the current count, remaining allowance, duration, and tracking status.

### Zero Reels

Display:

0 / 50

50 remaining.

This is a valid state, not an error.

### Tracking Unavailable

Keep the dashboard visible, but replace the active tracking indicator with an error or warning state.

Do not present stale counts as current without indicating that tracking has stopped.

### Detection Not Validated

Display the available count with an estimate label and explain the limitation.

### No History

Display zero values for the current day if the tracking system confirms there are no recorded events.

If tracking is unavailable, do not confuse missing data with a confirmed zero.

### Loading

Display a brief loading state only while reading local data.

### Storage Error

Display the last safely available values if possible, mark them as potentially stale, and provide Retry.

---

# 9. SCREEN 06 — BATTLE / DAILY LIMIT

**Screen ID:** SCR-006  
**Route:** `/battle`

## Purpose

Show the daily limit and help the user understand their current boundary.

## Layout

- Screen title: Battle.
- Daily progress indicator.
- Current Reel count.
- Daily limit.
- Remaining allowance.
- Warning status.
- Blocking status.
- Settings shortcut for the limit.

Example:

TODAY'S LIMIT

32 / 50

18 Reels remaining.

Status: On track.

## User Actions

### Edit Limit

Navigate to the Limit Settings screen.

### Open Progress

Navigate to Progress.

### Review Tracking

Navigate to Tracking Readiness or permission setup.

### Return to You

Select the You tab.

## States

### Under Limit

Display remaining allowance and progress.

### At Warning Threshold

Display a non-blocking warning when the count reaches the configured threshold.

Warnings should not appear repeatedly for every subsequent event. Each threshold should trigger at most once per local day unless the user explicitly resets the daily state.

### Limit Reached

Navigate to the Limit Reached screen or activate the validated interruption mechanism.

### Tracking Unavailable

Disable claims that the limit is actively enforced and provide a recovery action.

### No Data

Display zero only if zero has been confirmed for the current day.

---

# 10. SCREEN 07 — LIMIT SETTINGS

**Screen ID:** SCR-007  
**Route:** `/settings/limit`

## Purpose

Allow the user to change the daily limit and warning thresholds.

## Layout

### Daily Limit

Numeric control.

Default: 50.

Validation:

- Integer only.
- Minimum: 1.
- Maximum: 500.

The maximum is a product decision to prevent invalid or accidental values, not an Android platform limit.

### Warning Thresholds

Default:

- 40 Reels.
- 45 Reels.

Thresholds must be unique positive integers below the configured daily limit.

If the limit is changed, invalid thresholds must be adjusted or flagged before saving.

### Blocking

Toggle: Enable blocking.

Show an explanation that blocking depends on permissions and supported Android behavior.

### Buttons

- Save Changes.
- Cancel.

## User Actions

### Save Changes

1. Validate all fields.
2. Persist settings.
3. Recalculate the remaining allowance.
4. Update the dashboard, Battle screen, and widget.
5. Return to Battle with a success confirmation.

### Cancel

Discard unsaved changes and return to Battle.

### Invalid Input

Keep the user on the screen and explain the specific field error.

### Save Failure

Keep the entered values visible, display an error, and allow Retry.

## Special Case: Current Count Exceeds a Lowered Limit

Example:

- Current count: 42.
- Existing limit: 50.
- New limit: 30.

After saving, the current day is already above the new limit.

The application must:

- Preserve the count of 42.
- Display 42 / 30.
- Show 0 remaining.
- Mark the limit as reached.
- Activate the selected blocking behavior if it is enabled and operational.

Do not reset the count when the limit changes.

---

# 11. SCREEN 08 — LIMIT REACHED

**Screen ID:** SCR-008  
**Route:** `/battle/limit-reached`

## Purpose

Make the user aware that the daily allowance has been consumed.

This screen may also be used by a separately implemented Android interruption mechanism, but navigation and enforcement must not be treated as the same operation.

## Layout

- Heading: Daily limit reached.
- Count: 50 / 50, or the actual count if it exceeds the limit.
- Message: You've reached today's Reel limit.
- Time spent today.
- Daily reset information.
- Primary button: Close Instagram / Return to Dashboard, according to the supported flow.
- Secondary button: View Progress.
- Optional button: Review Limit Settings.

Do not include a casual one-tap bypass that defeats the user's chosen limit.

## User Actions

### Close Instagram / Return to Dashboard

Use the supported implementation.

The app must not claim that it has forcibly closed Instagram unless that behavior is genuinely supported.

If the app cannot close Instagram, present a clear instruction or interruption experience instead.

### View Progress

Navigate to Progress.

### Review Limit Settings

Open Limit Settings.

If blocking is active, changing the limit must be a deliberate action rather than an accidental tap.

### Daily Reset

The limit state is recalculated for the next local calendar day.

## States

### Success

The user sees that the limit has been reached.

### Enforcement Unavailable

Display the limit-reached information but clearly state that automatic interruption could not be applied.

### Tracking Unavailable

Do not infer that the limit was reached if the counter cannot be trusted.

### Empty

Not applicable.

---

# 12. SCREEN 09 — PROGRESS OVERVIEW

**Screen ID:** SCR-009  
**Route:** `/progress`

## Purpose

Give the user a clear view of daily and weekly behavior.

## Layout

### Header

- Title: Progress.
- Date or selected period.

### Period Selector

- Daily.
- Weekly.

### Summary Cards

Daily view:

- Reels watched.
- Instagram time.
- Number of sessions.
- Daily limit status.

Weekly view:

- Total Reels.
- Total tracked time.
- Daily average.
- Highest-activity day.
- Lowest-activity day.

### Chart

Daily view:

- Hourly activity, when timestamp history is available.

Weekly view:

- Reel count by day.
- Daily limit comparison.

### History List

Recent daily summaries.

Each row displays:

- Date.
- Reel count.
- Time spent.
- Limit status.

## User Actions

### Daily / Weekly Selector

Switch the selected period and update the summary.

### Tap a Chart Bar

Open the relevant date's details.

### Tap a History Row

Navigate to Day Details.

### Change Date

If a date picker is implemented, select another date and refresh all values.

### Return to You

Select the You tab.

## States

### Success

Display the available data for the selected period.

### No History

Display:

No history yet.

Explanation: Your recorded activity will appear here once tracking is configured and data has been collected.

Offer Review Tracking when relevant.

### Partial History

Display available records and identify missing or unverified tracking periods.

Do not treat missing records as zero activity.

### Analytics Calculation Error

Show a retry action and preserve the rest of the screen where possible.

### Loading

Use a brief local-data loading state.

---

# 13. SCREEN 10 — DAILY DETAILS

**Screen ID:** SCR-010  
**Route:** `/progress/day/{date}`

## Purpose

Display a complete summary for one selected day.

## Layout

- Selected date.
- Reel count.
- Daily limit.
- Remaining allowance or over-limit amount.
- Total tracked Instagram time.
- Number of sessions.
- Average session duration.
- Hourly activity chart.
- Tracking quality status.

## User Actions

### Previous Day

Navigate to the previous date with available history or allow browsing through dates.

### Next Day

Navigate to the next date if it is within the permitted date range.

For a future date, do not display invented statistics.

### Back to Progress

Return to Progress while preserving the selected period.

## States

### Success

Display all available metrics for the date.

### Empty Day

Display zero only when the application has adequate evidence that tracking was active and no events were recorded.

If no history exists, display No recorded data instead.

### Partial Data

Label estimates and indicate unavailable intervals.

### Future Date

Disable navigation to dates beyond today.

---

# 14. SCREEN 11 — SETTINGS

**Screen ID:** SCR-011  
**Route:** `/settings`

## Purpose

Provide access to configuration, permissions, and data management.

## Layout

Sections:

### Tracking

- Tracking status.
- Permission setup.
- Detection readiness.

### Daily Limit

- Current limit.
- Warning thresholds.
- Blocking toggle.

### Data and Privacy

- Local storage explanation.
- Delete history.
- Reset settings.

### About

- App name.
- App version.
- Privacy statement.

## User Actions

### Tracking Status

Open Tracking Readiness.

### Permission Setup

Open Permission Setup.

### Daily Limit

Open Limit Settings.

### Delete History

Open the Delete History confirmation dialog.

### Reset Settings

Open the Reset Settings confirmation dialog.

### App Information

Display a simple information page or dialog.

## States

### Success

All current settings are displayed.

### Permission Missing

Show an explicit warning with an action to restore the permission.

### Storage Error

Display the error and do not silently claim that settings have been saved.

---

# 15. SCREEN 12 — TRACKING READINESS AND RECOVERY

**Screen ID:** SCR-012  
**Route:** `/settings/tracking`

This screen can reuse the Tracking Readiness UI from onboarding.

## Purpose

Provide a permanent location to diagnose tracking problems.

## Layout

- Usage access status.
- Accessibility service status.
- Detection validation status.
- Blocking status.
- Last known successful tracking time, if available.
- Troubleshooting guidance.

## User Actions

### Open Android Settings

Open the relevant system settings screen.

### Recheck Status

Read current permission state and update the screen.

### Retry Tracking

Restart or reinitialize the relevant internal tracking component only when supported and appropriate.

Do not assume the user has granted permission until system state confirms it.

### View Troubleshooting

Show instructions for restoring access, restarting the app, and verifying Instagram compatibility.

## States

### Ready

All required components are operational.

### Permission Missing

Show the missing permission and its setup action.

### Detection Not Validated

Show that exact Reel counts cannot currently be guaranteed.

### Service Unavailable

Explain the failure and offer Retry.

### System Settings Failure

Provide manual instructions and a Back action.

---

# 16. SCREEN 13 — DELETE HISTORY CONFIRMATION

**Screen ID:** SCR-013  
**Route:** Dialog opened from Settings.

## Purpose

Prevent accidental loss of personal history.

## Dialog Content

Title: Delete all history?

Message:

This will permanently remove locally stored Reel history, daily summaries, and associated analytics. Your settings will remain unchanged.

Buttons:

- Cancel.
- Delete History.

## User Actions

### Cancel

Close the dialog without changing data.

### Delete History

1. Delete historical event records.
2. Delete daily summaries.
3. Clear derived analytics caches.
4. Refresh dashboard and widgets.
5. Preserve settings.
6. Display a success confirmation.

The implementation should use a transaction or appropriate repository-level operation to avoid leaving inconsistent history.

## States

### Success

Display:

History deleted.

Dashboard values update to the current available state.

### Error

If deletion fails:

- Keep the dialog or show an error.
- Explain that deletion could not be completed.
- Provide Retry.
- Do not claim the data has been removed.

### Empty

If no history exists, disable the destructive action or explain that there is nothing to delete.

---

# 17. SCREEN 14 — RESET SETTINGS CONFIRMATION

**Screen ID:** SCR-014  
**Route:** Dialog opened from Settings.

## Purpose

Restore default preferences without accidentally deleting history.

## Dialog Content

Title: Reset settings?

Message:

This restores the default daily limit and other preferences. Your recorded usage history will remain available.

Buttons:

- Cancel.
- Reset Settings.

## User Actions

### Cancel

Close without changes.

### Reset Settings

Restore:

- Daily limit: 50.
- Warning thresholds: 40 and 45.
- Blocking preference: default disabled until the user explicitly enables it.
- Other user-configurable settings: documented defaults.

Preserve:

- Historical Reel events.
- Daily summaries.
- Existing records.

Recalculate the current daily status using the reset limit.

## States

### Success

Display Settings Reset.

### Error

Show the error and preserve the previous values wherever possible.

---

# 18. SCREEN 15 — HOME-SCREEN WIDGET

**Screen ID:** WDG-001  
**Surface:** Android home screen.

## Purpose

Display today's progress without requiring the user to open the application.

## Layout

Title: Scroll Battle.

Main value:

32 / 50 Reels.

Secondary value:

18 remaining.

Progress indicator.

Optional small status label:

- Tracking active.
- Data may be stale.
- Setup required.

## User Actions

### Tap Widget Body

Open the Dashboard.

### Tap Progress Area

If supported by the selected widget implementation, open Progress.

### Add Widget

The user adds the widget through the Android launcher's widget picker.

The app must not assume it can place itself on the home screen automatically.

## Update Behavior

Refresh when:

- Relevant tracking data changes and an update is appropriate.
- The application writes new daily summaries.
- The daily rollover is detected.
- The widget is re-created or re-enabled.

The implementation must follow Android widget scheduling restrictions.

## States

### Success

Display the latest locally available data.

### No History

Display 0 / 50 only if the app can confirm that the current day's recorded count is zero.

### Tracking Unavailable

Display a clear status instead of pretending that the count is current.

### Stale Data

Display the last available count and an appropriate stale-data indicator.

### No Permission

If the widget depends on a missing tracking permission, display Setup required.

---

# 19. SCREEN 16 — TRACKING INTERRUPTION OR OVERLAY

**Screen ID:** SYS-001  
**Type:** Optional Android-level interruption mechanism.

This component is conditional on the feasibility and policy review of the selected enforcement approach.

## Purpose

Interrupt continued Instagram usage after the daily limit has been reached.

## Trigger

- Accepted Reel count reaches or exceeds the daily limit.
- Blocking is enabled.
- Required permissions are available.
- The tracking and enforcement components are operational.

## Display

- Daily limit reached.
- Current count.
- Remaining allowance: 0.
- Short reminder of the user's chosen goal.
- Supported action to leave or return from Instagram.

## User Actions

### Return to Dashboard

Open Scroll Battle, if supported.

### Dismiss or Continue

Do not implement a casual bypass unless the product explicitly chooses to allow it.

If an override is later supported, it must be deliberate and recorded locally.

## Failure Behavior

If the overlay cannot be displayed, the application must not claim that blocking succeeded.

The app should record an enforcement failure locally where feasible and show the status on the dashboard.

The exact implementation must follow Android restrictions and any applicable distribution policies.

---

# 20. SCREEN 17 — TROUBLESHOOTING

**Screen ID:** SCR-017  
**Route:** `/settings/troubleshooting`

## Purpose

Help the user resolve common problems without reinstalling the application.

## Troubleshooting Topics

### Reel Count Is Not Increasing

Possible causes:

- Missing accessibility permission.
- Instagram UI changed.
- Detection method does not support the current feed.
- Tracking service is unavailable.
- Reel detection has not been validated.

Actions:

- Check permissions.
- Recheck tracking status.
- Run the detection test if available.
- Review known limitations.

### Time Is Increasing but Reel Count Is Not

Explain that Instagram usage time and individual Reel counting use different signals.

Do not artificially convert minutes into a Reel count.

### Widget Is Not Updating

Actions:

- Open the app.
- Recheck local data.
- Refresh the widget where supported.
- Check whether tracking is available.

### Daily Count Did Not Reset

Actions:

- Refresh local state.
- Recalculate the local calendar day.
- Inspect device time and time-zone changes.
- Recheck stored date summaries.

### Blocking Is Not Working

Actions:

- Verify blocking is enabled.
- Check required permissions.
- Recheck service status.
- Explain that Android and Instagram behavior may limit enforcement.

## States

### Success

The problem is resolved and the status screen reflects the new state.

### Error

If recovery fails, show that tracking or enforcement remains unavailable.

---

# 21. GLOBAL ERROR AND EMPTY-STATE MATRIX

The implementation must distinguish the following states.

| Condition | Display | Available action |
|---|---|---|
| No Reels recorded today | 0 / 50, when confirmed | None required |
| No historical records | No history yet | Set up tracking |
| Usage access missing | Tracking unavailable or limited | Open Settings |
| Accessibility service disabled | Reel detection unavailable | Enable service |
| Detection unvalidated | Estimated or unverified count | Run test / review status |
| Database read failure | Data unavailable or stale | Retry |
| Database write failure | Tracking may be incomplete | Retry / review status |
| Widget update delayed | Last available values | Open app |
| Limit reached | Limit reached | Return / view progress |
| Limit changed below current count | Over-limit state | Review limit settings |
| Notification permission missing | Notifications unavailable | Open Settings if needed |
| App resumed after midnight | New daily state | Continue |
| Device time zone changed | Recalculate according to documented policy | Review date if necessary |
| History deletion failed | Data may remain | Retry |
| Tracking service restarted | Tracking status rechecked | None if recovered |
| Unsupported Instagram UI | Reel count unavailable or estimated | Troubleshoot |

Error messages must explain the impact on the user's data and the next available action.

---

# 22. GLOBAL SUCCESS-STATE RULES

The app may display a success message only after the operation has actually completed.

Examples:

- Settings Saved: only after local persistence succeeds.
- History Deleted: only after deletion succeeds.
- Permission Enabled: only after system state confirms it.
- Tracking Active: only when the relevant service is operational.
- Blocking Active: only when the enforcement mechanism is operational.
- Widget Updated: only when the updated widget state is actually applied or accepted by the relevant system API.

Opening an Android system-settings page is not itself a successful permission grant.

---

# 23. ACCESSIBILITY AND USABILITY REQUIREMENTS

- Support Android font scaling.
- Maintain readable contrast.
- Use accessible labels for icons.
- Provide sufficiently large touch targets.
- Do not communicate status through color alone.
- Avoid excessive animation.
- Keep critical information near the top of each screen.
- Preserve state during configuration changes.
- Avoid unexpected navigation after saving settings.
- Use confirmation dialogs for destructive actions.
- Do not use shame-based messages when the user exceeds their limit.

---

# 24. IMPLEMENTATION ACCEPTANCE CHECKLIST

The implementation agent must verify every item before considering the UX complete.

## Onboarding

- [ ] First-launch welcome screen.
- [ ] Privacy explanation.
- [ ] Permission setup.
- [ ] Permission recheck after returning from Android settings.
- [ ] Tracking readiness screen.
- [ ] Limited mode when permissions are skipped.

## Dashboard

- [ ] Current Reel count.
- [ ] Daily limit.
- [ ] Remaining allowance.
- [ ] Tracking duration.
- [ ] Tracking status.
- [ ] Settings shortcut.
- [ ] Navigation to Battle and Progress.

## Battle

- [ ] Daily progress.
- [ ] Warning thresholds.
- [ ] Limit-reached state.
- [ ] Settings navigation.
- [ ] Tracking-unavailable state.

## Analytics

- [ ] Daily analytics.
- [ ] Weekly analytics.
- [ ] Hourly activity when supported.
- [ ] Historical day details.
- [ ] No-history state.
- [ ] Partial-data state.

## Settings

- [ ] Edit daily limit.
- [ ] Validate thresholds.
- [ ] Blocking toggle.
- [ ] Permission status.
- [ ] Delete history.
- [ ] Reset settings.
- [ ] Troubleshooting.

## Widget

- [ ] Add widget through Android launcher.
- [ ] Show daily count.
- [ ] Show remaining allowance.
- [ ] Display stale or unavailable status appropriately.
- [ ] Open the correct app destination.

## Reliability

- [ ] Local-midnight reset.
- [ ] App restart recovery.
- [ ] Permission revocation.
- [ ] Storage errors.
- [ ] No duplicate event counting.
- [ ] No false success messages.
- [ ] Offline operation.
- [ ] No unnecessary network access.

---

# 25. FINAL UX DECISION

The core user journey must remain simple:

1. Open Scroll Battle.
2. See today's Reel count and remaining allowance.
3. Receive a warning before reaching 50.
4. Reach the configured limit and receive the supported interruption.
5. Review daily or weekly analytics.
6. Check progress from the home-screen widget.

Every secondary feature must support this journey.

Do not add social functionality, cloud accounts, complicated gamification, or unnecessary screens in Version 1.

When technical limitations prevent exact counting or reliable blocking, show a truthful limited state and explain the next action. The user must always be able to distinguish confirmed activity, estimated activity, and unavailable tracking.