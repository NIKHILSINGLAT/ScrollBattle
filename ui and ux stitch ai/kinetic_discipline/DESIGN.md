---
name: Kinetic Discipline
colors:
  surface: '#131316'
  surface-dim: '#131316'
  surface-bright: '#39393c'
  surface-container-lowest: '#0e0e11'
  surface-container-low: '#1b1b1e'
  surface-container: '#1f1f22'
  surface-container-high: '#2a2a2d'
  surface-container-highest: '#353438'
  on-surface: '#e4e1e5'
  on-surface-variant: '#ddc1b0'
  inverse-surface: '#e4e1e5'
  inverse-on-surface: '#303033'
  outline: '#a58c7c'
  outline-variant: '#564336'
  surface-tint: '#ffb785'
  primary: '#ffb785'
  on-primary: '#502500'
  primary-container: '#ff8a24'
  on-primary-container: '#643000'
  inverse-primary: '#954a00'
  secondary: '#f9bd42'
  on-secondary: '#412d00'
  secondary-container: '#bc8700'
  on-secondary-container: '#392600'
  tertiary: '#60dd98'
  on-tertiary: '#00391f'
  tertiary-container: '#3ebf7c'
  on-tertiary-container: '#004829'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#ffdcc6'
  primary-fixed-dim: '#ffb785'
  on-primary-fixed: '#301400'
  on-primary-fixed-variant: '#713700'
  secondary-fixed: '#ffdea7'
  secondary-fixed-dim: '#f9bd42'
  on-secondary-fixed: '#271900'
  on-secondary-fixed-variant: '#5e4200'
  tertiary-fixed: '#7efab2'
  tertiary-fixed-dim: '#60dd98'
  on-tertiary-fixed: '#002110'
  on-tertiary-fixed-variant: '#00522f'
  background: '#131316'
  on-background: '#e4e1e5'
  surface-variant: '#353438'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 48px
    fontWeight: '700'
    lineHeight: 56px
  display-lg-mobile:
    fontFamily: Inter
    fontSize: 36px
    fontWeight: '700'
    lineHeight: 44px
  headline-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  headline-sm:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Roboto Flex
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Roboto Flex
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Roboto Flex
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  margin: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system frames digital discipline not through guilt or aggressive restriction, but through calm athletic precision. Designed specifically for an Android-first environment, it merges the systematic utility of Material Design 3 with the focused, performance-driven typography and information density of high-tier training trackers.

The emotional signature is stoic, quiet, and deliberate. Interfaces avoid shrill alarms, neon glows, and patronizing anti-pattern messaging. Instead, doom-scrolling metrics are treated like split times or athletic telemetry: objective, trackable, and optimized through personal agency. Deep monochromatic charcoal foundations reduce visual fatigue, while precise citrus-amber accents highlight moments of active resistance, intentional breaks, and focus streaks.

## Colors

The palette establishes an ultra-low-strain dark baseline with calibrated semantic tiers:

- **Canvas & Surfaces**: The base canvas is `#0D0D0F`, stepping upward to `#141416` for secondary sections, `#1A1A1D` for standard interactive cards, and `#222226` for elevated sheets, modals, and floating action surfaces.
- **Structural Lines**: Borders, dividers, and container strokes use `#303035` at 1dp width to delineate structural space without harsh contrast.
- **Telemetry Accents**: 
  - Primary Citrus (`#FF8A24`) identifies focused interactive targets, active tracking states, and personal records.
  - Accent Gold (`#FFC247`) denotes streaks, milestones, and cautionary screen thresholds.
  - Recovery Green (`#4CCB87`) highlights reclaimed time and completed focus sessions.
  - Critical Red (`#FF6464`) and Informational Slate Blue (`#78B8FF`) remain reserved strictly for destructive operations and system diagnostic status.
- **Typography Contrast**: Primary text is fixed at `#F5F5F5` for high-legibility readouts. Secondary metadata rests at `#A5A5AD`, and tertiary hints/de-emphasized axes scale back to `#777780`.

## Typography

Typography prioritizes fast numerical parsing and effortless scanning during high-friction usage moments:

- **Headlines & Metric Data (Inter)**: Applied to all high-level totals, timer readouts, screen-time split metrics, and actionable titles. Digits should always render with tabular figures (`tnum`) enabled to prevent layout jitter during active countdowns and live session timers.
- **Body & Informational Copy (Roboto Flex)**: Handles contextual logs, session reflections, settings copy, and detailed statistical breakdowns. Its variable rendering engine provides optimal native legibility on Android displays at compact sizes.
- **Labels & Micro-Data (Inter)**: Tight, uppercase-capable tracking for tags, session badges, status chips, and graph axis timestamps.

## Layout & Spacing

The spatial model operates strictly on an 8dp foundational grid, with a 4dp half-step for micro-alignments, compact tag paddings, and dense telemetry graphs.

- **Mobile Viewport Grid**: Uses a fluid single-column layout flanked by fixed 16dp (`1rem`) outer canvas margins. Components and internal card layouts span standard horizontal gutters of 16dp (`1rem`).
- **Vertical Rhythm**: 
  - Standard spacing between consecutive metric modules is 12dp to 16dp.
  - Section-to-section breaks use 24dp (`1.5rem`) or 32dp (`2rem`) to retain clear visual grouping without structural clutter.
- **Safe Area Insets**: Edge-to-edge system bars (Android Status Bar and Gesture Navigation bar) maintain transparent draws, enforcing internal padding reservations that dynamically offset top and bottom app chrome.

## Elevation & Depth

Visual hierarchy relies on matte surface stacking and controlled 1dp boundaries rather than physical drop shadows or artificial bloom:

- **Level 0 (App Canvas)**: `#0D0D0F`, flat and unbordered.
- **Level 1 (Group Containers)**: `#141416`, bordered with `#303035` at 1dp.
- **Level 2 (Cards & Active Modules)**: `#1A1A1D`, framed with `#303035` at 1dp.
- **Level 3 (Modals, Overlays & Floating Bars)**: `#222226`, stroked with `#303035`, accompanied by a minimal ambient shadow (`0px 8px 24px rgba(0, 0, 0, 0.45)`) to cleanly lift the layer over the scrollable canvas.
- **Interactive State Depth**: Taps and presses do not scale or pop with glow effects. Instead, surfaces shift in luminance (e.g., `#1A1A1D` shifts to `#222226` on press) backed by immediate, crisp haptic feedback.

## Shapes

Corner radii correspond directly to structural hierarchy, reinforcing containment and tactile reach:

- **Hero & Summary Cards**: 24dp corner radius. Applied to top-level daily battle dashboards, streak visualizations, and primary focus charts.
- **Standard Content Cards**: 18dp corner radius. Standard container for app breakdown rows, metric comparisons, and session intervals.
- **Buttons & Tactical Triggers**: 14dp corner radius. Balances touch target comfort with modern athletic compactness.
- **Chips, Badges & Micro-Pills**: 8dp corner radius. Keeps metric tags, category labels, and status indicators compact and crisp.
- **Inner Nested Elements**: Any interactive module nested inside an 18dp container must use an internal radius of 10dp to 12dp to maintain concentric curvature.

## Components

### Buttons
- **Primary Action (Commit / Lock-In)**: Background in solid `#FF8A24`, text in `#0D0D0F` (`Inter 14dp/600`), 14dp corner radius, minimum height of 48dp.
- **Secondary Action (Manage / Adjust)**: Background in `#1A1A1D`, 1dp outline in `#303035`, text in `#F5F5F5`.
- **Ghost Action**: Transparent background, text in `#A5A5AD`, hover/pressed state tinted with 8% white.

### Metric Cards & Hero Modules
- **Hero Session Card**: Background `#1A1A1D`, stroke 1dp `#303035`, 24dp radius, 20dp internal padding. Features large tabular numerical readouts (`Inter Display`) juxtaposed with concise secondary performance tags.
- **Application Friction List Items**: Background `#141416`, separated by 1dp `#303035` lines or spaced within 18dp rounded cards. Includes app icon, usage delta indicators (`#4CCB87` for reclaimed time, `#FFC247` for high usage), and a minimal linear duration bar.

### Chips & Telemetry Badges
- **Status Badge**: Background `#222226`, 8dp radius, 1dp stroke `#303035`. Internal padding of 4dp vertical by 8dp horizontal. Text uses `label-sm` in `#A5A5AD` or semantic tones (`#4CCB87` / `#FFC247`).
- **Interactive Filter Chip**: Unselected: `#141416` with `#303035` stroke. Selected: `#FF8A24` background with `#0D0D0F` label.

### Form Inputs & Limit Selectors
- **Input Fields**: Background `#141416`, 1dp border `#303035`, 14dp radius, 16dp horizontal padding. Focused state shifts the border to `#FF8A24` without an outer glow. Secondary placeholders styled in `#777780`.
- **Toggle Switches**: Track uses `#222226` with a 1dp border in `#303035` when inactive; fills `#FF8A24` when engaged. Thumb is crisp white `#F5F5F5` with zero offset blur.

### Battle Progress & Bar Meters
- **Metric Trackers**: Low-profile tracks (4dp to 8dp height) set in `#222226` with a 4dp radius. Filled indicators render in solid `#FF8A24` or `#4CCB87`, avoiding gradients to uphold pure utilitarian clarity.