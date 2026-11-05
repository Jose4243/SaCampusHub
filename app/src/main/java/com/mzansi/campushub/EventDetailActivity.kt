package com.mzansi.campushub

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class EventDetailActivity : BaseActivity() {

    companion object {
        const val EXTRA_EVENT_TITLE = "extra_event_title"
        const val EXTRA_EVENT_DATE = "extra_event_date"
        const val EXTRA_EVENT_DESCRIPTION = "extra_event_description"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        val detailImage = findViewById<ImageView>(R.id.detailImage)
        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvDate = findViewById<TextView>(R.id.tvDetailDate)
        val tvDescription = findViewById<TextView>(R.id.tvDetailDescription)
        val btnRsvp = findViewById<Button>(R.id.btnRsvp)
        val btnCalendar = findViewById<Button>(R.id.btnCalendar)
        val btnBack = findViewById<Button>(R.id.btnBack)

        val title = intent.getStringExtra(EXTRA_EVENT_TITLE) ?: "Event Title"
        val date = intent.getStringExtra(EXTRA_EVENT_DATE) ?: "2026-10-15"
        val description = intent.getStringExtra(EXTRA_EVENT_DESCRIPTION) ?: "No description available."

        tvTitle.text = title
        tvDate.text = date
        tvDescription.text = description

        // Correct image mapping for all events including Spring Bash
        val imageResId = when {
            title.contains("Hackathon", ignoreCase = true) -> R.drawable.event_hackathon
            title.contains("Career", ignoreCase = true) -> R.drawable.event_career
            else -> R.drawable.event_party
        }
        detailImage.setImageResource(imageResId)

        // RSVP feature with persistence
        val prefs = getSharedPreferences("RsvpPrefs", MODE_PRIVATE)
        val isRsvped = prefs.getBoolean(title, false)
        if (isRsvped) {
            btnRsvp.text = "✓ You are Attending"
        }

        btnRsvp.setOnClickListener {
            prefs.edit().putBoolean(title, true).apply()
            btnRsvp.text = "✓ You are Attending"
            Toast.makeText(this, "RSVP Confirmed for $title!", Toast.LENGTH_SHORT).show()
        }

        // Native Calendar Reminder Intent
        btnCalendar.setOnClickListener {
            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, title)
                putExtra(CalendarContract.Events.DESCRIPTION, description)
                putExtra(CalendarContract.Events.EVENT_LOCATION, "Mzansi Campus Main Quad")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Calendar app not available.", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}