package com.mzansi.campushub

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var tvFeedHeader: TextView
    private lateinit var btnSettings: Button
    private lateinit var tvFeedStatus: TextView
    private lateinit var rvCampusEvents: RecyclerView
    private lateinit var btnLogout: Button

    private lateinit var userPrefs: SharedPreferences
    private lateinit var campusEventAdapter: CampusEventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvFeedHeader = findViewById<TextView>(R.id.tvFeedHeader)
        btnSettings = findViewById<Button>(R.id.btnSettings)
        tvFeedStatus = findViewById<TextView>(R.id.tvFeedStatus)
        rvCampusEvents = findViewById<RecyclerView>(R.id.rvCampusEvents)
        btnLogout = findViewById<Button>(R.id.btnLogout)

        userPrefs = getSharedPreferences(RegisterActivity.PREFS_NAME, MODE_PRIVATE)

        setupRecyclerView()
        fetchCampusEvents()

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun setupRecyclerView() {
        campusEventAdapter = CampusEventAdapter(emptyList()) { clickedEvent ->
            openEventDetail(clickedEvent)
        }
        rvCampusEvents.layoutManager = LinearLayoutManager(this)
        rvCampusEvents.adapter = campusEventAdapter
    }

    private fun openEventDetail(event: CampusEvent) {
        val intent = Intent(this, EventDetailActivity::class.java)
        intent.putExtra(EventDetailActivity.EXTRA_EVENT_TITLE, event.title)
        intent.putExtra(EventDetailActivity.EXTRA_EVENT_DATE, event.date)
        intent.putExtra(EventDetailActivity.EXTRA_EVENT_DESCRIPTION, event.description)
        startActivity(intent)
    }

    private fun fetchCampusEvents() {
        tvFeedStatus.text = "Loading events..."
        Log.d(TAG, "Requesting events from ${RetrofitClient.baseUrlForLogging()}events")

        RetrofitClient.instance.getEvents().enqueue(object : Callback<List<CampusEvent>> {
            override fun onResponse(
                call: Call<List<CampusEvent>>,
                response: Response<List<CampusEvent>>
            ) {
                Log.d(TAG, "HTTP ${response.code()} received. isSuccessful=${response.isSuccessful}")

                if (response.isSuccessful) {
                    val events = response.body() ?: emptyList()
                    campusEventAdapter.updateEvents(events)
                    Log.d(TAG, "Fetched ${events.size} campus event(s).")

                    tvFeedStatus.text = if (events.isEmpty()) {
                        "No events returned by the server."
                    } else {
                        "Showing ${events.size} event(s)."
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "Failed to load events: HTTP ${response.code()} - $errorBody")
                    tvFeedStatus.text = "Server error ${response.code()} while loading events."
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.msg_failed_to_load_events),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<CampusEvent>>, t: Throwable) {
                Log.e(TAG, "Network error while fetching events", t)
                tvFeedStatus.text = "Network error: ${t.localizedMessage}"
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.msg_failed_to_load_events),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun logout() {
        userPrefs.edit()
            .remove(RegisterActivity.KEY_EMAIL)
            .remove(RegisterActivity.KEY_PASSWORD_HASH)
            .remove(RegisterActivity.KEY_IS_REGISTERED)
            .apply()

        Toast.makeText(this, getString(R.string.msg_logged_out), Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}