package com.scrollbattle.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.scrollbattle.MainActivity
import com.scrollbattle.data.DefaultScrollBattleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ScrollBattleAccessibilityService : AccessibilityService() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private lateinit var repository: DefaultScrollBattleRepository
    private lateinit var overlayManager: FloatingOverlayManager

    private var currentPackage: String? = null
    private val targetPackage = "com.instagram.android"

    private var lastReelPosition = -1

    companion object {
        var isServiceRunning = false
            private set
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        isServiceRunning = true

        repository = DefaultScrollBattleRepository.getInstance(applicationContext)
        overlayManager = FloatingOverlayManager(this)

        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_VIEW_SCROLLED or
                    AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                    AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            notificationTimeout = 100
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS or
                    AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
        }
        serviceInfo = info

        repository.refreshPermissions(applicationContext)

        val filter = android.content.IntentFilter().apply {
            addAction("com.scrollbattle.ACTION_SHOW_PILL")
            addAction("com.scrollbattle.ACTION_SHOW_INTERRUPTION")
            addAction("com.scrollbattle.ACTION_HIDE_OVERLAYS")
        }
        androidx.core.content.ContextCompat.registerReceiver(
            this,
            testReceiver,
            filter,
            androidx.core.content.ContextCompat.RECEIVER_EXPORTED
        )

        // Observe state changes in real time
        serviceScope.launch {
            repository.state.collectLatest { state ->
                val shouldShow = isTargetApp(currentPackage) && isReelsActive()
                if (shouldShow) {
                    val isLimitReached = state.todayReels >= state.totalAllowance

                    if (isLimitReached) {
                        enforceLimitIfExceeded()
                    } else {
                        overlayManager.hideInterruption()
                        overlayManager.showPill(state.todayReels, state.totalAllowance)
                    }
                } else if (!isTargetApp(currentPackage)) {
                    overlayManager.hideAll()
                }
            }
        }
    }

    private var lastSwipeTimestamp = 0L
    private var lastContentCheckTimestamp = 0L

    private fun isTargetApp(pkg: String?): Boolean {
        return pkg == "com.instagram.android" || 
               pkg == "com.instagram.lite" || 
               pkg == "com.google.android.youtube" || 
               pkg == "com.snapchat.android"
    }

    private fun enforceLimitIfExceeded(): Boolean {
        val state = repository.state.value
        if (state.todayReels >= state.totalAllowance) {
            overlayManager.hideAll()
            // Force stop / kick user out of Instagram immediately!
            performGlobalAction(GLOBAL_ACTION_HOME)

            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("destination", "limit_reached")
            }
            startActivity(intent)
            return true
        }
        return false
    }

    private fun isReelsActive(eventSource: android.view.accessibility.AccessibilityNodeInfo? = null): Boolean {
        val root = rootInActiveWindow ?: eventSource ?: return false
        try {
            // Exclude Direct Messages and other non-video screens
            val direct = root.findAccessibilityNodeInfosByViewId("com.instagram.android:id/direct_thread_layout")
            if (!direct.isNullOrEmpty()) return false

            val commentBox = root.findAccessibilityNodeInfosByViewId("com.instagram.android:id/layout_comment_thread_edittext")
            if (!commentBox.isNullOrEmpty()) return false

            // Direct checks for verified short-video containers (from BrainPal reverse-engineering)
            val containerIds = listOf(
                "com.instagram.android:id/clips_viewer_view_pager",
                "com.instagram.android:id/clips_video_container",
                "com.instagram.android:id/clips_media_component",
                "com.instagram.android:id/reel_viewer",
                "com.instagram.android:id/clips_swipe_refresh_container",
                "com.instagram.android:id/clips_action_bar_container",
                "com.instagram.android:id/reel_viewer_media_layout",
                "com.google.android.youtube:id/reel_player_page_content",
                "com.google.android.youtube:id/reel_recycler",
                "com.snapchat.android:id/spotlight_container"
            )
            for (id in containerIds) {
                val nodes = root.findAccessibilityNodeInfosByViewId(id)
                if (!nodes.isNullOrEmpty()) return true
            }

            // Check event source's ID
            val sourceId = eventSource?.viewIdResourceName ?: ""
            if (sourceId.contains("clips") || sourceId.contains("reel") || sourceId.contains("spotlight")) {
                return true
            }

            // Fallback: Check for distinct Reels UI text markers (for newer or obfuscated Instagram builds)
            val reelsMarkers = listOf("Original audio", "Remix", "Reels", "Reel by", "Use audio")
            for (marker in reelsMarkers) {
                val nodes = root.findAccessibilityNodeInfosByText(marker)
                if (!nodes.isNullOrEmpty()) {
                    val homeHeader = root.findAccessibilityNodeInfosByText("Instagram")
                    val homeLogo = root.findAccessibilityNodeInfosByViewId("com.instagram.android:id/action_bar_home_logo")
                    if (homeHeader.isNullOrEmpty() && homeLogo.isNullOrEmpty()) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            // ignore
        }
        return false
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val eventPackage = event?.packageName?.toString() ?: return

        // 0. If in target app and 50-reels limit reached, FORCE CLOSE & KICK OUT IMMEDIATELY
        if (isTargetApp(eventPackage) && enforceLimitIfExceeded()) {
            return
        }

        // 1. App or Window transition
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val oldPackage = currentPackage
            currentPackage = eventPackage

            if (isTargetApp(oldPackage) && !isTargetApp(eventPackage)) {
                // User navigated away from target short-video app
                overlayManager.hideAll()
                return
            }

            if (isTargetApp(eventPackage)) {
                if (isReelsActive(event.source)) {
                    val state = repository.state.value
                    overlayManager.showPill(state.todayReels, state.totalAllowance)
                } else {
                    // Inside target app but not in Reels (e.g. DMs, Profile, Feed) -> hide pill
                    overlayManager.hidePill()
                }
            }
        }

        // 2. Window Content Changed: Throttled to at most once per 800ms
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && isTargetApp(eventPackage)) {
            val now = System.currentTimeMillis()
            if (now - lastContentCheckTimestamp > 800L) {
                lastContentCheckTimestamp = now
                val inReels = isReelsActive(event.source)
                val state = repository.state.value
                if (inReels && !overlayManager.isPillVisible) {
                    overlayManager.showPill(state.todayReels, state.totalAllowance)
                } else if (!inReels && overlayManager.isPillVisible) {
                    overlayManager.hidePill()
                }
            }
            return
        }

        // 3. ONLY ON ACTUAL VERTICAL SHORT-VIDEO SCROLL
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED && isTargetApp(eventPackage)) {
            // Ignore horizontal swipes (stories or photo carousels)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                val deltaY = event.scrollDeltaY
                val deltaX = event.scrollDeltaX
                if (deltaX != 0 && deltaY == 0) {
                    return
                }
            }

            // Only count if verified inside Reels container
            if (!isReelsActive(event.source)) {
                return
            }

            // Strict debounce: minimum 700ms between reel increments
            val now = System.currentTimeMillis()
            if (now - lastSwipeTimestamp < 700L) {
                return
            }
            lastSwipeTimestamp = now

            val srcName = if (eventPackage.contains("youtube")) "YouTube Shorts" else "Instagram Reels"
            repository.recordReelTransition(srcName)

            // If this swipe pushed count to 50, kick user out of Instagram immediately!
            if (enforceLimitIfExceeded()) return

            val state = repository.state.value
            overlayManager.showPill(state.todayReels, state.totalAllowance)
        }
    }

    private val testReceiver = object : android.content.BroadcastReceiver() {
        override fun onReceive(context: android.content.Context?, intent: android.content.Intent?) {
            val state = repository.state.value
            when (intent?.action) {
                "com.scrollbattle.ACTION_SHOW_PILL" -> {
                    overlayManager.showPill(state.todayReels, state.totalAllowance)
                }
                "com.scrollbattle.ACTION_SHOW_INTERRUPTION" -> {
                    overlayManager.showInterruption(
                        currentReels = state.todayReels,
                        totalAllowance = state.totalAllowance,
                        onStartChallenge = {
                            repository.triggerNavigation("maths_challenge")
                            val mainIntent = Intent(this@ScrollBattleAccessibilityService, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                putExtra("destination", "maths_challenge")
                            }
                            startActivity(mainIntent)
                        },
                        onDoneForToday = {
                            performGlobalAction(GLOBAL_ACTION_HOME)
                        }
                    )
                }
                "com.scrollbattle.ACTION_HIDE_OVERLAYS" -> {
                    overlayManager.hideAll()
                }
            }
        }
    }

    override fun onInterrupt() {
        overlayManager.hideAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
        try {
            unregisterReceiver(testReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        overlayManager.hideAll()
        serviceScope.cancel()
    }
}
