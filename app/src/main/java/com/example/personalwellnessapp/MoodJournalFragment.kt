package com.example.personalwellnessapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.widget.LinearLayout

class MoodJournalFragment : Fragment() {

    private lateinit var dataManager: DataManager
    private lateinit var moodAdapter: MoodAdapter
    private lateinit var moodList: MutableList<MoodEntry>
    private var selectedEmoji: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mood_journal, container, false)
        dataManager = DataManager(requireContext())
        moodList = dataManager.getMoods()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMoods)
        recyclerView.layoutManager = LinearLayoutManager(context)
        // Disable nested scrolling since we're using a ScrollView as parent
        recyclerView.isNestedScrollingEnabled = false
        moodAdapter = MoodAdapter(moodList)
        recyclerView.adapter = moodAdapter

        // Set current date
        val currentDateText = view.findViewById<TextView>(R.id.tvCurrentDate)
        val dateFormat = SimpleDateFormat("d'th' MMMM yyyy", Locale.getDefault())
        currentDateText.text = dateFormat.format(Date())

        // Set mood count
        val moodCountText = view.findViewById<TextView>(R.id.tvMoodCount)
        moodCountText.text = "${moodList.size} entr${if (moodList.size == 1) "y" else "ies"}"

        // Set up emoji selection
        setupEmojiSelection(view)

        val noteInput = view.findViewById<EditText>(R.id.etMoodNote)
        val submitButton = view.findViewById<Button>(R.id.btnSubmitMood)
        val backButton = view.findViewById<Button>(R.id.btnBack)
        val calendarButton = view.findViewById<Button>(R.id.btnCalendar)

        submitButton.setOnClickListener {
            selectedEmoji?.let { emoji ->
                val note = noteInput.text.toString()
                val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                moodList.add(0, MoodEntry(date, emoji, note)) // Add to beginning of list
                moodAdapter.notifyItemInserted(0)
                dataManager.saveMoods(moodList)
                noteInput.text.clear()
                clearEmojiSelection(view) // Clear selection after submission
                
                // Update mood count
                val updatedMoodCountText = view.findViewById<TextView>(R.id.tvMoodCount)
                updatedMoodCountText.text = "${moodList.size} entr${if (moodList.size == 1) "y" else "ies"}"
                
                // Scroll to the top to show the new entry
                recyclerView.scrollToPosition(0)
            }
        }

        backButton.setOnClickListener {
            // Navigate back to previous fragment
            parentFragmentManager.popBackStack()
        }

        calendarButton.setOnClickListener {
            // TODO: Implement calendar functionality
        }

        return view
    }

    private fun setupEmojiSelection(view: View) {
        val emojiMap = mapOf(
            "Happy" to "😊",
            "Sad" to "😔",
            "Angry" to "😡",
            "Anxious" to "😟",
            "Neutral" to "😐",
            "Excited" to "😍"
        )

        val emojiLayouts = mapOf(
            "Happy" to view.findViewById<LinearLayout>(R.id.emojiHappy),
            "Sad" to view.findViewById<LinearLayout>(R.id.emojiSad),
            "Angry" to view.findViewById<LinearLayout>(R.id.emojiAngry),
            "Anxious" to view.findViewById<LinearLayout>(R.id.emojiAnxious),
            "Neutral" to view.findViewById<LinearLayout>(R.id.emojiNeutral),
            "Excited" to view.findViewById<LinearLayout>(R.id.emojiExcited)
        )

        emojiLayouts.forEach { (name, layout) ->
            layout.setOnClickListener {
                // Clear previous selection
                clearEmojiSelection(view)
                
                // Highlight selected emoji
                val card = layout.getChildAt(0) as MaterialCardView
                card.strokeColor = ContextCompat.getColor(requireContext(), R.color.ice_blue_accent)
                card.strokeWidth = 4
                
                // Set selected emoji
                selectedEmoji = emojiMap[name]
            }
        }
    }

    private fun clearEmojiSelection(view: View) {
        val emojiLayouts = listOf(
            view.findViewById<LinearLayout>(R.id.emojiHappy),
            view.findViewById<LinearLayout>(R.id.emojiSad),
            view.findViewById<LinearLayout>(R.id.emojiAngry),
            view.findViewById<LinearLayout>(R.id.emojiAnxious),
            view.findViewById<LinearLayout>(R.id.emojiNeutral),
            view.findViewById<LinearLayout>(R.id.emojiExcited)
        )

        emojiLayouts.forEach { layout ->
            val card = layout.getChildAt(0) as MaterialCardView
            card.strokeColor = ContextCompat.getColor(requireContext(), R.color.dark_card)
            card.strokeWidth = 2
        }
        
        selectedEmoji = null
    }

    // Refresh moods when fragment becomes visible
    override fun onResume() {
        super.onResume()
        moodList = dataManager.getMoods()
        moodAdapter = MoodAdapter(moodList)
        view?.findViewById<RecyclerView>(R.id.recyclerMoods)?.adapter = moodAdapter
        
        // Update mood count
        view?.findViewById<TextView>(R.id.tvMoodCount)?.text = "${moodList.size} entr${if (moodList.size == 1) "y" else "ies"}"
    }
}