package com.mzansi.campushub

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

/**
 * Displays the full details (title, date, description) of a single campus
 * event that the user tapped on the events feed in [MainActivity].
 *
 * The event's fields are passed as individual String Intent extras rather
 * than a serialized object, keyed by [EXTRA_EVENT_TITLE], [EXTRA_EVENT_DATE]
 * and [EXTRA_EVENT_DESCRIPTION]. Extraction also falls back to a set of
 * shorter/alternate key names (e.g. "title") so this screen keeps working
 * if it's ever launched from another entry point that used those instead.
 */
class EventDetailActivity : BaseActivity() {

    companion object {
        const val EXTRA_EVENT_TITLE = "EVENT_TITLE"
        const val EXTRA_EVENT_DATE = "EVENT_DATE"
        const val EXTRA_EVENT_DESCRIPTION = "EVENT_DESCRIPTION"

        // Alternate/fallback keys accepted for backwards compatibility.
        private val TITLE_FALLBACK_KEYS = listOf("title", "event_title")
        private val DATE_FALLBACK_KEYS = listOf("date", "event_date")
        private val DESCRIPTION_FALLBACK_KEYS = listOf("description", "event_description")
    }

    private lateinit var tvDetailHeader: TextView
    private lateinit var tvDetailTitle: TextView
    private lateinit var tvDetailDate: TextView
    private lateinit var tvDetailDescription: TextView
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        tvDetailHeader = findViewById<TextView>(R.id.tvDetailHeader)
        tvDetailTitle = findViewById<TextView>(R.id.tvDetailTitle)
        tvDetailDate = findViewById<TextView>(R.id.tvDetailDate)
        tvDetailDescription = findViewById<TextView>(R.id.tvDetailDescription)
        btnBack = findViewById<Button>(R.id.btnBack)

        val title = extractExtra(EXTRA_EVENT_TITLE, TITLE_FALLBACK_KEYS)
        val date = extractExtra(EXTRA_EVENT_DATE, DATE_FALLBACK_KEYS)
        val description = extractExtra(EXTRA_EVENT_DESCRIPTION, DESCRIPTION_FALLBACK_KEYS)

        if (title.isNullOrBlank() && date.isNullOrBlank() && description.isNullOrBlank()) {

            finish()
            return
        }

        bindEvent(title, date, description)

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun bindEvent(title: String?, date: String?, description: String?) {
        tvDetailTitle.text = title.orEmpty()
        tvDetailDate.text = date.orEmpty()
        tvDetailDescription.text = description.orEmpty()
    }

    /**
     * Reads a String extra from [getIntent], trying [primaryKey] first and
     * then each key in [fallbackKeys] in order, so the screen tolerates
     * callers that used a slightly different naming convention.
     */
    private fun extractExtra(primaryKey: String, fallbackKeys: List<String>): String? {
        intent.getStringExtra(primaryKey)?.let { return it }

        for (fallbackKey in fallbackKeys) {
            intent.getStringExtra(fallbackKey)?.let { return it }
        }

        return null
    }
}
