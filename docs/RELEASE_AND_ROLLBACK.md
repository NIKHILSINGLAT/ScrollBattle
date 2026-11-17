# Release, versioning and rollback

## Branch policy

- `main` is the stable branch.
- Create one branch per repair or feature: `fix/<short-description>` or `feature/<short-description>`.
- Do not commit untested fixes directly to `main`.
- Open a pull request and review the diff before merging.
- Keep each commit focused so a regression can be traced to a specific change.

## Android version numbers

Update both values in `app/build.gradle.kts` for every APK release:

- `versionCode`: increase by at least 1 for every published APK.
- `versionName`: human-readable release, following MAJOR.MINOR.PATCH.

Never reuse a published versionCode.

## Required checks before release

1. GitHub Actions succeeds for `testDebugUnitTest` and `assembleDebug`.
2. Install the APK on the target Samsung device.
3. Confirm Accessibility and overlay permission status.
4. Count at least 20 consecutive Instagram Reel transitions at normal and fast swipe speeds.
5. Confirm profile, home feed, comments and DMs do not increment the Reel count.
6. Confirm Reel 50 triggers one intervention, not repeated activity launches.
7. Confirm reopening Instagram to the Reel feed triggers intervention again while ordinary Instagram screens remain usable.
8. Confirm the daily count survives force-stop/restart and resets at 04:00 local time.
9. Confirm Google Pay can be opened and tested after the service is disabled. Then test with the service enabled; compatibility is device/provider-dependent and must not be assumed.

## Rollback

If a release introduces a regression:

1. Stop distributing the affected APK.
2. Identify the last known-good Git tag or commit in GitHub.
3. Revert the offending commit on a new branch, or create a hotfix from the last stable tag.
4. Increase `versionCode` even when reverting code. Android will not install an APK with a lower versionCode over a newer installed APK.
5. Run CI and the physical-device checklist again.
6. Publish the corrected APK and document the change in `CHANGELOG.md`.

## Important platform limitation

ScrollBattle uses Android Accessibility events and UI heuristics. Instagram does not provide a public API for Reel page transitions. Instagram UI changes, accessibility-node changes, OEM behaviour and Android lifecycle policies can affect detection. A successful CI build does not prove correct Reel counting; physical-device validation is mandatory.
