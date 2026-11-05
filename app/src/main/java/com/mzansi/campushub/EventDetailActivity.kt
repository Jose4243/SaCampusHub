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
        const val EXTRA_EVENT_TIME = "extra_event_time"
        const val EXTRA_EVENT_LOCATION = "extra_event_location"
        const val EXTRA_EVENT_CATEGORY = "extra_event_category"
        const val EXTRA_EVENT_DESCRIPTION = "extra_event_description"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        val detailImage = findViewById<ImageView>(R.id.detailImage)
        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvDate = findViewById<TextView>(R.id.tvDetailDate)
        val tvMeta = findViewById<TextView>(R.id.tvDetailMeta)
        val tvDescription = findViewById<TextView>(R.id.tvDetailDescription)
        val btnRsvp = findViewById<Button>(R.id.btnRsvp)
        val btnCalendar = findViewById<Button>(R.id.btnCalendar)
        val btnBack = findViewById<Button>(R.id.btnBack)

        val title = intent.getStringExtra(EXTRA_EVENT_TITLE) ?: "Event Title"
        val date = intent.getStringExtra(EXTRA_EVENT_DATE) ?: "2026-10-15"
        val time = intent.getStringExtra(EXTRA_EVENT_TIME) ?: "09:00 AM"
        val location = intent.getStringExtra(EXTRA_EVENT_LOCATION) ?: "Main Campus"
        val category = intent.getStringExtra(EXTRA_EVENT_CATEGORY) ?: "General"

        val description = when {
            title.contains("Hackathon", ignoreCase = true) ->
                "Join us for the ultimate 24-hour student coding challenge! Collaborate in teams of up to 4 to build innovative software solutions addressing real-world campus and community challenges. Free pizza, energy drinks, high-speed Wi-Fi, and 24/7 technical mentorship from industry experts will be provided. Awesome cash prizes and tech gear up for grabs for the winning teams!"
            title.contains("Career", ignoreCase = true) ->
                "Network directly with top-tier software engineering recruiters, tech giants, and innovative startups looking to hire graduates for internships and full-time roles. Bring multiple printed copies of your CV, ensure your LinkedIn and GitHub portfolios are up to date, and prepare for on-the-spot networking sessions, resume reviews, and mini-interviews."
            title.contains("Workshop", ignoreCase = true) ->
                "Dive into the exciting world of Artificial Intelligence! Learn how to build and train your first neural network using Python, TensorFlow, and Hugging Face models. Led by Department of Computer Science faculty, this hands-on workshop covers practical prompt engineering, LLM integration, and ethical AI development. Laptops are required."
            title.contains("Soccer", ignoreCase = true) || title.contains("Sports", ignoreCase = true) ->
                "The ultimate showdown between campus residences! Come support your residence hall as they battle it out on the pitch for the championship trophy and bragging rights. Enjoy food trucks, half-time entertainment, and supporter gear giveaways. Entry is free for all registered students with valid student IDs."
            title.contains("Art", ignoreCase = true) || title.contains("Film", ignoreCase = true) ->
                "Showcasing incredible creative work from student filmmakers, digital artists, and photographers across campus. Enjoy independent short film screenings, interactive gallery walkthroughs, and Q&A sessions with award-winning student directors and local guest judges. Refreshments and canapés will be served during the networking mixer."
            else ->
                "Celebrate the end of midterms and welcome the summer season with the annual SRC Spring Bash! Enjoy live music performances from student bands and local DJs, diverse food stalls, gaming tournaments, interactive student club booths, and giveaways. Don't forget to bring your student card for free entry, complimentary snacks, and exclusive event merchandise!"
        }

        tvTitle.text = title
        tvDate.text = "📅 Date: $date at $time"
        tvMeta.text = "📍 Location: $location  |  🏷️ Category: $category"
        tvDescription.text = description

        val imageResId = when {
            title.contains("Hackathon", ignoreCase = true) -> R.drawable.event_hackathon
            title.contains("Career", ignoreCase = true) -> R.drawable.event_career
            title.contains("Workshop", ignoreCase = true) -> R.drawable.event_workshop
            title.contains("Soccer", ignoreCase = true) || title.contains("Sports", ignoreCase = true) -> R.drawable.event_sports
            title.contains("Art", ignoreCase = true) || title.contains("Film", ignoreCase = true) -> R.drawable.event_arts
            else -> R.drawable.event_party
        }
        detailImage.setImageResource(imageResId)

        val prefs = getSharedPreferences("RsvpPrefs", MODE_PRIVATE)
        if (prefs.getBoolean(title, false)) {
            btnRsvp.text = "✓ You are Attending"
        }

        btnRsvp.setOnClickListener {
            prefs.edit().putBoolean(title, true).apply()
            btnRsvp.text = "✓ You are Attending"
            Toast.makeText(this, "RSVP Confirmed for $title!", Toast.LENGTH_SHORT).show()
        }

        btnCalendar.setOnClickListener {
            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, title)
                putExtra(CalendarContract.Events.DESCRIPTION, description)
                putExtra(CalendarContract.Events.EVENT_LOCATION, location)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Calendar app not available.", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener { finish() }
    }
}