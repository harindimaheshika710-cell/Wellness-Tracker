package com.example.personalwellnessapp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataManager(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Habits
    fun saveHabits(habits: List<Habit>) {
        val json = gson.toJson(habits)
        prefs.edit().putString("habits", json).apply()
    }

    fun getHabits(): MutableList<Habit> {
        val json = prefs.getString("habits", null)
        return if (json != null) gson.fromJson(json, object : TypeToken<MutableList<Habit>>() {}.type)
        else mutableListOf()
    }

    // Mood
    fun saveMoods(moods: List<MoodEntry>) {
        val json = gson.toJson(moods)
        prefs.edit().putString("moods", json).apply()
    }

    fun getMoods(): MutableList<MoodEntry> {
        val json = prefs.getString("moods", null)
        return if (json != null) gson.fromJson(json, object : TypeToken<MutableList<MoodEntry>>() {}.type)
        else mutableListOf()
    }
}
