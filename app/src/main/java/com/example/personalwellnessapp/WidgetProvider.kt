package com.example.personalwellnessapp

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.personalwellnessapp.Habit

class WidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val dataManager = DataManager(context)
        val habits = dataManager.getHabits()
        val completed = habits.count { it.completed }
        val percent = if (habits.isNotEmpty()) (completed * 100 / habits.size) else 0

        appWidgetIds.forEach { id ->
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.tvWidgetProgress, "Today's Habit Completion: $percent%")
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}