# SCROLL BATTLE
# UI/UX DESIGN BRIEF

**Document Version:** 1.0  
**Product:** Scroll Battle — Personal Anti-Doom-Scrolling Android App  
**Platform:** Android  
**Design Direction:** Premium Dark Minimalism + Warm Orange/Gold  
**Primary Framework:** Jetpack Compose  
**Target Experience:** Personal, offline-first, lightweight, distraction-free  
**Default Daily Limit:** 50 Instagram Reels  
**Reference Material:** Four user-provided mobile UI screenshots  
**Status:** Ready for AI UI implementation

---

# 1. DESIGN VISION

## 1.1 Product Identity

Scroll Battle is a personal digital-wellness dashboard that helps users understand and control their Instagram Reels consumption.

The interface must feel focused, modern, calm, and slightly competitive. It should communicate progress without overwhelming the user with charts, badges, animations, or unnecessary information.

The design should combine:

- The clarity of a productivity dashboard.
- The visual quality of a premium fitness application.
- The simplicity of a minimalist habit tracker.
- The warmth of orange and gold accents against a dark background.

The app must not resemble Instagram or imitate its addictive interaction patterns.

## 1.2 Core Design Objective

Within three seconds of opening the dashboard, the user should understand:

1. How many Reels they have watched today.
2. How many remain before reaching their limit.
3. How much tracked time they have spent.
4. Whether tracking is currently functioning.

## 1.3 Brand Personality

**Primary characteristics**

- Focused
- Disciplined
- Modern
- Private
- Encouraging
- Precise
- Lightweight

**Avoid**

- Childish gamification.
- Aggressive red warning screens as the default.
- Excessive gradients.
- Flashy animations.
- Social-media-style engagement loops.
- Shame-based messaging.
- Cluttered analytics dashboards.

## 1.4 Design Philosophy

The interface should help the user spend less time using the app itself.

Every screen must justify its existence. Every card must communicate useful information. Every animation must serve a purpose.

The product should reward awareness and intentional behavior, not maximize app engagement.

---

# 2. VISUAL REFERENCES

Use the four supplied reference screenshots as the primary visual direction.

Extract the following characteristics:

1. A dark background with strong foreground contrast.
2. Warm orange or amber as the primary accent.
3. Large, readable numeric progress.
4. Rounded cards and restrained borders.
5. Simple navigation with three principal destinations.
6. Clear separation between daily usage and historical analytics.
7. Compact progress indicators.
8. Minimal visual decoration.

These references define the direction, not a requirement to reproduce every screen pixel-for-pixel.

The implementation should adapt the design to native Android conventions and accessibility requirements.

## 2.1 Additional Design Inspiration

Use these products as conceptual references, not as interfaces to copy.

- <Entity category="mobile_app" value="Forest" disambig="Focus and productivity mobile application"/> — focused habit-building and clear task-oriented feedback.
- <Entity category="mobile_app" value="Digital Wellbeing" disambig="Android screen-time and digital wellness application"/> — clarity around usage statistics and screen-time controls.
- <Entity category="mobile_app" value="Linear" disambig="Minimal productivity and project management software"/> — restrained dark surfaces, typography, and visual hierarchy.
- <Entity category="mobile_app" value="Strava" disambig="Fitness tracking and activity analytics application"/> — prominent metrics, progress visualization, and activity history.

Do not reproduce their proprietary assets, exact layouts, logos, or branding.

---

# 3. COLOR PALETTE

Use a consistent dark color system with a single dominant accent family.

All colors must be defined centrally as reusable design tokens. Do not scatter hard-coded color values throughout the UI.

## 3.1 Primary Colors

| Token | Hex | Usage |
|---|---|---|
| Background Primary | `#0D0D0F` | Main app background |
| Background Secondary | `#141416` | Secondary screen surfaces |
| Surface Primary | `#1A1A1D` | Main cards |
| Surface Elevated | `#222226` | Dialogs, elevated surfaces |
| Border Subtle | `#303035` | Card outlines and separators |
| Accent Orange | `#FF8A24` | Primary buttons and progress |
| Accent Gold | `#FFC247` | Highlights and secondary progress |
| Text Primary | `#F5F5F5` | Main text and large metrics |
| Text Secondary | `#A5A5AD` | Descriptions and metadata |
| Text Muted | `#777780` | Timestamps and less prominent labels |

## 3.2 Semantic Colors

| Token | Hex | Usage |
|---|---|---|
| Success | `#4CCB87` | Tracking operational and positive status |
| Warning | `#FFC247` | Approaching daily limit |
| Error | `#FF6464` | Tracking failure and destructive errors |
| Info | `#78B8FF` | Neutral information and setup guidance |

Semantic colors must not replace explanatory text or icons.

For example, a green dot alone must never be the only indication that tracking is active.

## 3.3 Accent Usage Rules

Orange is the main brand accent.

Use it for:

- Primary buttons.
- The active navigation destination.
- Main progress indicators.
- Key numerical emphasis.
- Important interactive elements.

Use gold for:

- Secondary highlights.
- Milestone markers.
- Selected chart details.
- Approaching-limit indicators.

Use red only for meaningful error states and destructive actions.

Do not make every card orange. Most of the interface should remain dark and neutral.

## 3.4 Gradients

Gradients are optional and should be used sparingly.

If used, keep them subtle and restricted to a hero card or progress visualization.

Suggested gradient:

`#FF8A24` → `#FFC247`

Avoid full-screen gradients, glowing text, and excessive neon effects.

## 3.5 Contrast

Text and controls must maintain accessible contrast against their actual backgrounds.

Use a contrast checker during implementation. Aim for WCAG AA contrast for standard text and large text, and ensure meaningful control boundaries remain visible.

Never reduce critical status text to barely visible gray.

---

# 4. TYPOGRAPHY SYSTEM

## 4.1 Font Family

**Primary choice:** Roboto, the standard Android typeface.

Use the platform's default font unless a bundled typeface demonstrably improves the design.

The app must not download fonts at runtime.

## 4.2 Type Scale

| Style | Size | Weight | Usage |
|---|---:|---|---|
| Display Large | 40–48 sp | Bold | Main Reel count |
| Display Medium | 32–36 sp | Bold | Key dashboard metric |
| Heading Large | 24 sp | SemiBold | Screen titles |
| Heading Medium | 20 sp | SemiBold | Card headings |
| Title | 16–18 sp | Medium | Section titles |
| Body | 14–16 sp | Normal | Main descriptions |
| Label | 12–14 sp | Medium | Buttons and labels |
| Caption | 11–12 sp | Normal | Supporting metadata |

These are starting values. Adjust them for the actual screen size and content length.

## 4.3 Numeric Typography

Numbers are the most important visual element in Scroll Battle.

The Reel count must be the most prominent item on the dashboard.

Use tabular numerals where available so that values such as `9 / 50` and `10 / 50` do not cause distracting layout shifts.

## 4.4 Typography Rules

- Avoid all-uppercase paragraphs.
- Use uppercase sparingly for small metric labels.
- Keep line lengths comfortable.
- Avoid excessively thin fonts.
- Use consistent text weights.
- Support Android font scaling.
- Prevent text clipping at larger accessibility font sizes.

---

# 5. SPACING AND LAYOUT SYSTEM

Use a consistent 4 dp spacing grid.

## 5.1 Spacing Tokens

| Token | Value | Usage |
|---|---:|---|
| Space 1 | 4 dp | Tight icon spacing |
| Space 2 | 8 dp | Small internal gaps |
| Space 3 | 12 dp | Compact card spacing |
| Space 4 | 16 dp | Standard padding |
| Space 5 | 20 dp | Section separation |
| Space 6 | 24 dp | Large spacing |
| Space 7 | 32 dp | Major visual separation |
| Space 8 | 40 dp | Hero section spacing |

## 5.2 Screen Margins

- Default horizontal content padding: 20 dp.
- Compact-screen minimum horizontal padding: 16 dp.
- Standard card padding: 16 dp.
- Large hero-card padding: 20–24 dp.
- Standard vertical gap between cards: 12–16 dp.

## 5.3 Corner Radius

| Component | Radius |
|---|---:|
| Large hero card | 24 dp |
| Standard card | 18 dp |
| Compact card | 14 dp |
| Button | 14 dp |
| Input field | 12 dp |
| Small badge | 8 dp |
| Circular progress | Fully circular |

Do not give every component a different radius. Reuse a small set of consistent values.

## 5.4 Borders and Shadows

- Prefer subtle borders over heavy shadows.
- Use elevation only when it communicates hierarchy.
- Avoid excessive blur and glow.
- Dialogs may use a slightly elevated surface.
- Cards should remain distinguishable from the background without appearing visually heavy.

---

# 6. COMPONENT DESIGN SYSTEM

Create reusable Compose components instead of independently styling every screen.

## 6.1 Primary Button

**Appearance**

- Orange background.
- Dark text when contrast is sufficient.
- Rounded corners.
- Height around 52 dp.
- Full width when it is the primary action on a mobile screen.

**States**

- Default.
- Pressed.
- Disabled.
- Loading, only when an operation genuinely takes time.

**Behavior**

Buttons must have clear labels and visible feedback.

Examples:

- Get Started
- Enable Tracking
- Save Changes
- View Progress

## 6.2 Secondary Button

**Appearance**

- Dark or transparent background.
- Subtle outline or neutral surface.
- Light text.
- Same general height as the primary button.

Use for Cancel, Back, and secondary navigation actions.

## 6.3 Icon Button

Use simple, recognizable icons.

Examples:

- Settings.
- Back.
- Calendar.
- Information.
- Refresh.
- Delete.

All icon-only buttons must have accessible descriptions.

## 6.4 Daily Progress Card

This is the most important reusable component.

**Contents**

1. Small label: REELS WATCHED.
2. Large value: `32 / 50`.
3. Remaining allowance: `18 remaining`.
4. Horizontal or circular progress indicator.
5. Optional status label.

**Visual behavior**

- Orange progress when comfortably below the limit.
- Gold progress near the warning threshold.
- A clear limit-reached state at or above the limit.
- No animated counter that makes the number difficult to read.

The color change must be accompanied by text.

## 6.5 Statistic Card

Used for tracked time, sessions, averages, and historical metrics.

**Contents**

- Small icon, optional.
- Label.
- Prominent value.
- Optional explanatory caption.

Examples:

- Time Spent — 24 min.
- Sessions — 5.
- Daily Average — 37 Reels.

## 6.6 Tracking Status Card

Must support:

**Active**

"Tracking active"

**Limited**

"Reel counting is not fully verified"

**Permission required**

"Enable tracking access to continue"

**Unavailable**

"Tracking is currently unavailable"

Each state must include an appropriate icon and an action when recovery is possible.

## 6.7 Progress Bar

- Rounded track.
- Neutral dark track color.
- Orange or gold fill.
- Minimum visual thickness around 8 dp.
- Text label outside or above the bar.

Do not rely on color alone to communicate completion.

## 6.8 Navigation Bar

Three destinations:

- You.
- Battle.
- Progress.

Use a dark surface that visually belongs to the rest of the app.

The selected destination uses orange emphasis. Inactive destinations use secondary text and icons.

Follow Android navigation and system-inset conventions.

## 6.9 Dialogs and Bottom Sheets

Use dialogs for short, important decisions.

Use a bottom sheet for larger but secondary tasks when appropriate.

Destructive actions must include:

- A clear title.
- An explanation of what will be removed.
- Cancel.
- A clearly labeled destructive action.

## 6.10 Charts

Charts must be simple and readable.

- Avoid 3D charts.
- Avoid excessive gridlines.
- Label units.
- Provide a text summary.
- Support accessible descriptions.
- Use orange for the primary data series.
- Use gold for comparisons or highlights.
- Avoid displaying more detail than the data can support.

---

# 7. DASHBOARD STRUCTURE

The dashboard is the primary product surface and must receive the most design attention.

## 7.1 Layout Order

From top to bottom:

1. Header.
2. Daily progress hero card.
3. Tracking status.
4. Time-spent and session statistics.
5. Quick actions.
6. Bottom navigation.

The most important information must appear above secondary statistics.

## 7.2 Header

Left:

- Scroll Battle wordmark or small icon.
- Optional date label.

Right:

- Settings icon.

The header must remain compact. Do not add notifications, profiles, social icons, or unrelated actions.

## 7.3 Hero Card

This card should occupy approximately one-third of the initial visual attention.

Example content:

SCROLL BATTLE

TODAY'S REELS

32 / 50

18 Reels remaining

[Progress indicator]

64% of daily limit used

The hero card may use a subtle orange-to-gold accent, but the text must remain highly readable.

The large number must remain the focal point.

## 7.4 Tracking Status

Place a compact status card directly below the hero card.

Example:

[Status icon] Tracking active

Reel detection verified for the current configuration.

If detection is unverified, use:

[Information icon] Reel count may be estimated

Do not hide tracking limitations inside the settings screen.

## 7.5 Time and Sessions

Use two compact statistic cards, arranged side by side when space allows.

Card A:

TIME SPENT

24 min

Card B:

SESSIONS

5

When the available width is too small, stack the cards vertically.

The time metric must accurately describe what the tracking implementation measures.

## 7.6 Quick Actions

Use two compact action cards or buttons:

- Battle — review your limit.
- Progress — view your trends.

Avoid duplicating all bottom-navigation destinations in a large grid.

## 7.7 Dashboard Empty State

When no Reels have been recorded and tracking is confirmed operational:

0 / 50

50 remaining.

Message:

"Your day is off to a fresh start."

When tracking has not been configured, do not show an unqualified zero.

Instead display:

"Set up tracking to measure your Reels."

Primary action: Enable Tracking.

---

# 8. BATTLE SCREEN DESIGN

## 8.1 Purpose

The Battle screen makes the daily goal explicit and shows the user's current relationship to the limit.

## 8.2 Layout

1. Screen title.
2. Daily count.
3. Remaining allowance.
4. Large progress visualization.
5. Warning or limit status.
6. Blocking status.
7. Edit Limit button.

## 8.3 States

**Under the warning threshold**

- Orange progress.
- Message: "You're within today's limit."

**Approaching the limit**

- Gold progress.
- Message: "You're getting close to your daily limit."

**Limit reached**

- Clear limit-reached heading.
- Current count and limit.
- Remaining allowance: 0.
- Message encouraging the user to stop.
- Action to return to the dashboard or review progress.

**Tracking unavailable**

- Neutral warning card.
- Clear explanation.
- Fix Tracking action.

The screen must not use shame-based messages such as "You failed" or "You have no discipline."

---

# 9. PROGRESS SCREEN DESIGN

## 9.1 Purpose

Help the user understand trends without creating an overwhelming analytics dashboard.

## 9.2 Layout

1. Header: Progress.
2. Daily / Weekly segmented selector.
3. Summary statistics.
4. Main chart.
5. Historical daily list.

## 9.3 Daily View

Display:

- Reels watched.
- Time spent.
- Number of sessions.
- Hourly activity chart.

The chart should show hourly distribution only when timestamp data is available.

## 9.4 Weekly View

Display:

- Total Reels.
- Total tracked time.
- Daily average.
- Highest-activity day.
- Daily Reel counts.

A seven-day bar chart is preferred for the initial version.

Use a clear baseline and simple labels. Avoid complex legends and multiple competing series.

## 9.5 History List

Each row contains:

- Date.
- Reel count.
- Time spent.
- Limit status.

Tapping a row opens the details for that day.

## 9.6 Empty and Partial Data

**No history**

Display an explanatory empty state and a setup action.

**Partial tracking**

Show available statistics and identify missing or unverified data.

Never treat an untracked day as a confirmed zero-usage day.

---

# 10. SETTINGS AND PERMISSION SCREENS

Settings should be visually quieter than the dashboard.

## 10.1 Layout

Group options into clearly labeled sections:

**Tracking**
- Tracking status.
- Permission setup.
- Detection readiness.

**Daily Goal**
- Daily limit.
- Warning thresholds.
- Blocking preference.

**Privacy**
- Local storage explanation.
- Delete history.

**General**
- Reset settings.
- App version.

## 10.2 Input Design

For the daily limit:

- Use a numeric input or stepper.
- Show the valid range.
- Display inline validation.
- Preserve entered values if saving fails.

Avoid a complicated settings form with unnecessary fields.

## 10.3 Permission Explanations

Each permission screen must explain:

- What access is requested.
- Why it is needed.
- What feature depends on it.
- What happens if the user declines.

Do not use frightening or misleading permission language.

---

# 11. LIMIT-REACHED EXPERIENCE

This is a critical behavior, not merely a visual screen.

## 11.1 Visual Hierarchy

1. Clear limit-reached heading.
2. Large current count.
3. Short explanation.
4. Daily reset information.
5. Primary action.
6. Secondary action.

Example:

DAILY LIMIT REACHED

50 / 50 Reels

"You've reached the limit you set for today."

"Take a break and return tomorrow."

Primary action: Return to Dashboard.

Secondary action: View Progress.

## 11.2 Design Rules

- Do not use a full-screen flashing animation.
- Do not use a loud, celebratory success effect.
- Do not punish the user with insulting copy.
- Do not imply that Instagram has been closed if it has not.
- Do not imply that the limit is enforced if the blocking mechanism is unavailable.

The visual experience should communicate a firm boundary while remaining calm.

---

# 12. ONBOARDING DESIGN

## 12.1 Welcome Screen

Use a minimal logo, one clear heading, a short explanation, and a primary action.

Avoid a long carousel of marketing slides.

## 12.2 Privacy Screen

Use short paragraphs and clear labels.

Explain local-only storage and the role of permissions before requesting them.

## 12.3 Permission Setup

Use stacked permission cards with three states:

- Not configured.
- Enabled.
- Unavailable.

Each card should have one clear action.

## 12.4 Readiness Screen

Present a concise checklist of required capabilities.

Clearly distinguish:

- Tracking ready.
- Tracking limited.
- Reel detection unverified.
- Blocking unavailable.

Do not display an overall "Everything is working" success state if an essential capability remains unverified.

---

# 13. HOME-SCREEN WIDGET DESIGN

## 13.1 Primary Widget

Create one compact widget for the initial release.

Recommended content:

SCROLL BATTLE

32 / 50 Reels

18 remaining

[Progress bar]

The widget should be legible against both light and dark home-screen wallpapers where the Android widget system permits appropriate styling.

## 13.2 Widget Design Rules

- Minimal padding.
- Large numeric value.
- Clear contrast.
- No decorative charts.
- No unnecessary animation.
- No tiny status text.
- Avoid requiring a network connection.
- Do not continuously poll in the background.

## 13.3 Widget States

**Normal:** Show the latest available count.

**Limit reached:** Show the limit status and zero remaining.

**No recorded data:** Show setup guidance or a clearly qualified initial state.

**Tracking unavailable:** Show an appropriate status instead of pretending the count is current.

**Stale data:** Display the last available value with a stale-data indicator when necessary.

## 13.4 Interaction

Tapping the widget opens the dashboard.

If an additional progress action is implemented, it may open the Progress screen.

The widget must not contain controls that imply functionality the operating system cannot support.

---

# 14. MOBILE RESPONSIVENESS

The initial target is a standard Android phone in portrait orientation.

However, the design must support different device widths, font sizes, and system insets.

## 14.1 Compact Phones

For narrower screens:

- Use 16 dp horizontal padding.
- Stack statistic cards vertically if needed.
- Reduce decorative spacing before reducing text readability.
- Keep the main count visible.
- Avoid horizontal scrolling on the dashboard.
- Allow vertical scrolling for secondary content.

## 14.2 Standard Phones

For typical phone widths:

- Use approximately 20 dp horizontal padding.
- Keep the hero card prominent.
- Display two statistic cards side by side when they fit.
- Use a persistent bottom navigation bar on the main destinations.

## 14.3 Large Phones and Foldables

For wider screens:

- Center the content.
- Apply a sensible maximum content width.
- Avoid stretching cards across the entire display.
- Use two-column layouts when they improve clarity.
- Preserve the same information hierarchy.

## 14.4 Landscape

Landscape is not the primary design target.

The interface must remain functional in landscape:

- Avoid clipped buttons.
- Permit vertical scrolling.
- Keep navigation accessible.
- Avoid fixed-height content that becomes inaccessible.

## 14.5 System Insets

Respect Android status-bar, navigation-bar, gesture-navigation, and display-cutout insets.

Use edge-to-edge layouts where appropriate, with sufficient contrast and spacing around system controls.

## 14.6 Font Scaling

Support Android accessibility font scaling.

When text enlarges:

- Cards may grow vertically.
- Buttons may wrap labels if necessary.
- The daily count must remain legible.
- Critical actions must not become clipped or unreachable.

---

# 15. INTERACTION AND MOTION DESIGN

## 15.1 Animation Philosophy

Use motion sparingly.

The app is designed to reduce distraction, not entertain the user.

## 15.2 Recommended Animations

- Subtle progress-bar transitions.
- Brief state transitions when switching tabs.
- Small button-press feedback.
- Smooth but restrained chart updates.

## 15.3 Avoid

- Confetti.
- Continuous bouncing elements.
- Repeated pulsing notifications.
- Excessive parallax.
- Animated backgrounds.
- Long transition sequences.
- Reward animations that encourage opening the app unnecessarily.

## 15.4 Reduced Motion

Respect the system's animation and reduced-motion preferences where applicable.

All essential information must remain available without animation.

---

# 16. UX WRITING AND MICROCOPY

The language should be direct, calm, and supportive.

## 16.1 Preferred Copy

**Dashboard**
- "Reels watched today"
- "18 remaining"
- "Tracking active"

**Warning**
- "You're getting close to your daily limit."

**Limit reached**
- "You've reached today's limit."

**Permission**
- "Enable tracking access to measure your Instagram activity."

**Unverified count**
- "Reel counting has not been fully verified."

**No history**
- "Your recorded activity will appear here."

**Tracking error**
- "Tracking is unavailable. Check your permissions and try again."

## 16.2 Avoid

- "You failed."
- "You have no self-control."
- "You're addicted."
- "You wasted your day."
- "Perfect tracking guaranteed."
- "You can never bypass this limit."

Never use language that diagnoses a medical or psychological condition.

---

# 17. ACCESSIBILITY REQUIREMENTS

The design must support users with different visual and motor needs.

Requirements:

- Adequate text contrast.
- Readable type sizes.
- Support for Android font scaling.
- Accessible labels for icon-only buttons.
- Meaningful TalkBack descriptions.
- Logical focus order.
- Sufficient touch-target sizes.
- No status communicated solely through color.
- No interaction dependent exclusively on gestures.
- Clear error descriptions.
- Keyboard navigation where relevant.

Charts must provide textual summaries so that their meaning is not limited to visual interpretation.

---

# 18. DATA ACCURACY AND VISUAL TRUST

The UI must reflect the actual reliability of the underlying tracking system.

## 18.1 Confirmed Values

Display normal numeric progress when the tracking engine has sufficient validated signals.

## 18.2 Estimated Values

Use a visible estimate or unverified label when the detection method cannot guarantee an exact Reel count.

## 18.3 Stale Values

If tracking has stopped, distinguish the last saved count from a live current count.

## 18.4 Missing Data

Never represent an untracked period as zero usage.

## 18.5 Blocking State

Only show "Blocking active" when the blocking mechanism has been verified as operational.

This is a core trust requirement and must not be overridden for visual simplicity.

---

# 19. SCREEN-BY-SCREEN DESIGN CHECKLIST

The AI app builder must implement the following visual destinations and states.

1. Welcome.
2. Privacy and tracking explanation.
3. Permission setup.
4. Tracking readiness.
5. Dashboard.
6. Battle / daily limit.
7. Limit settings.
8. Limit reached.
9. Progress overview.
10. Daily details.
11. Settings.
12. Tracking recovery.
13. Delete history confirmation.
14. Reset settings confirmation.
15. Home-screen widget.
16. Optional interruption or blocking UI.
17. Troubleshooting.

The design system must include reusable loading, empty, error, permission-missing, stale-data, and success states.

---

# 20. TECHNICAL DESIGN HANDOFF REQUIREMENTS

The AI app builder must translate this brief into native Android components.

## Required Implementation

- Kotlin.
- Jetpack Compose.
- Centralized color and typography tokens.
- Reusable cards and buttons.
- Responsive layouts.
- Native navigation.
- Native Android system-inset handling.
- Accessibility support.
- Real local data bindings.
- Actual permission states.
- Real dashboard statistics.

## Prohibited Shortcuts

- Static mock data presented as live tracking.
- Fake buttons.
- Placeholder navigation.
- Hard-coded success states.
- Fake Reel detection.
- Fake widget refresh behavior.
- Unnecessary network services.
- Decorative charts that do not reflect actual data.

If a feature is not yet implemented, show an honest unavailable or placeholder state during development.

Do not represent a prototype as a finished tracking or blocking system.

---

# 21. FINAL DESIGN ACCEPTANCE CRITERIA

The design is approved when:

1. The dashboard communicates daily usage immediately.
2. The 50-Reel limit is visually prominent.
3. Remaining allowance is easy to understand.
4. Tracking reliability is visible.
5. The app uses a consistent dark orange-and-gold design system.
6. All screens use consistent spacing, typography, and component styles.
7. Navigation is clear and predictable.
8. Daily and weekly analytics are readable.
9. Empty, error, and permission states are designed.
10. The widget displays the latest available local data.
11. The layout works on compact and large Android phones.
12. Text scales without clipping.
13. Essential interactions work with accessibility services enabled.
14. The UI contains no fake metrics or misleading success messages.
15. The app remains visually calm and distraction-free.

---

# 22. FINAL DESIGN DIRECTION

Scroll Battle should feel like a premium, minimalist personal-control dashboard.

The main visual signature is a near-black background, warm orange progress indicators, golden highlights, rounded cards, and large numeric typography.

The dashboard comes first. Tracking status must always be transparent. Analytics should remain simple. Settings should remain quiet. The home-screen widget should show only the information the user needs.

The final design should be attractive enough to feel polished but restrained enough to support the product's real mission: helping the user spend less time doom-scrolling.

**Design principle: Make the user's behavior visible, make their daily goal clear, and never let visual polish hide technical uncertainty.**