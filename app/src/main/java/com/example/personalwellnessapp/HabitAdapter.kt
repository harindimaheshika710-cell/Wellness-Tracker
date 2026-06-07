package com.example.personalwellnessapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class HabitAdapter(
    private val habits: MutableList<Habit>,
    private val onHabitChanged: () -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    class HabitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkHabit)
        val editButton: ImageView = view.findViewById(R.id.editHabit)
        val deleteButton: ImageView = view.findViewById(R.id.deleteHabit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        holder.checkBox.text = habit.name
        holder.checkBox.isChecked = habit.completed
        
        // Apply strikethrough effect for completed habits
        if (habit.completed) {
            holder.checkBox.paintFlags = holder.checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.checkBox.paintFlags = holder.checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        
        // Remove any existing listener to prevent issues when views are recycled
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            habit.completed = isChecked
            // Apply or remove strikethrough effect
            if (isChecked) {
                holder.checkBox.paintFlags = holder.checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.checkBox.paintFlags = holder.checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            onHabitChanged()
        }
        
        // Handle edit button click
        holder.editButton.setOnClickListener {
            showEditDialog(holder.itemView.context, habit, position)
        }
        
        // Handle delete button click
        holder.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, position)
        }
    }

    override fun getItemCount(): Int = habits.size
    
    // Method to add a new habit
    fun addHabit(habit: Habit) {
        habits.add(habit)
        notifyItemInserted(habits.size - 1)
    }
    
    // Method to remove a habit at specific position
    fun removeHabit(position: Int) {
        if (position >= 0 && position < habits.size) {
            habits.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    
    // Method to update a habit at specific position
    fun updateHabit(position: Int, newName: String) {
        if (position >= 0 && position < habits.size) {
            habits[position].name = newName
            notifyItemChanged(position)
        }
    }
    
    private fun showEditDialog(context: android.content.Context, habit: Habit, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Habit")
        
        val input = TextInputEditText(context)
        input.setText(habit.name)
        input.setPadding(60, 60, 60, 60)
        
        builder.setView(input)
        
        builder.setPositiveButton("Save") { _, _ ->
            val newName = input.text.toString().trim()
            if (newName.isNotEmpty()) {
                updateHabit(position, newName)
                onHabitChanged()
            }
        }
        
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        
        builder.show()
    }
    
    private fun showDeleteConfirmationDialog(context: android.content.Context, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Habit")
        builder.setMessage("Are you sure you want to delete this habit?")
        
        builder.setPositiveButton("Delete") { _, _ ->
            removeHabit(position)
            onHabitChanged()
        }
        
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        
        builder.show()
    }
}