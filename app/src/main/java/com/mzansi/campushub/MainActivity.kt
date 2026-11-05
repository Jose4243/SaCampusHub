package com.mzansi.campushub

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: CampusEventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Settings Click
        val tvSettings = findViewById<TextView>(R.id.tvSettings)
        tvSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val eventList = listOf(
            CampusEvent(
                title = "Campus Hackathon 2026",
                date = "2026-10-15",
                time = "09:00 AM",
                location = "Innovation Lab, Block B",
                category = "Technology & Coding",
                description = "Join us for the ultimate 24-hour student coding challenge! Collaborate in teams of up to 4 to build innovative software solutions addressing real-world campus and community challenges. Free pizza, energy drinks, high-speed Wi-Fi, and 24/7 technical mentorship from industry experts will be provided. Awesome cash prizes and tech gear up for grabs for the winning teams!"
            ),
            CampusEvent(
                title = "Annual Tech & Career Fair",
                date = "2026-10-22",
                time = "10:00 AM",
                location = "Great Hall & Main Concourse",
                category = "Career & Employment",
                description = "Network directly with top-tier software engineering recruiters, tech giants, and innovative startups looking to hire graduates for internships and full-time roles. Bring multiple printed copies of your CV, ensure your LinkedIn and GitHub portfolios are up to date, and prepare for on-the-spot networking sessions, resume reviews, and mini-interviews."
            ),
            CampusEvent(
                title = "SRC Spring Bash",
                date = "2026-11-05",
                time = "02:00 PM",
                location = "Main Campus Quad",
                category = "Student Life & Social",
                description = "Celebrate the end of midterms and welcome the summer season with the annual SRC Spring Bash! Enjoy live music performances from student bands and local DJs, diverse food stalls, gaming tournaments, interactive student club booths, and giveaways. Don't forget to bring your student card for free entry, complimentary snacks, and exclusive event merchandise!"
            ),
            CampusEvent(
                title = "AI & Machine Learning Workshop",
                date = "2026-11-12",
                time = "01:00 PM",
                location = "Computer Lab 4A",
                category = "Academic & Tech",
                description = "Dive into the exciting world of Artificial Intelligence! Learn how to build and train your first neural network using Python, TensorFlow, and Hugging Face models. Led by Department of Computer Science faculty, this hands-on workshop covers practical prompt engineering, LLM integration, and ethical AI development. Laptops are required."
            ),
            CampusEvent(
                title = "Inter-Res Varsity Soccer Cup",
                date = "2026-11-20",
                time = "03:30 PM",
                location = "Main Campus Sports Complex",
                category = "Sports & Recreation",
                description = "The ultimate showdown between campus residences! Come support your residence hall as they battle it out on the pitch for the championship trophy and bragging rights. Enjoy food trucks, half-time entertainment, and supporter gear giveaways. Entry is free for all registered students with valid student IDs."
            ),
            CampusEvent(
                title = "Campus Art & Film Festival",
                date = "2026-11-28",
                time = "06:00 PM",
                location = "Student Center Auditorium",
                category = "Arts & Culture",
                description = "Showcasing incredible creative work from student filmmakers, digital artists, and photographers across campus. Enjoy independent short film screenings, interactive gallery walkthroughs, and Q&A sessions with award-winning student directors and local guest judges. Refreshments and canapés will be served during the networking mixer."
            )
        )

        eventAdapter = CampusEventAdapter(eventList) { event ->
            val intent = Intent(this, EventDetailActivity::class.java).apply {
                putExtra(EventDetailActivity.EXTRA_EVENT_TITLE, event.title)
                putExtra(EventDetailActivity.EXTRA_EVENT_DATE, event.date)
                putExtra(EventDetailActivity.EXTRA_EVENT_TIME, event.time)
                putExtra(EventDetailActivity.EXTRA_EVENT_LOCATION, event.location)
                putExtra(EventDetailActivity.EXTRA_EVENT_CATEGORY, event.category)
                putExtra(EventDetailActivity.EXTRA_EVENT_DESCRIPTION, event.description)
            }
            startActivity(intent)
        }

        recyclerView.adapter = eventAdapter
    }
}