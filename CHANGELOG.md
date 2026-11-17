# Changelog

## 1.3.0 — Reliability and hard-limit repair

- Scope accessibility event delivery and detection to Instagram.
- Count the first Reel when entering the Reel feed and filter scroll counts to Reel-player containers.
- Persist seven-day usage records and make the Day/Week analytics selector functional.
- Reduce requested accessibility flags and stop using an accessibility overlay as a fallback for normal overlays.
- Enforce the 50-Reel cap across the entire Instagram app, including Feed, profiles and DMs.
- Return users to Android Home and show the ScrollBattle limit screen when the cap is reached.
- Re-block Instagram whenever it is reopened until the 04:00 local-time daily reset.
- Debounce duplicate accessibility events while allowing a fresh Instagram foreground launch to be blocked immediately.
- Persist the configured daily allowance and recent session records.
- Reset daily counters at the documented 04:00 local-time boundary.
- Keep the daily Reel allowance hard: the maths challenge no longer adds extra Reels.
- Correct permission readiness to account for overlay permission.
- Remove the unused notification permission prompt.
- Correct the activity soft-input configuration.
- Add automated build and unit-test checks in GitHub Actions.

> Reel detection remains heuristic and must be verified against the installed Instagram version and physical devices. Android does not expose an official API for reading Instagram's internal Reel page index or force-stopping Instagram.
