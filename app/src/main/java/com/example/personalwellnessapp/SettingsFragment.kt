package com.example.personalwellnessapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsFragment : Fragment() {
    
    private lateinit var notificationHelper: NotificationHelper
    
    override fun onCreateView(
        inflater: LayoutInflater, 
        container: ViewGroup?, 
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        
        notificationHelper = NotificationHelper(requireContext())
        
        val reminderSwitch = view.findViewById<SwitchMaterial>(R.id.switchReminder)
        val intervalPicker = view.findViewById<NumberPicker>(R.id.numberPickerInterval)
        val saveButton = view.findViewById<Button>(R.id.btnSaveSettings)
        val saveStatus = view.findViewById<TextView>(R.id.tvSaveStatus)
        
        // Configure number picker
        intervalPicker.minValue = 1
        intervalPicker.maxValue = 120
        intervalPicker.value = 30 // Default to 30 minutes
        
        saveButton.setOnClickListener {
            if (reminderSwitch.isChecked) {
                // Schedule repeating notification
                val intervalMinutes = intervalPicker.value.toLong()
                notificationHelper.scheduleRepeatingNotification(intervalMinutes)
                saveStatus.text = "Settings saved successfully! Reminders enabled."
            } else {
                // Cancel scheduled notifications
                notificationHelper.cancelScheduledNotifications()
                saveStatus.text = "Settings saved successfully! Reminders disabled."
            }
            
            // Show success message
            saveStatus.visibility = View.VISIBLE
            
            // Hide the message after 3 seconds
            saveStatus.postDelayed({
                saveStatus.visibility = View.GONE
            }, 3000)
        }
        
        return view
    }
}