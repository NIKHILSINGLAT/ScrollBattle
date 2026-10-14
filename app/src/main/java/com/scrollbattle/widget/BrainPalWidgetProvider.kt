package com.scrollbattle.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.widget.RemoteViews
import com.scrollbattle.MainActivity
import com.scrollbattle.R
import com.scrollbattle.data.DefaultScrollBattleRepository

class BrainPalWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val repo = DefaultScrollBattleRepository.getInstance(context)
        val state = repo.state.value

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_brainpal_buddy)

            val isPermissionsMissing = !state.accessibilityGranted && !state.usageAccessGranted
            val isOverGoal = state.todayReels >= state.totalAllowance && state.totalAllowance > 0

            // 1. Draw dynamic circular progress gauge bitmap
            val gaugeBitmap = createGaugeBitmap(
                reels = state.todayReels,
                allowance = state.totalAllowance,
                isOverGoal = isOverGoal
            )
            views.setImageViewBitmap(R.id.widget_gauge_image, gaugeBitmap)

            // 2. Set Status Texts, Colors & Mascot
            if (isPermissionsMissing) {
                views.setTextViewText(R.id.widget_status_title, "Setup required")
                views.setTextViewText(R.id.widget_status_subtitle, "Tap to enable")
                views.setTextViewText(R.id.widget_top_emoji, "⚠️")
                views.setImageViewResource(R.id.widget_mascot_image, R.drawable.brain_tired)
                views.setInt(R.id.widget_root, "setBackgroundColor", 0xFF2A1C14.toInt())
            } else if (isOverGoal) {
                views.setTextViewText(R.id.widget_status_title, "Over your goal")
                views.setTextViewText(R.id.widget_status_subtitle, "🚨 Limit reached")
                views.setTextViewText(R.id.widget_top_emoji, "😢")
                views.setImageViewResource(R.id.widget_mascot_image, R.drawable.brain_tired)
                views.setInt(R.id.widget_root, "setBackgroundColor", 0xFF4A121A.toInt())
            } else {
                views.setTextViewText(R.id.widget_status_title, "Keep the streak!")
                views.setTextViewText(R.id.widget_status_subtitle, "🔥 Under goal")
                views.setTextViewText(R.id.widget_top_emoji, "✨")
                views.setImageViewResource(R.id.widget_mascot_image, R.drawable.brain_happy)
                views.setInt(R.id.widget_root, "setBackgroundColor", 0xFF143823.toInt())
            }

            // 3. Set Click to open ScrollBattle
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
    companion object {
        fun updateAllWidgets(context: Context) {
            try {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val component = ComponentName(context, BrainPalWidgetProvider::class.java)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(component)
                if (appWidgetIds.isNotEmpty()) {
                    val intent = Intent(context, BrainPalWidgetProvider::class.java).apply {
                        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                    }
                    context.sendBroadcast(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun requestPinWidget(context: Context): Boolean {
            return try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val component = ComponentName(context, BrainPalWidgetProvider::class.java)
                    if (appWidgetManager.isRequestPinAppWidgetSupported) {
                        appWidgetManager.requestPinAppWidget(component, null, null)
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }



        private fun createGaugeBitmap(reels: Int, allowance: Int, isOverGoal: Boolean): Bitmap {
            val size = 220
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val strokeWidth = 16f
            val padding = strokeWidth / 2f + 4f
            val rect = RectF(padding, padding, size - padding, size - padding)

            // Background circle track
            val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                this.strokeWidth = strokeWidth
                color = Color.parseColor("#33FFFFFF")
            }
            canvas.drawOval(rect, trackPaint)

            // Foreground arc
            val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                this.strokeWidth = strokeWidth
                strokeCap = Paint.Cap.ROUND
                color = if (isOverGoal) Color.parseColor("#EF4444") else Color.parseColor("#22C55E")
            }
            val sweep = if (allowance > 0) {
                ((reels.toFloat() / allowance) * 360f).coerceIn(0f, 360f)
            } else 0f
            canvas.drawArc(rect, -90f, sweep, false, arcPaint)

            // Center count text
            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                textSize = 58f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            val textY = size / 2f + 6f
            canvas.drawText(reels.toString(), size / 2f, textY, textPaint)

            // Subtext "of <allowance>"
            val subTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#D1D5DB")
                textSize = 22f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("of $allowance", size / 2f, textY + 28f, subTextPaint)

            return bitmap
        }
    }
}
