package com.example.personalwellnessapp

data class Habit(var name: String, var completed: Boolean)
data class MoodEntry(val date: String, val emoji: String, val note: String)