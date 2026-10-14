package com.scrollbattle.service

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.scrollbattle.MainActivity

class FloatingOverlayManager(private val context: Context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private var pillView: View? = null
    private var pillText: TextView? = null
    private var isPillShowing = false
    val isPillVisible: Boolean
        get() = isPillShowing

    private var interruptionView: View? = null
    private var isInterruptionShowing = false

    private fun dpToPx(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun getOverlayType(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (android.provider.Settings.canDrawOverlays(context)) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            }
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }
    }

    private var pillMascot: ImageView? = null

    private fun loadPillPosition(): Pair<Int, Int> {
        val prefs = context.getSharedPreferences("overlay_prefs", Context.MODE_PRIVATE)
        val metrics = context.resources.displayMetrics
        val defaultX = (metrics.widthPixels / 2) - dpToPx(55)
        val defaultY = dpToPx(36)
        return Pair(prefs.getInt("pill_x", defaultX), prefs.getInt("pill_y", defaultY))
    }

    private fun savePillPosition(x: Int, y: Int) {
        val prefs = context.getSharedPreferences("overlay_prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("pill_x", x).putInt("pill_y", y).apply()
    }

    // ==========================================
    // FLOATING LIVE COUNTER PILL (DRAGGABLE)
    // ==========================================
    fun showPill(currentReels: Int, totalAllowance: Int) {
        if (isPillShowing) {
            updatePill(currentReels, totalAllowance)
            return
        }

        try {
            val isOverGoal = currentReels >= totalAllowance && totalAllowance > 0

            val pill = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(dpToPx(14), dpToPx(6), dpToPx(16), dpToPx(6))

                val backgroundDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = dpToPx(24).toFloat()
                    setColor(if (isOverGoal) Color.parseColor("#4A1504") else Color.parseColor("#4A2805"))
                    setStroke(dpToPx(1), if (isOverGoal) Color.parseColor("#DC2626") else Color.parseColor("#D97706"))
                }
                background = backgroundDrawable

                // Brain Mascot Icon (matching reference screenshot)
                val mascotView = ImageView(context).apply {
                    val resId = if (isOverGoal) {
                        com.scrollbattle.R.drawable.brain_icon_tired
                    } else {
                        com.scrollbattle.R.drawable.brain_icon
                    }
                    setImageResource(resId)
                    val p = dpToPx(26)
                    layoutParams = LinearLayout.LayoutParams(p, p).apply {
                        setMargins(0, 0, dpToPx(8), 0)
                    }
                    scaleType = ImageView.ScaleType.FIT_CENTER
                }
                addView(mascotView)
                pillMascot = mascotView

                // Large Bold Reel Counter Text
                pillText = TextView(context).apply {
                    text = currentReels.toString()
                    textSize = 18f
                    setTextColor(Color.parseColor("#FFA726")) // Vivid Amber/Gold
                    typeface = Typeface.DEFAULT_BOLD
                }
                addView(pillText)
            }

            val (savedX, savedY) = loadPillPosition()

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                getOverlayType(),
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
                x = savedX
                y = savedY
            }

            // Draggable Touch Listener: Allows user to move the pill anywhere on screen!
            pill.setOnTouchListener(object : View.OnTouchListener {
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f
                private var isDragging = false

                override fun onTouch(v: View?, event: android.view.MotionEvent?): Boolean {
                    val p = pillView?.layoutParams as? WindowManager.LayoutParams ?: return false
                    when (event?.action) {
                        android.view.MotionEvent.ACTION_DOWN -> {
                            initialX = p.x
                            initialY = p.y
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            isDragging = false
                            return true
                        }
                        android.view.MotionEvent.ACTION_MOVE -> {
                            val dx = (event.rawX - initialTouchX).toInt()
                            val dy = (event.rawY - initialTouchY).toInt()
                            if (Math.abs(dx) > 8 || Math.abs(dy) > 8) {
                                isDragging = true
                            }
                            p.x = initialX + dx
                            p.y = initialY + dy
                            try {
                                windowManager.updateViewLayout(pillView, p)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            return true
                        }
                        android.view.MotionEvent.ACTION_UP -> {
                            if (isDragging) {
                                savePillPosition(p.x, p.y)
                            } else {
                                // Tap without drag opens ScrollBattle
                                val intent = Intent(context, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                }
                                context.startActivity(intent)
                            }
                            return true
                        }
                    }
                    return false
                }
            })

            try {
                windowManager.addView(pill, params)
                pillView = pill
                isPillShowing = true
            } catch (e: Exception) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                    params.type == WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY &&
                    android.provider.Settings.canDrawOverlays(context)
                ) {
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                    windowManager.addView(pill, params)
                    pillView = pill
                    isPillShowing = true
                } else {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updatePill(currentReels: Int, totalAllowance: Int) {
        if (!isPillShowing || pillView == null) {
            showPill(currentReels, totalAllowance)
            return
        }
        pillText?.text = currentReels.toString()

        val isOverGoal = currentReels >= totalAllowance && totalAllowance > 0
        val ratio = if (totalAllowance > 0) currentReels.toFloat() / totalAllowance else 0f

        pillMascot?.setImageResource(
            if (isOverGoal || ratio >= 0.9f) com.scrollbattle.R.drawable.brain_icon_tired
            else com.scrollbattle.R.drawable.brain_icon
        )

        pillView?.let { view ->
            val bg = view.background as? GradientDrawable ?: return@let
            if (isOverGoal || ratio >= 0.9f) {
                bg.setColor(Color.parseColor("#4A1504")) // Warning Red-Brown
                bg.setStroke(dpToPx(1), Color.parseColor("#DC2626"))
                pillText?.setTextColor(Color.parseColor("#FCA5A5"))
            } else if (ratio >= 0.8f) {
                bg.setColor(Color.parseColor("#4A2805")) // Amber
                bg.setStroke(dpToPx(1), Color.parseColor("#EA580C"))
                pillText?.setTextColor(Color.parseColor("#FFA726"))
            } else {
                bg.setColor(Color.parseColor("#381F06"))
                bg.setStroke(dpToPx(1), Color.parseColor("#D97706"))
                pillText?.setTextColor(Color.parseColor("#F59E0B"))
            }
        }
    }

    fun hidePill() {
        if (!isPillShowing || pillView == null) return
        try {
            windowManager.removeView(pillView)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            pillView = null
            pillText = null
            pillMascot = null
            isPillShowing = false
        }
    }

    // ==========================================
    // INTERRUPTION OVERLAY CARD
    // ==========================================
    fun showInterruption(
        currentReels: Int,
        totalAllowance: Int,
        onStartChallenge: () -> Unit,
        onDoneForToday: () -> Unit
    ) {
        if (isInterruptionShowing) return

        try {
            // Full Screen Scrim Container
            val rootLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setBackgroundColor(Color.parseColor("#E60C0A09")) // 90% dimmed dark scrim
                setPadding(dpToPx(24), dpToPx(32), dpToPx(24), dpToPx(32))

                // Card Container
                val card = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER_HORIZONTAL
                    setPadding(dpToPx(24), dpToPx(28), dpToPx(24), dpToPx(28))

                    val cardBg = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = dpToPx(24).toFloat()
                        setColor(Color.parseColor("#1C1917")) // Dark Charcoal Card Surface
                        setStroke(dpToPx(1), Color.parseColor("#292524"))
                    }
                    background = cardBg

                    // Alert Icon Badge
                    val iconBadge = LinearLayout(context).apply {
                        gravity = Gravity.CENTER
                        val badgeBg = GradientDrawable().apply {
                            shape = GradientDrawable.OVAL
                            setColor(Color.parseColor("#2E1C0A"))
                            setStroke(dpToPx(1), Color.parseColor("#D97706"))
                        }
                        background = badgeBg
                        setPadding(dpToPx(14), dpToPx(14), dpToPx(14), dpToPx(14))

                        val icon = TextView(context).apply {
                            text = "⚡"
                            textSize = 24f
                        }
                        addView(icon)
                    }
                    addView(iconBadge)

                    // Title
                    val titleView = TextView(context).apply {
                        text = "That's today's $totalAllowance."
                        textSize = 22f
                        setTextColor(Color.parseColor("#FAFAF9"))
                        typeface = Typeface.DEFAULT_BOLD
                        gravity = Gravity.CENTER
                        setPadding(0, dpToPx(16), 0, dpToPx(8))
                    }
                    addView(titleView)

                    // Subtitle
                    val subTitleView = TextView(context).apply {
                        text = "You've reached your daily Reel limit. Take a moment before deciding whether to continue."
                        textSize = 14f
                        setTextColor(Color.parseColor("#A8A29E"))
                        gravity = Gravity.CENTER
                        setPadding(0, 0, 0, dpToPx(16))
                    }
                    addView(subTitleView)

                    // Stat Badge
                    val statBadge = TextView(context).apply {
                        text = "$currentReels / $totalAllowance Reels • 0 Remaining"
                        textSize = 12f
                        setTextColor(Color.parseColor("#F59E0B"))
                        typeface = Typeface.DEFAULT_BOLD
                        setPadding(dpToPx(12), dpToPx(6), dpToPx(12), dpToPx(6))
                        val badgeBg = GradientDrawable().apply {
                            shape = GradientDrawable.RECTANGLE
                            cornerRadius = dpToPx(12).toFloat()
                            setColor(Color.parseColor("#2E1C0A"))
                        }
                        background = badgeBg
                    }
                    addView(statBadge)

                    // Spacer
                    val spacer = View(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            dpToPx(24)
                        )
                    }
                    addView(spacer)

                    // Primary Button: "Start maths challenge"
                    val challengeButton = LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        gravity = Gravity.CENTER
                        setPadding(dpToPx(16), dpToPx(14), dpToPx(16), dpToPx(14))
                        val btnBg = GradientDrawable().apply {
                            shape = GradientDrawable.RECTANGLE
                            cornerRadius = dpToPx(16).toFloat()
                            setColor(Color.parseColor("#D97706")) // Amber accent
                        }
                        background = btnBg
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        val btnTitle = TextView(context).apply {
                            text = "Start maths challenge"
                            textSize = 16f
                            setTextColor(Color.parseColor("#0C0A09"))
                            typeface = Typeface.DEFAULT_BOLD
                        }
                        addView(btnTitle)

                        val btnSub = TextView(context).apply {
                            text = "6 questions · Unlock 20 more"
                            textSize = 12f
                            setTextColor(Color.parseColor("#292524"))
                        }
                        addView(btnSub)

                        setOnClickListener {
                            hideInterruption()
                            onStartChallenge()
                        }
                    }
                    addView(challengeButton)

                    // Spacer between buttons
                    val btnSpacer = View(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            dpToPx(12)
                        )
                    }
                    addView(btnSpacer)

                    // Secondary Button: "I'm done for today"
                    val doneButton = Button(context).apply {
                        text = "I'm done for today"
                        textSize = 15f
                        setTextColor(Color.parseColor("#D6D3D1"))
                        val btnBg = GradientDrawable().apply {
                            shape = GradientDrawable.RECTANGLE
                            cornerRadius = dpToPx(16).toFloat()
                            setColor(Color.parseColor("#292524"))
                            setStroke(dpToPx(1), Color.parseColor("#44403C"))
                        }
                        background = btnBg
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            dpToPx(52)
                        )

                        setOnClickListener {
                            hideInterruption()
                            onDoneForToday()
                        }
                    }
                    addView(doneButton)
                }
                addView(card)
            }

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                getOverlayType(),
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.CENTER
            }

            try {
                windowManager.addView(rootLayout, params)
                interruptionView = rootLayout
                isInterruptionShowing = true
            } catch (e: Exception) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                    params.type == WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY &&
                    android.provider.Settings.canDrawOverlays(context)
                ) {
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                    windowManager.addView(rootLayout, params)
                    interruptionView = rootLayout
                    isInterruptionShowing = true
                } else {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideInterruption() {
        if (!isInterruptionShowing || interruptionView == null) return
        try {
            windowManager.removeView(interruptionView)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            interruptionView = null
            isInterruptionShowing = false
        }
    }

    fun hideAll() {
        hidePill()
        hideInterruption()
    }
}
