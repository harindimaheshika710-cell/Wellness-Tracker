package com.example.personalwellnessapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var dataManager: DataManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        
        dataManager = DataManager(requireContext())
        
        // Set current date
        val currentDate = view.findViewById<TextView>(R.id.tvCurrentDate)
        val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
        currentDate.text = dateFormat.format(Date())
        
        // Update progress stats
        updateProgressStats(view)
        
        // Set up button listeners
        val addHabitButton = view.findViewById<View>(R.id.btnAddHabit)
        val logMoodButton = view.findViewById<View>(R.id.btnLogMood)
        
        addHabitButton.setOnClickListener {
            // Switch to habits fragment
            (activity as? MainActivity)?.switchToHabitsFragment()
        }
        
        logMoodButton.setOnClickListener {
            // Switch to mood fragment
            (activity as? MainActivity)?.switchToMoodFragment()
        }
        
        return view
    }
    
    override fun onResume() {
        super.onResume()
        view?.let { updateProgressStats(it) }
    }
    
    private fun updateProgressStats(view: View) {
        val habits = dataManager.getHabits()
        val moods = dataManager.getMoods()
        
        // Update habit progress
        val habitProgress = view.findViewById<TextView>(R.id.tvHabitProgress)
        val completedHabits = habits.count { it.completed }
        habitProgress.text = "$completedHabits/${habits.size} completed"
        
        // Update mood status
        val moodStatus = view.findViewById<TextView>(R.id.tvMoodStatus)
        if (moods.isNotEmpty()) {
            moodStatus.text = "Logged today"
            moodStatus.setTextColor(resources.getColor(R.color.success, null))
        } else {
            moodStatus.text = "Not logged"
            moodStatus.setTextColor(resources.getColor(R.color.text_secondary, null))
        }
    }
}