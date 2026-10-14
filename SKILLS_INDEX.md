# ScrollBattle Skills Index & Master Catalog

This repository is configured with **34 engineering skills** integrated into the Antigravity agent system under [`.agents/skills/`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/).

## How Skills Work in Antigravity

Antigravity uses **Progressive Disclosure**: skill metadata (`name` and `description`) is automatically discovered at workspace load time. When a task matches a skill's purpose or trigger conditions, the agent activates the skill and reads the full runbook instructions from its `SKILL.md` file.

---

## Category Summary

- **Android & Jetpack Compose Development** (17 skills)
- **Engineering Methodology & Quality** (6 skills)
- **Cloud, Backend & Distributed Systems** (9 skills)
- **Full-Stack & Modern Vibe Security** (2 skills)

---

## Android & Jetpack Compose Development

| Skill | Location | Trigger & Purpose |
| :--- | :--- | :--- |
| [`adaptive`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/adaptive/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/adaptive/SKILL.md) | Instructions to make or update an app's UI so that it adapts to different Android devices including phones, tablets, foldables, laptops, desktop, TV, Auto and XR. It includes ho... |
| [`android-accessibility`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-accessibility/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-accessibility/SKILL.md) | Use when building or reviewing Android UI for accessibility. Covers TalkBack, content descriptions, touch targets, Compose semantics, focus order, and Accessibility Scanner test... |
| [`android-architecture`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-architecture/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-architecture/SKILL.md) | Use when designing module structure, choosing architecture patterns (MVVM/MVI), setting up dependency injection, or defining layer boundaries in an Android project. |
| [`android-background-work`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-background-work/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-background-work/SKILL.md) | Use when work must run outside the visible UI — sync, uploads, periodic jobs, long-running tasks. Covers WorkManager constraints and expedited work, foreground service types, ex... |
| [`android-cli`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-cli/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-cli/SKILL.md) | Provides instructions for installing and using the `android` CLI. The `android` command-line tool is a critical tool for Android development and helps you create new Android pro... |
| [`android-data-persistence`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-data-persistence/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-data-persistence/SKILL.md) | Use when implementing local data storage with Room, DataStore, or offline-first patterns. Covers entities, DAOs, migrations, Paging3, and the repository pattern for data managem... |
| [`android-device-testing`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-device-testing/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-device-testing/SKILL.md) | Use when writing or debugging instrumented tests (Espresso, UI Automator, Compose test rules), using ADB, managing emulators, or inspecting UI with Layout Inspector. |
| [`android-e2e-verification`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-e2e-verification/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-e2e-verification/SKILL.md) | Use when a feature slice needs proof it works end-to-end on a device, or when closing the implement → run → assert loop for any UI-facing change. Maestro YAML flows turn accepta... |
| [`android-intent-security`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-intent-security/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-intent-security/SKILL.md) | Best practices for Android Intent security. Use this skill when auditing component configurations in AndroidManifest.xml activities, services, receivers) or source code handling... |
| [`android-ui-engineering`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-ui-engineering/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-ui-engineering/SKILL.md) | Use when building UI with Jetpack Compose. Covers component design, state hoisting, recomposition optimization, Material 3 theming, Navigation 3, adaptive layouts for large scre... |
| [`edge-to-edge`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/edge-to-edge/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/edge-to-edge/SKILL.md) | Use this skill to migrate your Jetpack Compose app to add adaptive edge-to-edge support and troubleshoot common issues. Use this skill to fix UI components (like buttons or list... |
| [`navigation-3`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/navigation-3/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/navigation-3/SKILL.md) | Learn how to install and migrate to Jetpack Navigation 3, and how to implement features and patterns such as deep links, multiple backstacks, scenes (dialogs, bottom sheets, lis... |
| [`performance-optimization`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/performance-optimization/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/performance-optimization/SKILL.md) | Use when measuring or improving Android app performance. Covers Android Vitals (startup, jank, ANR), Macrobenchmark, APK size, recomposition tracing, and profiling with Android ... |
| [`play-policy-insights`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/play-policy-insights/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/play-policy-insights/SKILL.md) | Automated auditor designed to verify Android applications against Google Play Policy domains. It cross-references static code analysis with Play Store declarations to generate d... |
| [`r8-analyzer`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/r8-analyzer/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/r8-analyzer/SKILL.md) | Analyzes Android build files and R8 keep rules to identify redundancies, broad package-wide rules, and rules that subsume library consumer keep rules. Use when developers want t... |
| [`security-and-hardening`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/security-and-hardening/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/security-and-hardening/SKILL.md) | Use when handling sensitive data, authentication, network communication, or before shipping to the Play Store. Three-tier framework (Always Do, Ask First, Never Do) with Android... |
| [`testing-setup`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/testing-setup/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/testing-setup/SKILL.md) | Analyze and create a testing strategy for Android apps - install testing libraries, set up test infrastructure, create harnesses for unit tests, UI tests, screenshot tests, and ... |


## Engineering Methodology & Quality

| Skill | Location | Trigger & Purpose |
| :--- | :--- | :--- |
| [`debugging-and-error-recovery`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/debugging-and-error-recovery/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/debugging-and-error-recovery/SKILL.md) | Use when encountering unexpected behavior, crashes, or test failures in Android. Six-step triage from reproduction to regression guard, using Logcat, Android Studio debugger, an... |
| [`incremental-implementation`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/incremental-implementation/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/incremental-implementation/SKILL.md) | Use when building features incrementally. Each increment leaves the system in a working, testable state. Covers vertical slicing, feature flags, and rollback-friendly developmen... |
| [`planning-and-task-breakdown`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/planning-and-task-breakdown/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/planning-and-task-breakdown/SKILL.md) | Use when starting implementation of a feature or change that involves multiple files or steps. Produces a structured task list with vertical slices, acceptance criteria, and ver... |
| [`source-driven-development`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/source-driven-development/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/source-driven-development/SKILL.md) | Use when implementing framework-specific code (Jetpack Compose, Room, Hilt, Navigation, etc.). Every API usage must be backed by official documentation, not memory or Stack Over... |
| [`spec-driven-development`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/spec-driven-development/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/spec-driven-development/SKILL.md) | Use when starting new Android projects, features, or changes with unclear requirements. Guides writing a structured spec (SPEC.md) that becomes the shared source of truth before... |
| [`test-driven-development`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/test-driven-development/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/test-driven-development/SKILL.md) | Use when implementing new features or fixing bugs. Write tests before implementation following Red-Green-Refactor. Covers JUnit5, MockK, Compose test rules, and the Prove-It pat... |


## Cloud, Backend & Distributed Systems

| Skill | Location | Trigger & Purpose |
| :--- | :--- | :--- |
| [`backup-and-restore`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/backup-and-restore/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/backup-and-restore/SKILL.md) | Defines the discipline of building backups you can actually restore under pressure — RPO-driven frequency, restores rehearsed on a schedule, offsite immutable copies, encryption... |
| [`caching-strategies`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/caching-strategies/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/caching-strategies/SKILL.md) | Covers caching correctly — deciding what is worth caching, choosing between cache-aside, write-through, and write-behind, setting TTLs from real staleness tolerance, invalidatin... |
| [`cdn`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/cdn/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/cdn/SKILL.md) | Covers caching and serving content at the edge — cache keys and TTL design, invalidation strategies, origin shielding, deciding what is safely cacheable, and moving static and d... |
| [`cloud-architecture`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/cloud-architecture/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/cloud-architecture/SKILL.md) | Designs systems for the cloud's actual shape — regions and availability zones, managed vs self-run tradeoffs, statelessness, failure-domain isolation, and the cost and lock-in c... |
| [`observability`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/observability/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/observability/SKILL.md) | Frames the mental model for making a running system explain itself — metrics, logs, and traces as complementary signals, RED and USE checklists, SLOs and error budgets, cardinal... |
| [`scalability-design`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/scalability-design/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/scalability-design/SKILL.md) | Designs systems to handle the next order of magnitude of load by removing state, shared contention, and single bottlenecks, and by choosing deliberately between horizontal and v... |
| [`secrets-management`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/secrets-management/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/secrets-management/SKILL.md) | Keeps credentials, API keys, and certificates out of code, images, and logs, and moves them into a real secret store with tight, scoped runtime injection. Use this whenever the ... |
| [`serverless`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/serverless/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/serverless/SKILL.md) | Covers functions and managed compute where the platform enforces statelessness and bills per request — cold starts, event-driven design, concurrency limits, and when serverless ... |
| [`well-architected-review`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/well-architected-review/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/well-architected-review/SKILL.md) | Runs a structured audit against the standard pillars — reliability, security, cost, performance, operational excellence, sustainability — producing prioritized, actionable findi... |


## Full-Stack & Modern Vibe Security

| Skill | Location | Trigger & Purpose |
| :--- | :--- | :--- |
| [`supabase`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/supabase/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/supabase/SKILL.md) | Use when doing ANY task involving Supabase. Triggers: Supabase products (Database, Auth, Edge Functions, Realtime, Storage, Vectors, Cron, Queues); client libraries and SSR inte... |
| [`vibe-security`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/vibe-security/SKILL.md) | [SKILL.md](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/vibe-security/SKILL.md) | Audits codebases for common security vulnerabilities that AI coding assistants introduce in "vibe-coded" applications. Checks for exposed API keys, broken access control (Supaba... |


---

## Original File Mapping & Deduplication Audit

The original [`Skill md/`](file:///D:/vibe%20coding%20project/ScrollBattle/Skill%20md) directory contained **42 files**, which mapped to **34 unique skills** and **8 duplicate files**:


| Skill Name | Target Path | Primary Source | Duplicates / Aliases in `Skill md/` |
| :--- | :--- | :--- | :--- |
| [`adaptive`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/adaptive/SKILL.md) | `.agents/skills/adaptive/SKILL.md` | `SKILL (5).md` | `SKILL 30 (14).md` |
| [`android-accessibility`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-accessibility/SKILL.md) | `.agents/skills/android-accessibility/SKILL.md` | `SKILL (13).md` | None (Single file) |
| [`android-architecture`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-architecture/SKILL.md) | `.agents/skills/android-architecture/SKILL.md` | `SKILL (2).md` | None (Single file) |
| [`android-background-work`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-background-work/SKILL.md) | `.agents/skills/android-background-work/SKILL.md` | `SKILL (10).md` | None (Single file) |
| [`android-cli`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-cli/SKILL.md) | `.agents/skills/android-cli/SKILL.md` | `SKILL 30 (21).md` | None (Single file) |
| [`android-data-persistence`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-data-persistence/SKILL.md) | `.agents/skills/android-data-persistence/SKILL.md` | `SKILL (9).md` | None (Single file) |
| [`android-device-testing`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-device-testing/SKILL.md) | `.agents/skills/android-device-testing/SKILL.md` | `SKILL (18).md` | None (Single file) |
| [`android-e2e-verification`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-e2e-verification/SKILL.md) | `.agents/skills/android-e2e-verification/SKILL.md` | `SKILL (19).md` | None (Single file) |
| [`android-intent-security`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-intent-security/SKILL.md) | `.agents/skills/android-intent-security/SKILL.md` | `SKILL (16).md` | `SKILL 30 (18).md` |
| [`android-ui-engineering`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/android-ui-engineering/SKILL.md) | `.agents/skills/android-ui-engineering/SKILL.md` | `SKILL (4).md` | None (Single file) |
| [`backup-and-restore`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/backup-and-restore/SKILL.md) | `.agents/skills/backup-and-restore/SKILL.md` | `SKILL 30 (1).md` | None (Single file) |
| [`caching-strategies`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/caching-strategies/SKILL.md) | `.agents/skills/caching-strategies/SKILL.md` | `SKILL 30 (11).md` | None (Single file) |
| [`cdn`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/cdn/SKILL.md) | `.agents/skills/cdn/SKILL.md` | `SKILL 30 (5).md` | `SKILL 30 (6).md` |
| [`cloud-architecture`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/cloud-architecture/SKILL.md) | `.agents/skills/cloud-architecture/SKILL.md` | `SKILL 30 (12).md` | `SKILL 30 (7).md`, `SKILL 30 (8).md` |
| [`debugging-and-error-recovery`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/debugging-and-error-recovery/SKILL.md) | `.agents/skills/debugging-and-error-recovery/SKILL.md` | `SKILL (15).md` | None (Single file) |
| [`edge-to-edge`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/edge-to-edge/SKILL.md) | `.agents/skills/edge-to-edge/SKILL.md` | `SKILL (6).md` | None (Single file) |
| [`incremental-implementation`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/incremental-implementation/SKILL.md) | `.agents/skills/incremental-implementation/SKILL.md` | `SKILL (3).md` | None (Single file) |
| [`navigation-3`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/navigation-3/SKILL.md) | `.agents/skills/navigation-3/SKILL.md` | `SKILL (7).md` | `SKILL (8).md`, `SKILL 30 (13).md` |
| [`observability`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/observability/SKILL.md) | `.agents/skills/observability/SKILL.md` | `SKILL 30 (3).md` | None (Single file) |
| [`performance-optimization`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/performance-optimization/SKILL.md) | `.agents/skills/performance-optimization/SKILL.md` | `SKILL (11).md` | None (Single file) |
| [`planning-and-task-breakdown`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/planning-and-task-breakdown/SKILL.md) | `.agents/skills/planning-and-task-breakdown/SKILL.md` | `SKILL (1).md` | None (Single file) |
| [`play-policy-insights`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/play-policy-insights/SKILL.md) | `.agents/skills/play-policy-insights/SKILL.md` | `SKILL 30 (17).md` | None (Single file) |
| [`r8-analyzer`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/r8-analyzer/SKILL.md) | `.agents/skills/r8-analyzer/SKILL.md` | `SKILL (20).md` | `SKILL 30 (16).md` |
| [`scalability-design`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/scalability-design/SKILL.md) | `.agents/skills/scalability-design/SKILL.md` | `SKILL 30 (4).md` | None (Single file) |
| [`secrets-management`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/secrets-management/SKILL.md) | `.agents/skills/secrets-management/SKILL.md` | `SKILL 30 (2).md` | None (Single file) |
| [`security-and-hardening`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/security-and-hardening/SKILL.md) | `.agents/skills/security-and-hardening/SKILL.md` | `SKILL (14).md` | None (Single file) |
| [`serverless`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/serverless/SKILL.md) | `.agents/skills/serverless/SKILL.md` | `SKILL 30 (9).md` | None (Single file) |
| [`source-driven-development`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/source-driven-development/SKILL.md) | `.agents/skills/source-driven-development/SKILL.md` | `SKILL (12).md` | None (Single file) |
| [`spec-driven-development`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/spec-driven-development/SKILL.md) | `.agents/skills/spec-driven-development/SKILL.md` | `SKILL.md` | None (Single file) |
| [`supabase`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/supabase/SKILL.md) | `.agents/skills/supabase/SKILL.md` | `SKILL 30 (19).md` | None (Single file) |
| [`test-driven-development`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/test-driven-development/SKILL.md) | `.agents/skills/test-driven-development/SKILL.md` | `SKILL (17).md` | None (Single file) |
| [`testing-setup`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/testing-setup/SKILL.md) | `.agents/skills/testing-setup/SKILL.md` | `SKILL 30 (15).md` | None (Single file) |
| [`vibe-security`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/vibe-security/SKILL.md) | `.agents/skills/vibe-security/SKILL.md` | `SKILL 30 (20).md` | None (Single file) |
| [`well-architected-review`](file:///D:/vibe%20coding%20project/ScrollBattle/.agents/skills/well-architected-review/SKILL.md) | `.agents/skills/well-architected-review/SKILL.md` | `SKILL 30 (10).md` | None (Single file) |

