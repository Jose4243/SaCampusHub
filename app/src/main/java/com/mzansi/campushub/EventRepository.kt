package com.mzansi.campushub

object EventRepository {
    fun getSampleEvents(): List<CampusEvent> {
        return listOf(
            CampusEvent(
                title = "Campus Hackathon 2026",
                date = "2026-10-15",
                time = "09:00 AM - 09:00 AM (24 Hours)",
                location = "Innovation Lab, Block B",
                category = "Technology & Coding",
                description = "Join us for the ultimate 24-hour student coding challenge! Collaborate in teams of up to 4 to build innovative software solutions addressing real-world campus and community challenges. Free pizza, energy drinks, high-speed Wi-Fi, and 24/7 technical mentorship from industry experts will be provided. Awesome cash prizes and tech gear up for grabs for the winning teams!"
            ),
            CampusEvent(
                title = "Annual Tech & Career Fair",
                date = "2026-10-22",
                time = "10:00 AM - 04:00 PM",
                location = "Great Hall & Main Concourse",
                category = "Career & Employment",
                description = "Network directly with top-tier software engineering recruiters, tech giants, and innovative startups looking to hire graduates for internships and full-time roles. Bring multiple printed copies of your CV, ensure your LinkedIn and GitHub portfolios are up to date, and prepare for on-the-spot networking sessions, resume reviews, and mini-interviews."
            ),
            CampusEvent(
                title = "SRC Spring Bash",
                date = "2026-11-05",
                time = "02:00 PM - 10:00 PM",
                location = "Main Campus Quad",
                category = "Student Life & Social",
                description = "Celebrate the end of midterms and welcome the summer season with the annual SRC Spring Bash! Enjoy live music performances from student bands and local DJs, diverse food stalls, gaming tournaments, interactive student club booths, and giveaways. Don't forget to bring your student card for free entry, complimentary snacks, and exclusive event merchandise!"
            )
        )
    }
}