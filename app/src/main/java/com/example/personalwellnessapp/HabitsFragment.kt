package com.example.personalwellnessapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ProgressBar
import android.widget.LinearLayout
import android.widget.CheckBox
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText

class HabitsFragment : Fragment() {

    private lateinit var dataManager: DataManager
    private lateinit var habitList: MutableList<Habit>
    private lateinit var habitsContainer: LinearLayout
    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_habits, container, false)
        rootView = view
        dataManager = DataManager(requireContext())

        habitList = dataManager.getHabits()
        habitsContainer = view.findViewById(R.id.containerHabits)

        val addButton = view.findViewById<Button>(R.id.btnAddHabit)
        val habitInput = view.findViewById<EditText>(R.id.etHabitName)
        addButton.setOnClickListener {
            val name = habitInput.text.toString()
            if (name.isNotEmpty()) {
                val newHabit = Habit(name, false)
                habitList.add(newHabit)
                saveHabits()
                habitInput.text.clear()
                updateHabitStats(view)
                addHabitToView(newHabit, habitList.size - 1)
            }
        }

        // Initialize habit stats and display existing habits
        updateHabitStats(view)
        displayAllHabits()

        return view
    }

    private fun displayAllHabits() {
        habitsContainer.removeAllViews()
        for (i in habitList.indices) {
            addHabitToView(habitList[i], i)
        }
    }

    private fun addHabitToView(habit: Habit, position: Int) {
        val habitView = layoutInflater.inflate(R.layout.item_habit_linear, habitsContainer, false)
        
        val checkBox = habitView.findViewById<CheckBox>(R.id.checkHabit)
        val editButton = habitView.findViewById<ImageView>(R.id.editHabit)
        val deleteButton = habitView.findViewById<ImageView>(R.id.deleteHabit)
        
        checkBox.text = habit.name
        checkBox.isChecked = habit.completed
        
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            habit.completed = isChecked
            saveHabits()
            updateHabitStats(rootView!!)
        }
        
        editButton.setOnClickListener {
            showEditDialog(habit, position)
        }
        
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog(position)
        }
        
        habitsContainer.addView(habitView)
    }

    private fun showEditDialog(habit: Habit, position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit Habit")
        
        val input = TextInputEditText(requireContext())
        input.setText(habit.name)
        input.setPadding(60, 60, 60, 60)
        
        builder.setView(input)
        
        builder.setPositiveButton("Save") { _, _ ->
            val newName = input.text.toString().trim()
            if (newName.isNotEmpty()) {
                habit.name = newName
                saveHabits()
                displayAllHabits()
                rootView?.let { updateHabitStats(it) }
            }
        }
        
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        
        builder.show()
    }
    
    private fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Habit")
        builder.setMessage("Are you sure you want to delete this habit?")
        
        builder.setPositiveButton("Delete") { _, _ ->
            if (position >= 0 && position < habitList.size) {
                habitList.removeAt(position)
                saveHabits()
                displayAllHabits()
                rootView?.let { updateHabitStats(it) }
            }
        }
        
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        
        builder.show()
    }

    private fun updateHabitStats(view: View) {
        // Update habit count
        val habitCountText = view.findViewById<TextView>(R.id.tvHabitCount)
        habitCountText.text = "${habitList.size} habit${if (habitList.size == 1) "" else "s"}"
        
        // Update progress
        val completedHabits = habitList.count { it.completed }
        val progressPercentage = if (habitList.isNotEmpty()) {
            (completedHabits.toDouble() / habitList.size.toDouble() * 100).toInt()
        } else {
            0
        }
        
        val progressBar = view.findViewById<ProgressBar>(R.id.progressHabits)
        val progressText = view.findViewById<TextView>(R.id.tvProgressText)
        val progressPercentageText = view.findViewById<TextView>(R.id.tvProgressPercentage)
        
        progressBar.progress = progressPercentage
        
        if (habitList.isEmpty()) {
            progressText.text = "Add habits to track your progress!"
            progressPercentageText.text = "0% completed"
        } else if (completedHabits == habitList.size) {
            progressText.text = "🎉 All habits completed! Great job!"
            progressPercentageText.text = "100% completed"
        } else {
            progressText.text = "$completedHabits of ${habitList.size} habits completed"
            progressPercentageText.text = "$progressPercentage% completed"
        }
    }

    private fun saveHabits() {
        dataManager.saveHabits(habitList)
    }
    
    // Refresh habits when fragment becomes visible
    override fun onResume() {
        super.onResume()
        habitList = dataManager.getHabits()
        displayAllHabits()
        rootView?.let { updateHabitStats(it) }
    }
}