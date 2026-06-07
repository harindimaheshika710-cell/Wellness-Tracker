package com.example.personalwellnessapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MoodAdapter(private val moods: MutableList<MoodEntry>) :
    RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    class MoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMood: TextView = view.findViewById(R.id.tvMood)
        val tvNote: TextView = view.findViewById(R.id.tvNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mood, parent, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val mood = moods[position]
        // Display emoji first, then date
        holder.tvMood.text = "${mood.emoji}  ${mood.date}"
        holder.tvNote.text = if (mood.note.isNotEmpty()) mood.note else "No note added"
    }

    override fun getItemCount(): Int = moods.size
}