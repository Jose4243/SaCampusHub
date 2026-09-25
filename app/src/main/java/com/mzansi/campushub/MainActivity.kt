package com.mzansi.campushub

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

/**
 * Main hub / dashboard screen shown after a user registers or logs in.
 *
 * Displays a personalised welcome message (derived from what was saved to
 * "UserPrefs" during registration) and fetches the list of campus events
 * from the backend using [RetrofitClient].
 */
class MainActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var tvStatus: TextView
    private lateinit var progressEvents: ProgressBar
    private lateinit var rvEvents: RecyclerView
    private lateinit var btnRefresh: Button

    private lateinit var userPrefs: SharedPreferences
    private lateinit var eventsAdapter: EventsAdapter

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        tvStatus = findViewById<TextView>(R.id.tvStatus)
        progressEvents = findViewById<ProgressBar>(R.id.progressEvents)
        rvEvents = findViewById<RecyclerView>(R.id.rvEvents)
        btnRefresh = findViewById<Button>(R.id.btnRefresh)

        userPrefs = getSharedPreferences(RegisterActivity.PREFS_NAME, MODE_PRIVATE)

        setupWelcomeMessage()
        setupRecyclerView()

        btnRefresh.setOnClickListener {
            fetchEvents()
        }

        fetchEvents()
    }

    private fun setupWelcomeMessage() {
        val isRegistered = userPrefs.getBoolean(RegisterActivity.KEY_IS_REGISTERED, false)
        val email = userPrefs.getString(RegisterActivity.KEY_EMAIL, null)

        tvWelcome.text = if (isRegistered && !email.isNullOrEmpty()) {
            "Welcome back, $email!"
        } else {
            "Welcome to Mzansi Campus Hub"
        }
    }

    private fun setupRecyclerView() {
        eventsAdapter = EventsAdapter(emptyList())
        rvEvents.layoutManager = LinearLayoutManager(this)
        rvEvents.adapter = eventsAdapter
    }

    private fun fetchEvents() {
        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getEvents()

                if (response.isSuccessful) {
                    val events = response.body() ?: emptyList()
                    if (events.isEmpty()) {
                        tvStatus.text = "No upcoming events right now. Check back soon!"
                    } else {
                        tvStatus.text = "Showing ${events.size} upcoming campus event(s)."
                    }
                    eventsAdapter.updateEvents(events)
                } else {
                    tvStatus.text = "Could not load events (error ${response.code()})."
                    Log.e(TAG, "Failed to fetch events: HTTP ${response.code()}")
                }
            } catch (e: Exception) {
                tvStatus.text = "Unable to reach the server. Please check your connection."
                Log.e(TAG, "Exception while fetching events", e)
                Toast.makeText(
                    this@MainActivity,
                    "Network error: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressEvents.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnRefresh.isEnabled = !isLoading
    }
}
