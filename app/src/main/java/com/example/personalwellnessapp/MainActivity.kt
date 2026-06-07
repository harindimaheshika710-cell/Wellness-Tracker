package com.example.personalwellnessapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var habitsFragment: HabitsFragment
    private lateinit var moodFragment: MoodJournalFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var dashboardFragment: DashboardFragment
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dashboardFragment = DashboardFragment()
        habitsFragment = HabitsFragment()
        moodFragment = MoodJournalFragment()
        settingsFragment = SettingsFragment()

        // Add all fragments to the container
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, dashboardFragment, "dashboard")
            .add(R.id.fragment_container, habitsFragment, "habits")
            .add(R.id.fragment_container, moodFragment, "mood")
            .add(R.id.fragment_container, settingsFragment, "settings")
            .hide(habitsFragment)
            .hide(moodFragment)
            .hide(settingsFragment)
            .commit()

        activeFragment = dashboardFragment

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    switchFragment(dashboardFragment)
                    true
                }
                R.id.navigation_habits -> {
                    switchFragment(habitsFragment)
                    true
                }
                R.id.navigation_mood -> {
                    switchFragment(moodFragment)
                    true
                }
                R.id.navigation_settings -> {
                    switchFragment(settingsFragment)
                    true
                }
                else -> false
            }
        }
        
        // Set default selected item
        navView.selectedItemId = R.id.navigation_dashboard
    }

    private fun switchFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .hide(dashboardFragment)
            .hide(habitsFragment)
            .hide(moodFragment)
            .hide(settingsFragment)
            .show(fragment)
            .commit()
        activeFragment = fragment
        return true
    }
    
    fun switchToHabitsFragment() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.selectedItemId = R.id.navigation_habits
    }
    
    fun switchToMoodFragment() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.selectedItemId = R.id.navigation_mood
    }
}