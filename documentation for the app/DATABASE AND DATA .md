# SCROLL BATTLE — DATABASE AND DATA OWNERSHIP SPECIFICATION

**Document Version:** 1.0  
**Status:** Proposed for Implementation  
**Platform:** Android  
**Database:** SQLite through Android Room  
**Settings Storage:** Preferences DataStore  
**Backend Server:** None  
**Authentication:** None  
**Network APIs:** None  
**Data Ownership:** Single local device owner  
**Default Daily Reel Limit:** 50

---

## 1. ARCHITECTURE DECISION

Scroll Battle is a personal, offline-first application. Its data model must not require a remote database or server.

Use:

- Room for structured event history and daily summaries.
- DataStore for simple application preferences.
- Android system permission APIs for permission state.
- Android Keystore only if a future feature requires encryption keys.
- No Firebase, Supabase, PostgreSQL server, authentication provider, or cloud API.

### Data flow

Instagram activity signals → Detection engine → Event validation and deduplication → Room database → Analytics repository → Compose UI and home-screen widget.

Settings flow:

User changes settings → DataStore → Limit policy engine → Dashboard and widget updates.

The application must not use the UI or widget as an independent source of truth.

---

## 2. DATABASE CONVENTIONS

### Naming

- Tables: lowercase snake_case.
- Columns: lowercase snake_case.
- Primary keys: `id` for generated integer identifiers, or a natural key where appropriate.
- Timestamps: Unix epoch milliseconds stored as SQLite `INTEGER`.
- Durations: milliseconds stored as SQLite `INTEGER`.
- Booleans: SQLite `INTEGER`, restricted to `0` or `1`.
- Counts: SQLite `INTEGER`.
- Dates: ISO 8601 local date strings in `YYYY-MM-DD` format.
- Enumerated values: constrained `TEXT` values.

### Important distinction

SQLite does not have a native Boolean, date, or timestamp storage class. Room converters and validation rules must preserve the intended Kotlin types.

Use UTC epoch timestamps for recorded instants. Use a documented local-date policy for assigning activity to daily summaries.

---

## 3. TABLE 1 — `tracking_sessions`

Purpose: Record periods during which Scroll Battle is monitoring a relevant Instagram activity session.

| Column | Type | Constraints |
|---|---|---|
| `id` | INTEGER | Primary key, auto-generated |
| `started_at_ms` | INTEGER | NOT NULL |
| `ended_at_ms` | INTEGER | Nullable |
| `last_activity_at_ms` | INTEGER | Nullable |
| `duration_ms` | INTEGER | NOT NULL, default 0 |
| `session_status` | TEXT | NOT NULL |
| `tracking_source` | TEXT | NOT NULL |
| `data_quality` | TEXT | NOT NULL |
| `created_at_ms` | INTEGER | NOT NULL |

### Allowed values

`session_status`:

- `ACTIVE`
- `PAUSED`
- `COMPLETED`
- `INTERRUPTED`

`tracking_source`:

- `ACCESSIBILITY`
- `USAGE_STATS`
- `COMBINED`
- `MANUAL`

`data_quality`:

- `VERIFIED`
- `ESTIMATED`
- `UNVERIFIED`

### Rules

- Only one session should be considered active at a time.
- An active session may have a null `ended_at_ms`.
- A completed session must have a valid end timestamp.
- `duration_ms` must never be negative.
- A paused session must not accumulate active duration unless the documented measurement policy explicitly says otherwise.
- Session duration must be derived from valid session timing data, not arbitrary background elapsed time.

### Indexes

- Index on `started_at_ms`.
- Index on `session_status`.
- Index on `ended_at_ms`.

---

## 4. TABLE 2 — `reel_events`

Purpose: Store individual Reel-transition events accepted by the detection engine.

| Column | Type | Constraints |
|---|---|---|
| `id` | INTEGER | Primary key, auto-generated |
| `session_id` | INTEGER | Nullable foreign key |
| `event_timestamp_ms` | INTEGER | NOT NULL |
| `local_date` | TEXT | NOT NULL |
| `event_type` | TEXT | NOT NULL |
| `detection_source` | TEXT | NOT NULL |
| `confidence` | TEXT | NOT NULL |
| `deduplication_key` | TEXT | Nullable |
| `counted` | INTEGER | NOT NULL, default 0 |
| `rejection_reason` | TEXT | Nullable |
| `created_at_ms` | INTEGER | NOT NULL |

### Foreign key

`session_id` references `tracking_sessions.id`.

Use `ON DELETE SET NULL` so that event records can survive session-record deletion if that behavior is ever needed. For ordinary history deletion, delete related events explicitly through the repository.

### Allowed values

`event_type`:

- `REEL_TRANSITION`
- `SESSION_START`
- `SESSION_END`
- `DETECTION_GAP`

`detection_source`:

- `ACCESSIBILITY`
- `USAGE_STATS`
- `MANUAL`

`confidence`:

- `VERIFIED`
- `ESTIMATED`
- `UNVERIFIED`

### Rules

1. A raw accessibility callback must not automatically count as one Reel.
2. The detection engine must validate and deduplicate candidate transitions.
3. Only accepted `REEL_TRANSITION` events may increment the Reel counter.
4. Session boundaries and detection-gap records must not increment the count.
5. A rejected candidate may be recorded for local diagnostics, but rejected candidates must not contribute to analytics.
6. Do not store screenshots, raw screen contents, unrelated UI text, or Instagram account information.
7. `local_date` must follow the documented date-assignment policy.

### Indexes

- Composite index on `(local_date, counted)`.
- Index on `event_timestamp_ms`.
- Index on `session_id`.
- Unique index on `deduplication_key` where the key is non-null and stable enough to identify duplicate candidates.

The deduplication key must not be generated randomly for each callback. If a stable key cannot be derived, use a carefully tested time-window deduplication strategy in the tracking engine instead.

---

## 5. TABLE 3 — `daily_summaries`

Purpose: Store one summary per local calendar date.

| Column | Type | Constraints |
|---|---|---|
| `local_date` | TEXT | Primary key |
| `reel_count` | INTEGER | NOT NULL, default 0 |
| `tracked_duration_ms` | INTEGER | NOT NULL, default 0 |
| `session_count` | INTEGER | NOT NULL, default 0 |
| `verified_event_count` | INTEGER | NOT NULL, default 0 |
| `estimated_event_count` | INTEGER | NOT NULL, default 0 |
| `tracking_status` | TEXT | NOT NULL |
| `data_quality` | TEXT | NOT NULL |
| `first_activity_at_ms` | INTEGER | Nullable |
| `last_activity_at_ms` | INTEGER | Nullable |
| `last_updated_at_ms` | INTEGER | NOT NULL |

### Allowed values

`tracking_status`:

- `ACTIVE`
- `LIMITED`
- `UNAVAILABLE`
- `INACTIVE`

`data_quality`:

- `VERIFIED`
- `ESTIMATED`
- `PARTIAL`
- `UNAVAILABLE`

### Rules

- `local_date` is unique and uses `YYYY-MM-DD`.
- Counts and durations must not be negative.
- `reel_count` must reflect accepted Reel events under the active counting policy.
- The application must not silently equate missing data with zero activity.
- `verified_event_count` and `estimated_event_count` must be consistent with the accepted events.
- If a summary is recalculated, its source records must remain available unless the user deletes history.
- Daily summaries must not be deleted during the normal midnight rollover.

### Indexes

The primary key already indexes `local_date`.

Optional index:

- `last_updated_at_ms`, if needed for maintenance or troubleshooting.

### Important implementation note

A summary is derived data. Reel events and session records are the underlying evidence.

For the MVP, summary updates and event inserts should be coordinated through Room transactions so that the dashboard cannot observe an inconsistent count.

---

## 6. TABLE 4 — `tracking_diagnostics`

Purpose: Record limited local diagnostic information about tracking availability and failures.

This table is optional. Include it only if the implementation needs persistent troubleshooting history.

| Column | Type | Constraints |
|---|---|---|
| `id` | INTEGER | Primary key, auto-generated |
| `timestamp_ms` | INTEGER | NOT NULL |
| `component` | TEXT | NOT NULL |
| `diagnostic_code` | TEXT | NOT NULL |
| `severity` | TEXT | NOT NULL |
| `details_code` | TEXT | Nullable |
| `resolved_at_ms` | INTEGER | Nullable |

### Allowed components

- `ACCESSIBILITY`
- `USAGE_STATS`
- `DETECTION`
- `DATABASE`
- `WIDGET`
- `BLOCKING`
- `PERMISSIONS`

### Severity

- `INFO`
- `WARNING`
- `ERROR`

### Rules

- Store diagnostic codes rather than raw screen content.
- Do not store personal information or unrelated accessibility text.
- Retain only a small, bounded diagnostic history.
- Delete diagnostics as part of a complete history reset when appropriate.
- Do not upload diagnostic information.

### Indexes

- Index on `timestamp_ms`.
- Composite index on `(component, severity)`.

---

## 7. SETTINGS — PREFERENCES DATASTORE

Simple settings should not be stored in Room.

Use Android Preferences DataStore for the following values.

| Key | Type | Default |
|---|---|---|
| `daily_limit` | Int | 50 |
| `warning_thresholds` | List of Int | 40, 45 |
| `blocking_enabled` | Boolean | false |
| `onboarding_completed` | Boolean | false |
| `detection_test_completed` | Boolean | false |
| `history_retention_days` | Int | 0 |
| `last_known_time_zone` | String | Current device time zone |
| `schema_version` | Int | Current configuration version |

### Validation

- Daily limit: integer between 1 and 500.
- Warning thresholds: unique positive integers below the daily limit.
- Blocking defaults to disabled until explicitly enabled by the user.
- `history_retention_days = 0` means no automatic time-based deletion.
- `onboarding_completed` is not a substitute for checking current permissions.
- `detection_test_completed` must not be interpreted as proof that future detection remains accurate.
- `schema_version` supports settings migrations and should not be used as a substitute for Room's schema version.

### Settings persistence

Changes must be validated before saving.

If persistence fails, retain the previous valid settings and report the failure to the UI.

---

## 8. OPTIONAL TABLE 5 — `local_date_metadata`

Add this table only if reliable reporting across time-zone changes requires preserving date-assignment metadata.

| Column | Type | Constraints |
|---|---|---|
| `local_date` | TEXT | Primary key |
| `zone_id` | TEXT | NOT NULL |
| `utc_offset_seconds` | INTEGER | NOT NULL |
| `created_at_ms` | INTEGER | NOT NULL |

Purpose:

Record the time-zone context used when a local-day summary was created.

This table is not required for the simplest MVP. It should be added only if the implementation adopts a policy that requires this additional historical context.

---

## 9. RELATIONSHIPS

The relationships are intentionally simple.

`tracking_sessions` 1 → many `reel_events`

A session can produce multiple accepted Reel events. An event may have no associated session when its origin cannot be established reliably.

`reel_events` → `daily_summaries`

Events contribute to daily summaries through `local_date`. This is a logical aggregation relationship rather than a direct foreign key because one daily summary represents many events.

`tracking_sessions` → `daily_summaries`

Session durations and counts contribute to daily summaries. Sessions crossing midnight must be split into date-specific duration contributions for accurate reporting.

`tracking_diagnostics`

Diagnostic records are independent and contain no user identity or remote ownership key.

`DataStore`

Settings are independent preferences associated with the local application installation.

---

## 10. INDEX STRATEGY

Create indexes only for queries that the app actually performs.

Recommended indexes:

1. `reel_events(local_date, counted)` — daily Reel counts.
2. `reel_events(event_timestamp_ms)` — hourly activity.
3. `reel_events(session_id)` — session-related event lookup.
4. `tracking_sessions(started_at_ms)` — historical session listing.
5. `tracking_sessions(session_status)` — recovery of unfinished sessions.
6. `tracking_diagnostics(timestamp_ms)` — recent troubleshooting records.

The `daily_summaries.local_date` primary key already supports date-based lookup.

Do not add indexes to every column. Unnecessary indexes increase storage and write overhead.

---

## 11. AUTHENTICATION AND SESSION HANDLING

### User authentication

Not applicable.

There is no:

- User table.
- Password table.
- Authentication token.
- Refresh token.
- Server session.
- Login endpoint.
- Remote identity provider.

### Tracking sessions

`tracking_sessions` represents a period of tracked activity, not an authenticated user session.

The app must not use tracking-session records as proof of user identity.

### Android process lifecycle

The app must recover gracefully when Android kills its process.

On startup or service recovery:

1. Read local settings.
2. Recheck actual Android permission state.
3. Inspect unfinished tracking sessions.
4. Close or reconcile stale session state using a documented recovery policy.
5. Rebuild any necessary daily summaries from persisted events.
6. Publish the resulting state to the UI and widget.

Do not invent missing Reel events during recovery.

---

## 12. PERMISSIONS AND DATA OWNERSHIP

### Application ownership

All records belong to the person using the Android installation.

There is no multi-user sharing model.

The app must store data in its private application sandbox.

### Android permissions

Request only the permissions needed by the implemented features.

Potential requirements:

- Usage access through Android system settings.
- Accessibility access through Android Accessibility settings.
- Notification permission if notifications are enabled and required.
- Any additional system permission required by a validated blocking mechanism.

Permission state must be checked against Android itself. Stored settings may record the user's preferences, but must never replace actual system permission checks.

### Data access

- The dashboard reads local summaries.
- Analytics reads local summaries and event history.
- The widget reads the same local data source.
- The tracking engine writes accepted events.
- Settings screens read and update DataStore.
- No third-party service receives personal usage data.

### Data deletion

When the user selects Delete History:

1. Delete Reel events.
2. Delete tracking sessions.
3. Delete daily summaries.
4. Delete tracking diagnostics if included in the requested deletion scope.
5. Clear derived caches.
6. Refresh widget state.
7. Preserve application preferences unless the user separately chooses Reset Settings.

The deletion operation should be coordinated through a repository-level transaction where possible.

---

## 13. TRANSACTIONAL REQUIREMENTS

A Reel event must not be counted twice if the same candidate callback is processed repeatedly.

The accepted event and its summary contribution must be committed consistently.

Recommended process:

1. Receive candidate event.
2. Validate event type and timestamp.
3. Check deduplication constraints.
4. Reject invalid or duplicate events.
5. Insert the accepted event.
6. Update the relevant daily summary.
7. Commit the transaction.
8. Notify observers of the new state.

If the transaction fails, the application must not display a successful count increment that was never persisted.

---

## 14. DAILY RESET AND HISTORICAL DATA

There is no database-wide reset operation at midnight.

Instead:

- The current date changes.
- A new daily summary is created when appropriate.
- The active dashboard reads the new date's summary.
- Previous summaries remain intact.
- Historical analytics continue to query past dates.

When a tracking session crosses midnight, its duration must be attributed to the relevant dates according to the documented local-day policy.

Time-zone changes require explicit handling. Do not silently rewrite history without a defined migration or reassignment policy.

---

## 15. SECURITY AND PRIVACY REQUIREMENTS

- No remote backend.
- No external analytics SDK.
- No advertising SDK.
- No network transmission of usage records.
- No storage of screenshots or raw screen text.
- No account credentials.
- No unnecessary exported Android components.
- No public database provider.
- No sensitive information in production logs.
- No cloud backup of usage history unless explicitly introduced as a future user-controlled feature.
- No hard-coded API keys or service credentials.

The absence of a backend reduces the remote attack surface but does not remove the need to secure local data and Android components.

---

## 16. MIGRATION STRATEGY

Use Room schema versioning and explicit database migrations.

Rules:

- Never silently discard user history during a schema change.
- Add columns with compatible defaults where appropriate.
- Test migrations against populated databases.
- Preserve primary keys and event ordering.
- Keep migration tests for every released schema version.
- Version DataStore settings independently from Room's schema.
- Back up a test database before validating destructive migrations.

For a personal MVP, use a single database initially. Avoid unnecessary multi-database architecture.

---

## 17. IMPLEMENTATION ACCEPTANCE CRITERIA

The schema is ready for implementation when:

1. All required entities and constraints are defined.
2. Daily summaries can be queried efficiently.
3. Duplicate Reel events are prevented or rejected.
4. Session records recover safely after process termination.
5. Daily rollover does not delete historical data.
6. Session durations crossing midnight are handled correctly.
7. Settings persist independently from historical data.
8. Permission state is checked against Android.
9. History deletion clears all applicable derived data.
10. Widget and dashboard read the same authoritative data.
11. No personal usage data is transmitted externally.
12. Room migrations are tested.
13. The schema remains small and maintainable.

---

## 18. FINAL DECISION

Use Room/SQLite for event history, sessions, daily summaries, and optional diagnostics. Use Preferences DataStore for settings.

Do not create users, authentication sessions, remote API endpoints, or cloud database tables.

The application should maintain one authoritative local record of each accepted Reel event, calculate daily summaries consistently, and make the same data available to the dashboard, analytics, and home-screen widget.

The database must preserve historical data across daily resets and accurately represent missing or uncertain tracking information.

Privacy, consistency, and honest measurement take precedence over unnecessary backend infrastructure.